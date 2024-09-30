package com.example.graduationproject.presentation.challengedetails

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.example.graduationproject.R
import com.example.graduationproject.databinding.FragmentChallengeDetailsBinding
import com.example.graduationproject.domain.entity.Achievement
import com.example.graduationproject.domain.entity.Challenge
import com.example.graduationproject.domain.entity.UserProfile
import com.example.graduationproject.presentation.challengedetails.adapter.AchievementsAdapter
import com.facebook.shimmer.ShimmerFrameLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ChallengeDetailsView : Fragment() {

    private val viewModel: ChallengeDetailsViewModel by viewModels()
    private var _binding: FragmentChallengeDetailsBinding? = null
    private val binding get() = _binding!!
    private val args: ChallengeDetailsViewArgs by navArgs()
    private lateinit var achievementsAdapter: AchievementsAdapter
    private lateinit var achievementView: RecyclerView
    private lateinit var challengeNameView: TextView
    private lateinit var challengeId: String
    private lateinit var challenge: Challenge
    private lateinit var challengeDescriptionView: ChallengeDescriptionView
    private lateinit var descriptionShimmerLayout: ShimmerFrameLayout
    private lateinit var achievementShimmerLayout: ShimmerFrameLayout
    private lateinit var descriptionErrorView: CardView
    private lateinit var updateDescriptionError: LinearLayout
    private lateinit var challengeDescriptionContainer: CardView

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
        _binding = FragmentChallengeDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        setUpChallengeName()
        observeCurrentChallenge()
        initAdapter()
        setUpAchievements()
        observeAchievements()
        setUpErrorUpdateAction()
    }

    private fun initViews() {
        challengeNameView = binding.challengeNameView
        challengeDescriptionView = ChallengeDescriptionView(binding.challengeDetailsContainerView)
        achievementView = binding.achievementRecyclerView
        descriptionShimmerLayout = binding.descriptionShimmerLayout
        achievementShimmerLayout = binding.achievementShimmerLayout
        descriptionErrorView = binding.errorView.errorRootContainer
        updateDescriptionError = binding.errorView.updateContainer
        challengeDescriptionContainer =
            binding.challengeDetailsContainerView.challengeDetailsRootContainer
    }

    private fun setUpChallengeName() {
        challengeId = args.chalengeId
        viewModel.setCurrentChallenge(challengeId)
    }

    private fun observeCurrentChallenge() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.viewStateCurrentChallenge.collect {
                    when (it) {
                        is ChallengeDetailsViewState.Success -> {
                            challenge = it.data.first
                            handleOnSuccess(it.data)
                        }

                        is ChallengeDetailsViewState.Loading -> {
                            startShimmer(descriptionShimmerLayout, challengeDescriptionContainer)
                        }

                        is ChallengeDetailsViewState.Failure -> {
                            handleOnFailure()
                        }
                    }
                }
            }
        }
    }

    private fun handleOnSuccess(challengeDetails: Pair<Challenge, UserProfile?>) {
        setChallengeName(challengeDetails.first)
        setChallengeDescription(challengeDetails)
        stopShimmer(descriptionShimmerLayout, challengeDescriptionContainer)
    }

    private fun setChallengeName(challenge: Challenge) {
        val challengeTitle =
            getString(R.string.challenge_details_screen_challenge_title, challenge.challengeName)
        challengeNameView.text = challengeTitle
    }

    private fun setChallengeDescription(challengeDetails: Pair<Challenge, UserProfile?>) {
        challengeDescriptionView.updateChallengeDescription(
            challengeDetails.first,
            challengeDetails.second
        )
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

    private fun handleOnFailure() {
        stopShimmer(descriptionShimmerLayout, challengeDescriptionContainer)
        descriptionErrorView.visibility = View.VISIBLE
    }

    private fun initAdapter() {
        achievementView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        achievementsAdapter = AchievementsAdapter(mutableListOf()) { achievement ->
            moveToAchievementDetailsScreen(achievement)
        }
        achievementView.adapter = achievementsAdapter
    }

    private fun moveToAchievementDetailsScreen(achievement: Achievement) {
        val action =
            ChallengeDetailsViewDirections.actionChallengeDetailsViewToAchievementDetailsView(
                achievement.achievementId
            )
        findNavController().navigate(action)
    }

    private fun setUpAchievements() {
        viewModel.setUpAchievements(challengeId)
    }

    private fun observeAchievements() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.viewStateAchievements.collect {
                    when (it) {
                        is ChallengeDetailsViewState.Success -> {
                            loadAchievements(it.data)
                        }

                        is ChallengeDetailsViewState.Loading -> {
                            startShimmer(achievementShimmerLayout, achievementView)
                        }

                        is ChallengeDetailsViewState.Failure -> {
                            stopShimmer(achievementShimmerLayout, achievementView)
                        }
                    }
                }
            }
        }
    }

    private fun loadAchievements(achievements: MutableList<Achievement>) {
        val sortedAchievements = achievements.sortedBy { it.achievementName }.toMutableList()
        achievementsAdapter.setAchievements(sortedAchievements)
        stopShimmer(achievementShimmerLayout, achievementView)
    }

    private fun setUpErrorUpdateAction() {
        updateDescriptionError.setOnClickListener {
            descriptionErrorView.visibility = View.GONE
            setUpChallengeName()
        }
    }
}