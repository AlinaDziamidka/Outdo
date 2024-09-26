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
import com.facebook.shimmer.ShimmerFrameLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
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
    private lateinit var shimmerLayout: ShimmerFrameLayout

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
        setUpCreateGroupAction()
    }

    private fun initViews() {
        groupsView = binding.groupsRecyclerView
        searchView = binding.searchView
        createGroupAction = binding.createGroupAction
        shimmerLayout = binding.shimmerLayout
    }

    private fun initAdapter() {
        groupsView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        adapter = GroupAdapter(mutableListOf()) { groupParticipants ->
            moveToGroupDetailsScreen(groupParticipants)
        }
        groupsView.adapter = adapter
    }

    private fun moveToGroupDetailsScreen(groupParticipants: GroupParticipants) {
        val action =
            GroupViewDirections.actionGroupViewToGroupDetailsView(groupParticipants.group.groupId)
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
                    when (it) {
                        is GroupViewState.Success -> {
                            handleOnSuccess(it.data)
                        }

                        is GroupViewState.Loading -> {
                            startShimmer()
                        }

                        is GroupViewState.Failure -> {
                            stopShimmer()
                        }
                    }
                }
            }
        }
    }

    private fun handleOnSuccess(groupParticipants: MutableList<GroupParticipants>) {
        adapter.setGroupParticipants(groupParticipants)
        setUpSearchView()
        stopShimmer()
    }

    private fun setUpSearchView() {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.updateGroupParticipants()
                adapter.filterGroupParticipants(newText)
                return true
            }
        })
    }

    private fun startShimmer() {
        shimmerLayout.startShimmer()
        shimmerLayout.visibility = View.VISIBLE
        groupsView.visibility = View.GONE
    }

    private fun stopShimmer() {
        shimmerLayout.stopShimmer()
        shimmerLayout.visibility = View.GONE
        groupsView.visibility = View.VISIBLE
    }


    private fun setUpCreateGroupAction() {
        createGroupAction.setOnClickListener {
            val action =
                GroupViewDirections.actionGroupViewToCreateGroupView()
            findNavController().navigate(action)
        }
    }
}