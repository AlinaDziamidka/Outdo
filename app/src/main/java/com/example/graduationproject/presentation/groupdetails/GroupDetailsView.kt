package com.example.graduationproject.presentation.groupdetails

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
import com.example.graduationproject.databinding.FragmentGroupDetailsBinding
import com.example.graduationproject.domain.entity.Challenge
import com.example.graduationproject.domain.entity.Group
import com.example.graduationproject.presentation.groupdetails.adapter.ChallengesAdapter
import com.example.graduationproject.presentation.groupdetails.adapter.ChallengesHistoryAdapter
import com.facebook.shimmer.ShimmerFrameLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class GroupDetailsView : Fragment() {

    private val viewModel: GroupDetailsViewModel by viewModels()
    private var _binding: FragmentGroupDetailsBinding? = null
    private val binding get() = _binding!!
    private val args: GroupDetailsViewArgs by navArgs()
    private lateinit var challengesAdapter: ChallengesAdapter
    private lateinit var challengesHistoryAdapter: ChallengesHistoryAdapter
    private lateinit var challengesView: RecyclerView
    private lateinit var groupNameView: TextView
    private lateinit var createChallengeAction: Button
    private lateinit var switchParticipantsAction: TextView
    private lateinit var leaveGroupAction: Button
    private lateinit var challengesHistoryView: RecyclerView
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var groupId: String
    private lateinit var group: Group
    private lateinit var challengeShimmerLayout: ShimmerFrameLayout
    private lateinit var challengeHistoryShimmerLayout: ShimmerFrameLayout
    private lateinit var groupNameShimmerLayout: ShimmerFrameLayout
    private lateinit var challengesErrorView: CardView
    private lateinit var updateChallengesError: LinearLayout
    private lateinit var challengesHistoryErrorView: CardView
    private lateinit var updateChallengesHistoryError: LinearLayout

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
        _binding = FragmentGroupDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        setUpGroupName()
        observeCurrentGroup()
        setUpChallenges()
        observeChallenges()
        setUpChallengesHistory()
        observeChallengesHistory()
        setUpSwitchParticipantsAction()
        setUpLeaveGroupAction()
        setUpCreateChallengeAction()
        setUpErrorUpdateAction()
    }

    private fun initViews() {
        groupNameView = binding.groupNameView
        createChallengeAction = binding.createChallengeAction
        challengesView = binding.challengesRecyclerView
        switchParticipantsAction = binding.selectParticipantsAction
        challengesHistoryView = binding.challengeHistoryRecyclerView
        leaveGroupAction = binding.leaveGroupAction
        challengeShimmerLayout = binding.challengeShimmerLayout
        challengeHistoryShimmerLayout = binding.challengeHistoryShimmerLayout
        groupNameShimmerLayout = binding.groupNameShimmerLayout
        challengesErrorView = binding.challengesErrorView.errorRootContainer
        updateChallengesError = binding.challengesErrorView.updateContainer
        challengesHistoryErrorView = binding.challengesHistoryErrorView.errorRootContainer
        updateChallengesHistoryError = binding.challengesHistoryErrorView.updateContainer
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
                        is GroupDetailsViewState.Success -> {
                            group = it.data
                            setGroupName(it.data)
                            initAdapter(it.data)
                        }

                        is GroupDetailsViewState.Loading -> {
                            startShimmer(groupNameShimmerLayout, groupNameView)
                        }

                        is GroupDetailsViewState.Failure -> {
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

    private fun initAdapter(group: Group) {
        initChallengesAdapter(group)
        initChallengeHistoryAdapter(group)
    }

    private fun initChallengesAdapter(group: Group) {
        challengesView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        challengesAdapter = ChallengesAdapter(mutableListOf(), group) { challenge ->
            moveToChallengeDetailsScreen(challenge)
        }
        challengesView.adapter = challengesAdapter
    }

    private fun initChallengeHistoryAdapter(group: Group) {
        challengesHistoryView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        challengesHistoryAdapter = ChallengesHistoryAdapter(mutableListOf(), group) { challenge ->
            moveToChallengeDetailsScreen(challenge)
        }
        challengesHistoryView.adapter = challengesHistoryAdapter
    }

    private fun moveToChallengeDetailsScreen(challenge: Challenge) {
        val action =
            GroupDetailsViewDirections.actionGroupDetailsViewToChallengeDetailsView(challenge.challengeId)
        findNavController().navigate(action)
    }

    private fun setUpChallenges() {
        viewModel.setUpChallenges(groupId)
    }

    private fun observeChallenges() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.viewStateChallenges.collect {
                    when (it) {
                        is GroupDetailsViewState.Success -> {
                            loadChallenges(it.data)
                        }

                        is GroupDetailsViewState.Loading -> {
                            startShimmer(challengeShimmerLayout, challengesView)
                        }

                        is GroupDetailsViewState.Failure -> {
                            handleOnChallengesFailure()
                        }
                    }
                }
            }
        }
    }

    private fun loadChallenges(challenges: MutableList<Challenge>) {
        val sortedChallenges = challenges.sortedBy { it.endTime }.toMutableList()
        challengesAdapter.setChallenges(sortedChallenges, group)
        stopShimmer(challengeShimmerLayout, challengesView)
    }

    private fun handleOnChallengesFailure() {
        stopShimmer(challengeShimmerLayout, challengesView)
        challengesErrorView.visibility = View.VISIBLE
    }

    private fun setUpChallengesHistory() {
        viewModel.setUpChallengesHistory(groupId)
    }

    private fun observeChallengesHistory() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.viewStateChallengesHistory.collect {
                    when (it) {
                        is GroupDetailsViewState.Success -> {
                            loadChallengesHistory(it.data)
                        }

                        is GroupDetailsViewState.Loading -> {
                            startShimmer(challengeHistoryShimmerLayout, challengesHistoryView)
                        }

                        is GroupDetailsViewState.Failure -> {
                            handleOnChallengesHistoryFailure()
                        }
                    }
                }
            }
        }
    }

    private fun loadChallengesHistory(challenges: MutableList<Challenge>) {
        val sortedChallenges = challenges.sortedBy { it.endTime }.toMutableList()
        challengesHistoryAdapter.setChallenges(sortedChallenges, group)
        stopShimmer(challengeHistoryShimmerLayout, challengesHistoryView)
    }

    private fun handleOnChallengesHistoryFailure() {
        stopShimmer(challengeHistoryShimmerLayout, challengesHistoryView)
        challengesHistoryErrorView.visibility = View.VISIBLE
    }

    private fun setUpSwitchParticipantsAction() {
        switchParticipantsAction.setOnClickListener {
            val action =
                GroupDetailsViewDirections.actionGroupDetailsViewToGroupParticipantsView(groupId)
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

    private fun setUpCreateChallengeAction() {
        createChallengeAction.setOnClickListener {
            moveToCreateChallengeScreen()
        }
    }

    private fun moveToCreateChallengeScreen() {
        val action = GroupDetailsViewDirections.actionGroupDetailsViewToCreateChallengeView(groupId)
        findNavController().navigate(action)
    }

    private fun setUpErrorUpdateAction() {
        onClickChallengesUpdateAction()
        onClickChallengesHistoryUpdateAction()
    }

    private fun onClickChallengesUpdateAction() {
        updateChallengesError.setOnClickListener {
            challengesErrorView.visibility = View.GONE
            setUpChallenges()
        }
    }

    private fun onClickChallengesHistoryUpdateAction() {
        updateChallengesHistoryError.setOnClickListener {
            challengesHistoryErrorView.visibility = View.GONE
            setUpChallengesHistory()
        }
    }
}