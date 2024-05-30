package com.example.graduationproject.presentation.groupparticipants

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
import com.example.graduationproject.databinding.FragmentGroupParticipantsBinding
import com.example.graduationproject.domain.entity.Group
import com.example.graduationproject.domain.entity.UserProfile
import com.example.graduationproject.presentation.groupdetails.GroupDetailsViewDirections
import com.example.graduationproject.presentation.groupdetails.GroupDetailsViewState
import com.example.graduationproject.presentation.groupdetails.adapter.ChallengesAdapter
import com.example.graduationproject.presentation.groupparticipants.adapter.GroupParticipantsAdapter
import dagger.hilt.android.AndroidEntryPoint
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
                    Log.d("observeCurrentGroup in GroupParticipants", "New view state: $it")
                    when (it) {
                        is GroupParticipantsViewState.Success -> {
                            group = it.data
                            setGroupName(it.data)
                            initAdapter(it.data)
                            Log.d("observeCurrentGroup in GroupParticipants", "Success view state, data: ${it.data}")
                        }

                        is GroupParticipantsViewState.Loading -> {
                        }

                        is GroupParticipantsViewState.Failure -> {
                        }
                    }
                }
            }
        }
    }

    private fun setGroupName(group: Group) {
        groupNameView.text = group.groupName
    }

    private fun initAdapter(group: Group) {
        participantsView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        groupParticipantsAdapter = GroupParticipantsAdapter(mutableListOf())
        participantsView.adapter = groupParticipantsAdapter
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
        setUpParticipants()
        observeParticipants()
        setUpSwitchChallengesAction()
    }

    private fun initViews() {
        groupNameView = binding.groupNameView
        addFriendsAction = binding.addFriendsAction
        participantsView = binding.participantsRecyclerView
        switchChallengesAction = binding.selectChallengesAction
    }

    private fun setUpParticipants() {
        viewModel.setUpGroupParticipants(groupId)
    }

    private fun observeParticipants() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.viewStateParticipants.collect {
                    Log.d("observeParticipants", "New view state: $it")
                    when (it) {
                        is GroupParticipantsViewState.Success -> {
                            loadParticipants(it.data)
                            Log.d("observeParticipants", "Success view state, data: ${it.data}")
                        }

                        is GroupParticipantsViewState.Loading -> {
                            Log.d("observeParticipants", "Loading view state")
                        }

                        is GroupParticipantsViewState.Failure -> {
                            Log.d("observeParticipants", "Failure view state")
                        }
                    }
                }
            }
        }
    }

    private fun loadParticipants(participants: MutableList<UserProfile>) {
//        val sortedChallenges = challenges.sortedBy { it.endTime }.toMutableList()
        groupParticipantsAdapter.setParticipants(participants)
    }

    private fun setUpSwitchChallengesAction() {
        switchChallengesAction.setOnClickListener {
            val action =
                GroupParticipantsViewDirections.actionGroupParticipantsViewToGroupDetailsView(groupId)
            findNavController().navigate(action)
        }
    }
}