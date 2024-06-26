package com.example.graduationproject.presentation.home

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.graduationproject.App
import com.example.graduationproject.databinding.FragmentHomeBinding
import com.example.graduationproject.domain.entity.Achievement
import com.example.graduationproject.domain.entity.AchievementType
import com.example.graduationproject.domain.entity.Challenge
import com.example.graduationproject.domain.entity.ChallengeType
import com.example.graduationproject.domain.entity.Group
import com.example.graduationproject.domain.entity.GroupAndChallenges
import com.example.graduationproject.presentation.home.adapter.ChallengesAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class HomeViewFragment : Fragment() {

    private val viewModel: HomeViewModel by viewModels()
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: ChallengesAdapter
    private lateinit var challengeView: RecyclerView
    private lateinit var progressView: ProgressBar
    private lateinit var currentChallengeView: TextView
    private lateinit var showAllChallengesView: TextView
    private lateinit var userName: TextView
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var weekChallengeView: WeekView
    private lateinit var dailyAchievementView: DailyView

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
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        initAdapter()
        setUserName()
        setUpChallenges()
        setUpWeekChallenge()
        setUpDailyAchievement()
        observeChallenges()
        observeWeekChallenge()
        observeDailyAchievement()
        moveToAllChallengesScreen()
    }

    private fun initAdapter() {
        challengeView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        adapter = ChallengesAdapter(mutableListOf()) { challenge ->
            moveToChallengeDetailsScreen(challenge)
        }
        challengeView.adapter = adapter
    }

    private fun moveToChallengeDetailsScreen(challenge: Challenge) {
        val action = HomeViewFragmentDirections.actionHomeViewFragmentToChallengeDetailsView(challenge.challengeId)
        findNavController().navigate(action)
    }

    private fun initViews() {
        progressView = binding.progressView
        challengeView = binding.challengeRecyclerView
        currentChallengeView = binding.currentChallengesView
        showAllChallengesView = binding.showAllChallenges
        userName = binding.userNameView
        weekChallengeView = WeekView(binding.weekChallengeContainer)
        dailyAchievementView = DailyView(binding.dailyAchievementContainer)
    }

    private fun setUserName() {
        Log.d("HomeViewFragment", "Setting username")
        sharedPreferences =
            requireContext().getSharedPreferences("session_prefs", Context.MODE_PRIVATE)
        val usernamePref = sharedPreferences.getString("current_username", null)
        userName.text = usernamePref
    }

    private fun setUpChallenges() {
        val userId = sharedPreferences.getString("current_user_id", "  ") ?: "  "
        viewModel.setUpUserChallenges(userId)
    }

    private fun setUpWeekChallenge() {
        viewModel.setUpWeekChallenge(ChallengeType.WEEK)
    }

    private fun setUpDailyAchievement() {
        viewModel.setUpDailyAchievement(AchievementType.DAILY)
    }

    private fun observeChallenges() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.viewStateChallenges.collect {
                    Log.d("observeChallenges", "New view state: $it")
                    when (it) {
                        is HomeViewState.Success -> {
                            val groupAndChallengesPairs = transformToGroupAndChallengesPair(it.data)
                            handleOnSuccess(groupAndChallengesPairs)
                            Log.d("observeChallenges", "Success view state, data: ${it.data}")
                        }

                        is HomeViewState.Loading -> {
                            Log.d("observeChallenges", "Loading view state")
                            progressView.visibility = View.VISIBLE
                            challengeView.visibility = View.GONE
                        }

                        is HomeViewState.Failure -> {
                            Log.d("observeChallenges", "Failure view state")
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
        progressView.visibility = View.GONE
        currentChallengeView.visibility = View.VISIBLE
        showAllChallengesView.visibility = View.VISIBLE
        adapter.setGroupAndChallenges(groupAndChallenges)
        challengeView.visibility = View.VISIBLE
    }

    private fun handleOnFailure() {
        progressView.visibility = View.GONE
        currentChallengeView.visibility = View.GONE
        showAllChallengesView.visibility = View.GONE
        progressView.visibility = View.VISIBLE
        challengeView.visibility = View.GONE
    }

    private fun observeWeekChallenge() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.viewStateWeek.collect {
                    when (it) {
                        is HomeViewState.Success -> {
                            showWeekChallenge(it.data)
                        }

                        is HomeViewState.Loading -> {

                        }

                        is HomeViewState.Failure -> {

                        }
                    }
                }
            }
        }
    }

    private fun showWeekChallenge(challenge: Challenge) {
        weekChallengeView.updateWeeklyChallenge(challenge)
    }

    private fun observeDailyAchievement() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.viewStateDaily.collect {
                    when (it) {
                        is HomeViewState.Success -> {
                            showDailyAchievement(it.data)
                        }

                        is HomeViewState.Loading -> {

                        }

                        is HomeViewState.Failure -> {

                        }
                    }
                }
            }
        }
    }

    private fun showDailyAchievement(achievement: Achievement) {
        dailyAchievementView.updateDailyAchievement(achievement)
    }

    private fun moveToAllChallengesScreen() {
        showAllChallengesView.setOnClickListener {
            val action = HomeViewFragmentDirections.actionHomeViewFragmentToAllChallengesView()
            findNavController().navigate(action)
        }
    }

    override fun onResume() {
        super.onResume()
        }
    }
