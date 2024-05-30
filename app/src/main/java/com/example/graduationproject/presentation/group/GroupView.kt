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
        Log.d("GroupFragment", "initAdapter: Setting up layout manager")
        groupsView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        Log.d("GroupFragment", "initAdapter: Creating adapter")
        adapter = GroupAdapter(mutableListOf()) { groupParticipants ->
            Log.d("GroupFragment", "initAdapter: Adapter item clicked, moving to group details screen")
            moveToGroupDetailsScreen(groupParticipants)
        }
        Log.d("GroupFragment", "initAdapter: Setting adapter to RecyclerView")
        groupsView.adapter = adapter
    }

    private fun moveToGroupDetailsScreen(groupParticipants: GroupParticipants) {
        val action =GroupViewDirections.actionGroupViewToGroupDetailsView(groupParticipants.group.groupId)
        findNavController().navigate(action)
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
        setUpSearchView()
    }

    private fun setUpSearchView() {
        Log.d("GroupFragment", "setUpSearchView: Setting up search view listener")
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                Log.d("GroupFragment", "onQueryTextSubmit: Query submitted - $query")
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                Log.d("GroupFragment", "onQueryTextChange: Query text changed - $newText")
                adapter.updateGroupParticipants()
                adapter.filterGroupParticipants(newText)
                return true
            }
        })
    }
}