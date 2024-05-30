package com.example.graduationproject.presentation.groupdetails

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
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
import com.example.graduationproject.domain.entity.GroupParticipants
import com.example.graduationproject.presentation.group.GroupViewDirections
import com.example.graduationproject.presentation.group.GroupViewState
import com.example.graduationproject.presentation.group.adapter.GroupAdapter
import com.example.graduationproject.presentation.groupdetails.adapter.ChallengesAdapter
import com.example.graduationproject.presentation.groupdetails.adapter.ChallengesHistoryAdapter
import com.example.graduationproject.presentation.home.HomeViewState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

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
    private lateinit var challengesHistoryView: RecyclerView
    private lateinit var groupId: String
    private lateinit var group: Group


    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUpGroupName()
        observeCurrentGroup()
    }

    private fun setUpGroupName() {
        groupId = args.groupId
        viewModel.setCurrentGroup(groupId)
    }

    private fun observeCurrentGroup() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.viewStateCurrentGroup.collect {
                    Log.d("observeCurrentGroup", "New view state: $it")
                    when (it) {
                        is GroupDetailsViewState.Success -> {
                            group = it.data
                            setGroupName(it.data)
                            initAdapter(it.data)
                            Log.d("observeCurrentGroup", "Success view state, data: ${it.data}")
                        }

                        is GroupDetailsViewState.Loading -> {
                        }

                        is GroupDetailsViewState.Failure -> {
                        }
                    }
                }
            }
        }
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
//        val action = GroupViewDirections.actionGroupViewToGroupDetailsView(groupParticipants.group.groupId)
//        findNavController().navigate(action)
    }

    private fun setGroupName(group: Group) {
        groupNameView.text = group.groupName
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreate(savedInstanceState)
        _binding = FragmentGroupDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        setUpChallenges()
        observeChallenges()
        setUpChallengesHistory()
        observeChallengesHistory()
        setUpSwitchParticipantsAction()
    }

    private fun initViews() {
        groupNameView = binding.groupNameView
        createChallengeAction = binding.createChallengeAction
        challengesView = binding.challengesRecyclerView
        switchParticipantsAction = binding.selectParticipantsAction
        challengesHistoryView = binding.challengeHistoryRecyclerView
    }

    private fun setUpChallenges() {
        viewModel.setUpChallenges(groupId)
    }

    private fun observeChallenges() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.viewStateChallenges.collect {
                    Log.d("observeChallenges", "New view state: $it")
                    when (it) {
                        is GroupDetailsViewState.Success -> {
                            loadChallenges(it.data)
                            Log.d("observeChallenges", "Success view state, data: ${it.data}")
                        }

                        is GroupDetailsViewState.Loading -> {
                            Log.d("observeChallenges", "Loading view state")
                        }

                        is GroupDetailsViewState.Failure -> {
                            Log.d("observeChallenges", "Failure view state")
                        }
                    }
                }
            }
        }
    }

    private fun loadChallenges(challenges: MutableList<Challenge>) {
        val sortedChallenges = challenges.sortedBy { it.endTime }.toMutableList()
        challengesAdapter.setChallenges(sortedChallenges, group)
    }

    private fun setUpChallengesHistory() {
        viewModel.setUpChallengesHistory(groupId)
    }

    private fun observeChallengesHistory() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.viewStateChallengesHistory.collect {
                    Log.d("observeChallengesHistory", "New view state: $it")
                    when (it) {
                        is GroupDetailsViewState.Success -> {
                            loadChallengesHistory(it.data)
                            Log.d(
                                "observeChallengesHistory",
                                "Success view state, data: ${it.data}"
                            )
                        }

                        is GroupDetailsViewState.Loading -> {
                            Log.d("observeChallengesHistory", "Loading view state")
                        }

                        is GroupDetailsViewState.Failure -> {
                            Log.d("observeChallengesHistory", "Failure view state")
                        }
                    }
                }
            }
        }
    }

    private fun loadChallengesHistory(challenges: MutableList<Challenge>) {
        val sortedChallenges = challenges.sortedBy { it.endTime }.toMutableList()
        challengesHistoryAdapter.setChallenges(sortedChallenges, group)
    }

    private fun setUpSwitchParticipantsAction() {
        switchParticipantsAction.setOnClickListener {
            val action =
                GroupDetailsViewDirections.actionGroupDetailsViewToGroupParticipantsView(groupId)
            findNavController().navigate(action)
        }
    }
}