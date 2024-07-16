package com.example.graduationproject.presentation.creategroup

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.graduationproject.R
import com.example.graduationproject.databinding.FragmentCreateGroupBinding
import com.example.graduationproject.domain.entity.Group
import com.example.graduationproject.domain.entity.UserProfile
import com.example.graduationproject.presentation.addfriends.AddFriendsView.Companion.SELECTED_FRIENDS_LIST_KEY
import com.example.graduationproject.presentation.addfriends.AddFriendsView.Companion.SELECTED_FRIENDS_REQUEST_KEY
import com.example.graduationproject.presentation.createchallenge.CreateChallengeViewArgs
import com.example.graduationproject.presentation.creategroup.adapter.CreateGroupAdapter
import dagger.hilt.android.AndroidEntryPoint
import com.example.graduationproject.presentation.util.getSerializableCompat
import com.example.graduationproject.presentation.util.putArguments
import kotlinx.coroutines.launch
import java.io.Serializable

@AndroidEntryPoint
class CreateGroupView : Fragment() {

    private val viewModel: CreateGroupViewModel by viewModels()
    private var _binding: FragmentCreateGroupBinding? = null
    private val binding get() = _binding!!
    private lateinit var createGroupAction: Button
    private lateinit var loadAvatarAction: Button
    private lateinit var avatarNameView: TextView
    private lateinit var groupNameView: EditText
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var addFriendsAction: RelativeLayout
    private lateinit var friendsView: RecyclerView
    private lateinit var adapter: CreateGroupAdapter
    private lateinit var participants: MutableList<UserProfile>

    companion object {
        const val ADDED_FRIENDS_TO_GROUP_REQUEST_KEY = "ADDED_FRIENDS_TO_GROUP_REQUEST_KEY"
        const val ADDED_FRIENDS_TO_GROUP_LIST_KEY = "ADDED_FRIENDS_TO_GROUP_LIST_KEY"
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreate(savedInstanceState)
        _binding = FragmentCreateGroupBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        initAdapter()
        setFriends()
        observeFriends()
        observeGroupName()
        setUpAddFriendsButton()
        setUpCreateGroupAction()
    }

    private fun initViews() {
        createGroupAction = binding.createGroupAction
        loadAvatarAction = binding.uploadAction
        avatarNameView = binding.fileNameView
        groupNameView = binding.groupNameContent
        addFriendsAction = binding.addFriendsAction.rootButtonContainer
        friendsView = binding.friendsRecyclerView
    }

    private fun initAdapter() {
        friendsView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        adapter = CreateGroupAdapter(mutableListOf()) { friend ->
            viewModel.deleteFriend(friend)
        }
        friendsView.adapter = adapter
    }

    private fun setFriends() {
        parentFragmentManager.setFragmentResultListener(
            SELECTED_FRIENDS_REQUEST_KEY,
            this
        ) { _, bundle ->
            val friendsArrayList = bundle.getSerializableCompat(
                SELECTED_FRIENDS_LIST_KEY,
                ArrayList::class.java
            ) as? ArrayList<UserProfile>
            friendsArrayList?.let {
                viewModel.addFriends(it)
            }
        }
    }

    private fun observeFriends() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.addedFriends.collect { friends ->
                    adapter.setFriends(friends)
                }
            }
        }
    }

    private fun observeGroupName() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.groupName.collect { name ->
                    groupNameView.setText(name)
                }
            }
        }
    }

    private fun setUpAddFriendsButton() {
        addFriendsAction.setOnClickListener {
            setGroupName()
            setArgumentsToAddFriendsView()
            moveToAddFriendsView()
        }
    }

    private fun setGroupName() {
        val groupName = groupNameView.text.toString()
        viewModel.setGroupName(groupName)
    }

    private fun setArgumentsToAddFriendsView() {
        val arrayList = ArrayList(viewModel.addedFriends.value)
        this.putArguments(ADDED_FRIENDS_TO_GROUP_LIST_KEY to arrayList as Serializable)
        setFragmentResult(ADDED_FRIENDS_TO_GROUP_REQUEST_KEY, arguments ?: Bundle())
    }

    private fun moveToAddFriendsView() {
        val action =
            CreateGroupViewDirections.actionCreateGroupViewToAddFriendsView(null)
        findNavController().navigate(action)
    }

    private fun setUpCreateGroupAction() {
        createGroupAction.setOnClickListener {
            setGroup()
            handleOnGroupSet()
        }
    }

    private fun setGroup() {
        val creatorId = getCreatorId()
        val groupName = groupNameView.text.toString()
        val groupAvatar = null
        viewModel.setGroup(groupName, creatorId, groupAvatar)
    }

    private fun getCreatorId(): String {
        sharedPreferences =
            requireContext().getSharedPreferences("session_prefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("current_user_id", "  ") ?: "  "
    }

    private fun handleOnGroupSet() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.viewState.collect() {
                    Log.d("observeAddedGroup", "New view state: $it")
                    when (it) {
                        is CreateGroupViewState.Success -> {
                            val group = it.data
                            setUserGroup(group)
                            notifyGroupParticipants(participants, group)
                            moveToGroupDetailsScreen(group)
                            Log.d("observeAddedGroups", "Success view state, data: ${it.data}")
                        }

                        is CreateGroupViewState.Loading -> {
                            Log.d("observeAddedGroup", "Loading view state")
                        }

                        is CreateGroupViewState.Failure -> {
                            Log.d("observeAddedGroup", "Failure view state")
                        }
                    }
                }
            }
        }
    }

    private fun notifyGroupParticipants(participants: MutableList<UserProfile>, group: Group) {
        val creatorName = sharedPreferences.getString("current_username", "  ") ?: "  "
        val creatorId = getCreatorId()
        val message =
            "{ \"creatorName\": \"$creatorName\", \"groupName\": \"${group.groupName}\", \"groupId\": \"${group.groupId}\" }"
        viewModel.notifyParticipants(participants, message, group, creatorId)
    }

    private fun setUserGroup(group: Group) {
        participants = viewModel.addedFriends.value
        viewModel.addGroupParticipants(group.groupId, participants)
    }

    private fun moveToGroupDetailsScreen(group: Group) {
        val action =
            CreateGroupViewDirections.actionCreateGroupViewToGroupDetailsView(group.groupId)
        findNavController().navigate(action)
    }
}