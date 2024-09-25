package com.example.graduationproject.presentation.challenges

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.cardview.widget.CardView
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
import com.facebook.shimmer.ShimmerFrameLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AllChallengesView : Fragment() {

    private val viewModel: ChallengesViewModel by viewModels()
    private var _binding: FragmentAllChallengesBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: AllChallengesAdapter
    private lateinit var challengesView: RecyclerView
    private lateinit var onBackAction: ImageButton
    private lateinit var shimmerLayout: ShimmerFrameLayout
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
        setUpErrorUpdateAction()
    }

    private fun initViews() {
        challengesView = binding.challengesRecyclerView
        onBackAction = binding.onBackAction
        shimmerLayout = binding.shimmerLayout
        errorView = binding.errorView.errorRootContainer
        updateError = binding.errorView.updateContainer
    }

    private fun initAdapter() {
        challengesView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        adapter = AllChallengesAdapter(mutableListOf()) { challenge ->
            moveToChallengeDetailsScreen(challenge)
        }
        challengesView.adapter = adapter
    }

    private fun moveToChallengeDetailsScreen(challenge: Challenge) {
        val action =
            AllChallengesViewDirections.actionAllChallengesViewToChallengeDetailsView(challenge.challengeId)
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
                    when (it) {
                        is ChallengeViewState.Success -> {
                            val groupAndChallengesPairs = transformToGroupAndChallengesPair(it.data)
                            handleOnSuccess(groupAndChallengesPairs)
                        }

                        is ChallengeViewState.Loading -> {
                            startShimmer()
                        }

                        is ChallengeViewState.Failure -> {
                            handleOnFailure()
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
        stopShimmer()
    }

    private fun startShimmer() {
        shimmerLayout.startShimmer()
        shimmerLayout.visibility = View.VISIBLE
        challengesView.visibility = View.GONE
    }

    private fun stopShimmer() {
        shimmerLayout.stopShimmer()
        shimmerLayout.visibility = View.GONE
        challengesView.visibility = View.VISIBLE
    }

    private fun handleOnFailure() {
        stopShimmer()
        errorView.visibility = View.VISIBLE
    }

    private fun setUpErrorUpdateAction() {
        updateError.setOnClickListener {
            errorView.visibility = View.GONE
            setUpChallenges()
        }
    }
}