package com.example.graduationproject.presentation.challenges

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.graduationproject.databinding.FragmentAllChallengesBinding
import com.example.graduationproject.domain.entity.Challenge
import com.example.graduationproject.domain.entity.Group
import com.example.graduationproject.domain.entity.GroupAndChallenges
import com.example.graduationproject.presentation.challenges.adapter.AllChallengesAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AllChallengesView : Fragment() {

    private val viewModel: ChallengesViewModel by viewModels()
    private var _binding: FragmentAllChallengesBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: AllChallengesAdapter
    private lateinit var challengesView: RecyclerView
    private lateinit var onBackAction: ImageButton

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
        _binding = FragmentAllChallengesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        initAdapter()
        onPressedBackAction()
        setUpChallenges()
        observeChallenges()
    }

    private fun initViews() {
        challengesView = binding.challengesRecyclerView
        onBackAction = binding.onBackAction
    }

    private fun initAdapter() {
        challengesView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        adapter = AllChallengesAdapter(mutableListOf()) { challenge ->
            moveToChallengeDetailsScreen()
        }
        challengesView.adapter = adapter
    }

    private fun moveToChallengeDetailsScreen() {
        val action = AllChallengesViewDirections.actionAllChallengesViewToChallengeDetailsView()
        findNavController().navigate(action)
    }

    private fun onPressedBackAction() {
        onBackAction.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun setUpChallenges() {
        val sharedPreferences =
            requireContext().getSharedPreferences("session_prefs", Context.MODE_PRIVATE)
        val userId = sharedPreferences.getString("current_user_id", "  ") ?: "  "
        viewModel.setUpUserChallenges(userId)
    }

    private fun observeChallenges() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.viewState.collect {
                    Log.d("observeAllChallenges", "New view state: $it")
                    when (it) {
                        is ChallengeViewState.Success -> {
                            val groupAndChallengesPairs = transformToGroupAndChallengesPair(it.data)
                            handleOnSuccess(groupAndChallengesPairs)
                            Log.d("observeAllChallenges", "Success view state, data: ${it.data}")
                        }

                        is ChallengeViewState.Loading -> {
                        }

                        is ChallengeViewState.Failure -> {
                        }
                    }
                }
            }
        }
    }

    private fun transformToGroupAndChallengesPair(groupAndChallengesList: List<GroupAndChallenges>): MutableList<Pair<Group, Challenge>> {
        val groupAndChallengesPairs = mutableListOf<Pair<Group, Challenge>>()
        groupAndChallengesList.flatMapTo(groupAndChallengesPairs) { groupAndChallenges ->
            groupAndChallenges.challenges.map {
                Pair(groupAndChallenges.group, it)
            }
        }
        return groupAndChallengesPairs.sortedBy { it.second.endTime }.toMutableList()
    }

    private fun handleOnSuccess(groupAndChallenges: MutableList<Pair<Group, Challenge>>) {
        adapter.setGroupAndChallenges(groupAndChallenges)
    }
}