package com.example.graduationproject.presentation.challengedetails

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.example.graduationproject.R
import com.example.graduationproject.databinding.FragmentChallengeDetailsBinding
import com.example.graduationproject.domain.entity.Achievement
import com.example.graduationproject.domain.entity.Challenge
import com.example.graduationproject.domain.entity.UserProfile
import com.example.graduationproject.presentation.challengedetails.adapter.AchievementsAdapter
import dagger.hilt.android.AndroidEntryPoint
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
    private lateinit var challengeDescription: ChallengeDescriptionView


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
    }

    private fun initViews() {
        challengeNameView = binding.challengeNameView
        challengeDescription = ChallengeDescriptionView(binding.challengeDetailsContainer)
        achievementView = binding.achievementRecyclerView
    }

    private fun setUpChallengeName() {
        challengeId = args.chalengeId
        viewModel.setCurrentChallenge(challengeId)
    }

    private fun observeCurrentChallenge() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.viewStateCurrentChallenge.collect {
                    Log.d("observeCurrentChallenge", "New view state: $it")
                    when (it) {
                        is ChallengeDetailsViewState.Success -> {
                            challenge = it.data.first
                            setChallengeName(it.data.first)
                            setChallengeDescription(it.data)
                            Log.d("observeCurrentChallenge", "Success view state, data: ${it.data}")
                        }

                        is ChallengeDetailsViewState.Loading -> {
                        }

                        is ChallengeDetailsViewState.Failure -> {
                        }
                    }
                }
            }
        }
    }

    private fun setChallengeName(challenge: Challenge) {
        val challengeTitle =
            getString(R.string.challenge_details_screen_challenge_title, challenge.challengeName)
        challengeNameView.text = challengeTitle
    }

    private fun setChallengeDescription(challengeDetails: Pair<Challenge, UserProfile?>) {
        challengeDescription.updateChallengeDescription(
            challengeDetails.first,
            challengeDetails.second
        )
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
                    Log.d("observeAchievements", "New view state: $it")
                    when (it) {
                        is ChallengeDetailsViewState.Success -> {
                            loadAchievements(it.data)
                            Log.d("observeAchievements", "Success view state, data: ${it.data}")
                        }

                        is ChallengeDetailsViewState.Loading -> {
                            Log.d("observeAchievements", "Loading view state")
                        }

                        is ChallengeDetailsViewState.Failure -> {
                            Log.d("observeAchievements", "Failure view state")
                        }
                    }
                }
            }
        }
    }

    private fun loadAchievements(achievements: MutableList<Achievement>) {
        val sortedAchievements = achievements.sortedBy { it.achievementName }.toMutableList()
        achievementsAdapter.setAchievements(sortedAchievements)
    }
}