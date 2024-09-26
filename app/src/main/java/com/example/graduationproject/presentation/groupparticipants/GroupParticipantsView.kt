package com.example.graduationproject.presentation.groupparticipants

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.graduationproject.databinding.FragmentGroupParticipantsBinding
import com.example.graduationproject.domain.entity.Group
import com.example.graduationproject.domain.entity.UserProfile
import com.example.graduationproject.presentation.groupparticipants.adapter.GroupParticipantsAdapter
import com.facebook.shimmer.ShimmerFrameLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class GroupParticipantsView : Fragment() {

    private val viewModel: GroupParticipantsViewModel by viewModels()
    private var _binding: FragmentGroupParticipantsBinding? = null
    private val binding get() = _binding!!
    private val args: GroupParticipantsViewArgs by navArgs()
    private lateinit var groupParticipantsAdapter: GroupParticipantsAdapter
    private lateinit var participantsView: RecyclerView
    private lateinit var groupNameView: TextView
    private lateinit var addFriendsAction: Button
    private lateinit var switchChallengesAction: TextView
    private lateinit var leaveGroupAction: Button
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var groupId: String
    private lateinit var group: Group
    private lateinit var participantsShimmerLayout: ShimmerFrameLayout
    private lateinit var groupNameShimmerLayout: ShimmerFrameLayout
    private lateinit var errorView: CardView
    private lateinit var updateError: LinearLayout

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
        _binding = FragmentGroupParticipantsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        setUpGroupName()
        observeCurrentGroup()
        setUpParticipants()
        observeParticipants()
        setUpSwitchChallengesAction()
        setUpLeaveGroupAction()
        setUpErrorUpdateAction()
    }

    private fun initViews() {
        groupNameView = binding.groupNameView
        addFriendsAction = binding.addFriendsAction
        participantsView = binding.participantsRecyclerView
        switchChallengesAction = binding.selectChallengesAction
        leaveGroupAction = binding.leaveGroupAction
        participantsShimmerLayout = binding.participantsShimmerLayout
        groupNameShimmerLayout = binding.groupNameShimmerLayout
        errorView = binding.errorView.errorRootContainer
        updateError = binding.errorView.updateContainer
    }

    private fun setUpGroupName() {
        groupId = args.groupId
        viewModel.setCurrentGroup(groupId)
    }

    private fun observeCurrentGroup() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.viewStateCurrentGroup.collect {
                    when (it) {
                        is GroupParticipantsViewState.Success -> {
                            group = it.data
                            setGroupName(it.data)
                            initAdapter()
                        }

                        is GroupParticipantsViewState.Loading -> {
                            startShimmer(groupNameShimmerLayout, groupNameView)
                        }

                        is GroupParticipantsViewState.Failure -> {
                            stopShimmer(groupNameShimmerLayout, groupNameView)
                        }
                    }
                }
            }
        }
    }

    private fun setGroupName(group: Group) {
        groupNameView.text = group.groupName
        stopShimmer(groupNameShimmerLayout, groupNameView)
    }

    private fun startShimmer(shimmerLayout: ShimmerFrameLayout, view: View) {
        shimmerLayout.startShimmer()
        shimmerLayout.visibility = View.VISIBLE
        view.visibility = View.GONE
    }

    private fun stopShimmer(shimmerLayout: ShimmerFrameLayout, view: View) {
        shimmerLayout.stopShimmer()
        shimmerLayout.visibility = View.GONE
        view.visibility = View.VISIBLE
    }

    private fun initAdapter() {
        participantsView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        groupParticipantsAdapter = GroupParticipantsAdapter(mutableListOf())
        participantsView.adapter = groupParticipantsAdapter
    }

    private fun setUpParticipants() {
        viewModel.setUpGroupParticipants(groupId)
    }

    private fun observeParticipants() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.viewStateParticipants.collect {
                    when (it) {
                        is GroupParticipantsViewState.Success -> {
                            loadParticipants(it.data)
                        }

                        is GroupParticipantsViewState.Loading -> {
                            startShimmer(participantsShimmerLayout, participantsView)
                        }

                        is GroupParticipantsViewState.Failure -> {
                            handleOnFailure()
                        }
                    }
                }
            }
        }
    }

    private fun loadParticipants(participants: MutableList<UserProfile>) {
        groupParticipantsAdapter.setParticipants(participants)
        stopShimmer(participantsShimmerLayout, participantsView)
    }

    private fun handleOnFailure() {
        stopShimmer(participantsShimmerLayout, participantsView)
        errorView.visibility = View.VISIBLE
    }

    private fun setUpSwitchChallengesAction() {
        switchChallengesAction.setOnClickListener {
            val action =
                GroupParticipantsViewDirections.actionGroupParticipantsViewToGroupDetailsView(
                    groupId
                )
            findNavController().navigate(action)
        }
    }

    private fun setUpLeaveGroupAction() {
        leaveGroupAction.setOnClickListener {
            val userId = getCurrentUserId()
            viewModel.deleteUserGroup(userId, groupId)
            requireActivity().supportFragmentManager.popBackStack()
        }
    }

    private fun getCurrentUserId(): String {
        sharedPreferences =
            requireContext().getSharedPreferences("session_prefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("current_user_id", "  ") ?: "  "
    }

    private fun setUpErrorUpdateAction() {
        updateError.setOnClickListener {
            errorView.visibility = View.GONE
            setUpParticipants()
        }
    }
}