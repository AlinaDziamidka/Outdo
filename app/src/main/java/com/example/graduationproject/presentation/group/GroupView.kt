package com.example.graduationproject.presentation.group

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.graduationproject.databinding.FragmentGroupsBinding
import com.example.graduationproject.domain.entity.GroupParticipants
import com.example.graduationproject.presentation.group.adapter.GroupAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class GroupView : Fragment() {

    private val viewModel: GroupsViewModel by viewModels()
    private var _binding: FragmentGroupsBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: GroupAdapter
    private lateinit var groupsView: RecyclerView
    private lateinit var searchView: SearchView
    private lateinit var createGroupAction: Button

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
        _binding = FragmentGroupsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        initAdapter()
        setUpGroups()
        observeGroups()
    }

    private fun initViews() {
        groupsView = binding.groupsRecyclerView
        searchView = binding.searchView
        createGroupAction = binding.createGroupAction
    }

    private fun initAdapter() {
        groupsView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        adapter = GroupAdapter(mutableListOf()) { group ->
            moveToGroupDetailsScreen()
        }
        groupsView.adapter = adapter
    }

    private fun moveToGroupDetailsScreen() {
//        val action = AllChallengesViewDirections.actionAllChallengesViewToChallengeDetailsView()
//        findNavController().navigate(action)
    }

    private fun setUpGroups() {
        val sharedPreferences =
            requireContext().getSharedPreferences("session_prefs", Context.MODE_PRIVATE)
        val userId = sharedPreferences.getString("current_user_id", "  ") ?: "  "
        viewModel.setUpUserGroups(userId)
    }

    private fun observeGroups() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.viewState.collect {
                    Log.d("observeGroups", "New view state: $it")
                    when (it) {
                        is GroupViewState.Success -> {
                            handleOnSuccess(it.data)
                            Log.d("observeGroups", "Success view state, data: ${it.data}")
                        }

                        is GroupViewState.Loading -> {
                        }

                        is GroupViewState.Failure -> {
                        }
                    }
                }
            }
        }
    }

    private fun handleOnSuccess(groupParticipants: MutableList<GroupParticipants>) {
        adapter.setGroupParticipants(groupParticipants)
    }
}