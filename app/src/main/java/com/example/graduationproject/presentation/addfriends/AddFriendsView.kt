package com.example.graduationproject.presentation.addfriends

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import androidx.cardview.widget.CardView
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
import com.example.graduationproject.databinding.FragmentAddFriendsBinding
import com.example.graduationproject.domain.entity.UserProfile
import com.example.graduationproject.presentation.addfriends.adapter.AddFriendsAdapter
import com.example.graduationproject.presentation.createchallenge.CreateChallengeView
import com.example.graduationproject.presentation.creategroup.CreateGroupView
import com.example.graduationproject.presentation.util.getSerializableCompat
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import com.example.graduationproject.presentation.util.putArguments
import com.facebook.shimmer.ShimmerFrameLayout
import kotlinx.coroutines.delay
import java.io.Serializable

@AndroidEntryPoint
class AddFriendsView : Fragment() {

    private val viewModel: AddFriendsViewModel by viewModels()
    private var _binding: FragmentAddFriendsBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: AddFriendsAdapter
    private lateinit var actionBack: LinearLayout
    private lateinit var confirmAction: Button
    private lateinit var friendsCheckView: RecyclerView
    private lateinit var sharedPreferences: SharedPreferences
    private val args: AddFriendsViewArgs by navArgs()
    private lateinit var shimmerLayout: ShimmerFrameLayout
    private lateinit var errorView: CardView
    private lateinit var updateError: LinearLayout

    companion object {
        const val SELECTED_FRIENDS_REQUEST_KEY = "SELECTED_FRIENDS_REQUEST_KEY"
        const val SELECTED_FRIENDS_LIST_KEY = "SELECTED_FRIENDS_LIST_KEY"
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        super.onCreate(savedInstanceState)
        _binding = FragmentAddFriendsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        initAdapter()
        getSelectedFriends()
        observeSelectedFriends()
        observeFriends()
        setUpConfirmAction()
        onPressedBackAction()
        setUpErrorUpdateAction()
    }

    private fun initViews() {
        actionBack = binding.actionPressedBackContainer
        confirmAction = binding.friendsContainer.confirmAction
        friendsCheckView = binding.friendsContainer.friendsRecyclerView
        shimmerLayout = binding.friendsContainer.challengeShimmerLayout
        errorView = binding.friendsContainer.errorView.errorRootContainer
        updateError = binding.friendsContainer.errorView.updateContainer
    }

    private fun initAdapter() {
        friendsCheckView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        adapter = AddFriendsAdapter(mutableListOf())
        friendsCheckView.adapter = adapter
    }

    private fun getSelectedFriends() {
        parentFragmentManager.setFragmentResultListener(
            CreateGroupView.ADDED_FRIENDS_TO_GROUP_REQUEST_KEY, this
        ) { _, bundle ->
            val friendsArrayList = bundle.getSerializableCompat(
                CreateGroupView.ADDED_FRIENDS_TO_GROUP_LIST_KEY, ArrayList::class.java
            ) as? ArrayList<UserProfile>
            friendsArrayList?.let {
                viewModel.addFriends(it)
            }
            setUpFriends()
        }

        parentFragmentManager.setFragmentResultListener(
            CreateChallengeView.ADDED_FRIENDS_TO_CHALLENGE_REQUEST_KEY, this
        ) { _, bundle ->
            val friendsArrayList = bundle.getSerializableCompat(
                CreateChallengeView.ADDED_FRIENDS_TO_CHALLENGE_LIST_KEY, ArrayList::class.java
            ) as? ArrayList<UserProfile>
            friendsArrayList?.let {
                viewModel.addFriends(it)
            }
            setUpGroupFriends()
        }
    }

    private fun observeSelectedFriends() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.selectedFriends.collect { friends ->
                    adapter.setFriends(friends)
                }
            }
        }
    }

    private fun setUpFriends() {
        val userId = getUserId()
        viewModel.setUpFriends(userId)
    }

    private fun getUserId(): String {
        sharedPreferences =
            requireContext().getSharedPreferences("session_prefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("current_user_id", "  ") ?: "  "
    }

    private fun setUpGroupFriends() {
        args.groupId?.let { groupId ->
            viewModel.setUpGroupFriends(groupId)
        } ?: run {
            Log.e("CreateGroupView", "Group ID is null. Cannot set up group friends.")
        }
    }

    private fun observeFriends() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.viewState.collect {
                    when (it) {
                        is AddFriendsViewState.Success -> {
                            loadFriends(it.data)
                        }

                        is AddFriendsViewState.Loading -> {
                            startShimmer()
                        }

                        is AddFriendsViewState.Failure -> {
                            handleOnFailure()
                        }
                    }
                }
            }
        }
    }

    private fun loadFriends(friends: MutableList<UserProfile>) {
        val filteredFriends = friends.filter { friend ->
            !viewModel.selectedFriends.value.contains(friend)
        }.toMutableList()
        adapter.setFriends(filteredFriends)
        stopShimmer()
    }

    private fun startShimmer() {
        shimmerLayout.startShimmer()
        shimmerLayout.visibility = View.VISIBLE
        friendsCheckView.visibility = View.GONE
    }

    private fun stopShimmer() {
        shimmerLayout.stopShimmer()
        shimmerLayout.visibility = View.GONE
        friendsCheckView.visibility = View.VISIBLE
    }

    private fun handleOnFailure() {
        stopShimmer()
        errorView.visibility = View.VISIBLE
    }

    private fun setUpConfirmAction() {
        confirmAction.setOnClickListener {
            setArgumentsToCreateGroup()
            findNavController().popBackStack()
        }
    }

    private fun setArgumentsToCreateGroup() {
        val selectedFriends = adapter.getSelectedFriends()
        val arrayList = ArrayList(selectedFriends)
        this.putArguments(SELECTED_FRIENDS_LIST_KEY to arrayList as Serializable)
        setFragmentResult(SELECTED_FRIENDS_REQUEST_KEY, arguments ?: Bundle())
    }

    private fun onPressedBackAction() {
        actionBack.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun setUpErrorUpdateAction() {
        updateError.setOnClickListener {
            errorView.visibility = View.GONE
            setUpFriends()
        }
    }
}
