package com.example.graduationproject.presentation.home

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.graduationproject.databinding.FragmentHomeBinding
import com.example.graduationproject.domain.entity.Achievement
import com.example.graduationproject.domain.entity.AchievementType
import com.example.graduationproject.domain.entity.Challenge
import com.example.graduationproject.domain.entity.ChallengeType
import com.example.graduationproject.domain.entity.Group
import com.example.graduationproject.domain.entity.GroupAndChallenges
import com.example.graduationproject.presentation.home.adapter.ChallengesAdapter
import com.facebook.shimmer.ShimmerFrameLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@AndroidEntryPoint
class HomeViewFragment : Fragment() {

    private val viewModel: HomeViewModel by viewModels()
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: ChallengesAdapter
    private lateinit var challengeView: RecyclerView
    private lateinit var currentChallengeView: TextView
    private lateinit var showAllChallengesView: TextView
    private lateinit var userName: TextView
    private lateinit var userId: String
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var weekChallengeView: WeekView
    private lateinit var dailyAchievementView: DailyView
    private lateinit var notificationView: ImageView
    private lateinit var notificationCountView: TextView
    private lateinit var shimmerLayout: ShimmerFrameLayout
    private lateinit var dailyShimmerLayout: ShimmerFrameLayout
    private lateinit var weekShimmerLayout: ShimmerFrameLayout
    private lateinit var weekChallengeContainer: CardView
    private lateinit var dailyAchievementContainer: CardView
    private lateinit var errorDailyView: CardView
    private lateinit var errorWeekView: CardView
    private lateinit var updateErrorDaily: LinearLayout
    private lateinit var updateErrorWeek: LinearLayout

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
        setUpNotification()
        observeChallenges()
        observeWeekChallenge()
        observeDailyAchievement()
        observeNotification()
        moveToAllChallengesScreen()
        moveToNotificationScreen()
        setUpErrorUpdateAction()
    }

    private fun initViews() {
        challengeView = binding.challengeRecyclerView
        currentChallengeView = binding.currentChallengesView
        showAllChallengesView = binding.showAllChallenges
        userName = binding.userNameView
        notificationView = binding.notificationView
        notificationCountView = binding.notificationCountView
        weekChallengeView = WeekView(binding.weekChallengeContainer)
        dailyAchievementView = DailyView(binding.dailyAchievementContainer)
        shimmerLayout = binding.shimmerLayout
        dailyShimmerLayout = binding.dailyShimmerLayout
        weekShimmerLayout = binding.weekShimmerLayout
        weekChallengeContainer = binding.weekChallengeContainer.weeklyRootContainer
        dailyAchievementContainer = binding.dailyAchievementContainer.rootContainer
        errorDailyView = binding.errorDailyView.errorRootContainer
        errorWeekView = binding.errorWeekView.errorRootContainer
        updateErrorDaily = binding.errorDailyView.updateContainer
        updateErrorWeek = binding.errorWeekView.updateContainer
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
        val action =
            HomeViewFragmentDirections.actionHomeViewFragmentToChallengeDetailsView(challenge.challengeId)
        findNavController().navigate(action)
    }

    private fun setUserName() {
        sharedPreferences =
            requireContext().getSharedPreferences("session_prefs", Context.MODE_PRIVATE)
        val usernamePref = sharedPreferences.getString("current_username", null)
        userName.text = usernamePref
    }

    private fun setUpChallenges() {
        userId = sharedPreferences.getString("current_user_id", "  ") ?: "  "
        viewModel.setUpUserChallenges(userId)
    }

    private fun setUpWeekChallenge() {
        viewModel.setUpWeekChallenge(ChallengeType.WEEK)
    }

    private fun setUpDailyAchievement() {
        viewModel.setUpDailyAchievement(AchievementType.DAILY)
    }

    private fun setUpNotification() {
        viewModel.setNotifications(userId)
    }

    private fun observeChallenges() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.viewStateChallenges.collect {
                    when (it) {
                        is HomeViewState.Success -> {
                            val groupAndChallengesPairs = transformToGroupAndChallengesPair(it.data)
                            handleOnSuccess(groupAndChallengesPairs)
                        }

                        is HomeViewState.Loading -> {
                            startShimmer(shimmerLayout, challengeView)
                        }

                        is HomeViewState.Failure -> {
                            handleOnFailure()
                        }
                    }
                }
            }
        }
    }

    private fun startShimmer(shimmerLayout: ShimmerFrameLayout, view: View) {
        shimmerLayout.startShimmer()
        shimmerLayout.visibility = View.VISIBLE
        view.visibility = View.GONE
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
        currentChallengeView.visibility = View.VISIBLE
        showAllChallengesView.visibility = View.VISIBLE
        adapter.setGroupAndChallenges(groupAndChallenges)
        stopShimmer(shimmerLayout, challengeView)
    }

    private fun stopShimmer(shimmerLayout: ShimmerFrameLayout, view: View) {
        shimmerLayout.stopShimmer()
        shimmerLayout.visibility = View.GONE
        view.visibility = View.VISIBLE
    }

    private fun handleOnFailure() {
        currentChallengeView.visibility = View.GONE
        showAllChallengesView.visibility = View.GONE
        challengeView.visibility = View.GONE
        stopShimmer(shimmerLayout, challengeView)
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
                            startShimmer(weekShimmerLayout, weekChallengeContainer)
                        }

                        is HomeViewState.Failure -> {
                            handleOnWeekFailure()
                        }
                    }
                }
            }
        }
    }

    private fun showWeekChallenge(challenge: Challenge) {
        weekChallengeView.updateWeeklyChallenge(challenge)
        stopShimmer(weekShimmerLayout, weekChallengeContainer)
    }

    private fun handleOnWeekFailure() {
        stopShimmer(weekShimmerLayout, weekChallengeContainer)
        errorWeekView.visibility = View.VISIBLE
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
                            startShimmer(dailyShimmerLayout, dailyAchievementContainer)
                        }

                        is HomeViewState.Failure -> {
                            handleOnDailyFailure()
                        }
                    }
                }
            }
        }
    }

    private fun showDailyAchievement(achievement: Achievement) {
        dailyAchievementView.updateDailyAchievement(achievement)
        stopShimmer(dailyShimmerLayout, dailyAchievementContainer)
    }

    private fun handleOnDailyFailure() {
        stopShimmer(dailyShimmerLayout, dailyAchievementContainer)
        errorDailyView.visibility = View.VISIBLE
    }

    private fun observeNotification() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.viewStateNotification.collect {
                    when (it) {
                        is HomeViewState.Success -> {
                            showNotificationCount(it.data.size)
                        }

                        is HomeViewState.Loading -> {
                            hideNotificationCount()
                        }

                        is HomeViewState.Failure -> {
                            hideNotificationCount()
                        }
                    }
                }
            }
        }
    }

    private fun showNotificationCount(notificationCount: Int) {
        if (notificationCount != 0) {
            notificationCountView.text = notificationCount.toString()
            notificationCountView.visibility = View.VISIBLE
        } else {
            hideNotificationCount()
        }
    }

    private fun hideNotificationCount() {
        notificationCountView.visibility = View.GONE
    }

    private fun moveToAllChallengesScreen() {
        showAllChallengesView.setOnClickListener {
            val action = HomeViewFragmentDirections.actionHomeViewFragmentToAllChallengesView()
            findNavController().navigate(action)
        }
    }

    private fun moveToNotificationScreen() {
        notificationView.setOnClickListener {
            val action = HomeViewFragmentDirections.actionHomeViewFragmentToNotificationView()
            findNavController().navigate(action)
        }
    }

    private fun setUpErrorUpdateAction() {
        onClickDailyUpdateAction()
        onClickWeekUpdateAction()
    }

    private fun onClickDailyUpdateAction() {
        updateErrorDaily.setOnClickListener {
            errorDailyView.visibility = View.GONE
            setUpDailyAchievement()
        }
    }

    private fun onClickWeekUpdateAction() {
        updateErrorWeek.setOnClickListener {
            errorWeekView.visibility = View.GONE
            setUpWeekChallenge()
        }
    }
}
