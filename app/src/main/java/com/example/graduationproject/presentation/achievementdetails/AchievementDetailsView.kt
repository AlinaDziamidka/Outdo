package com.example.graduationproject.presentation.achievementdetails

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
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.graduationproject.R
import com.example.graduationproject.databinding.FragmentAchievementDetailsBinding
import com.example.graduationproject.domain.entity.Achievement
import com.example.graduationproject.domain.entity.UserProfile
import com.example.graduationproject.presentation.achievementdetails.adapter.CompletedAdapter
import com.example.graduationproject.presentation.achievementdetails.adapter.UncompletedAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AchievementDetailsView : Fragment() {

    private val viewModel: AchievementDetailsViewModel by viewModels()
    private var _binding: FragmentAchievementDetailsBinding? = null
    private val binding get() = _binding!!
    private val args: AchievementDetailsViewArgs by navArgs()
    private lateinit var completedAdapter: CompletedAdapter
    private lateinit var uncompletedAdapter: UncompletedAdapter
    private lateinit var completedView: RecyclerView
    private lateinit var uncompletedView: RecyclerView
    private lateinit var achievementNameView: TextView
    private lateinit var achievementDescriptionView: TextView
    private lateinit var achievementId: String
    private lateinit var completeActionView: Button
    private lateinit var currentAchievement: Achievement


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
        _binding = FragmentAchievementDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        setUpChallengeName()
        observeCurrentAchievement()
        initAdapter()
        setUpCompletedFriends()
        setUpUncompletedFriends()
        observeCompletedFriends()
        observeUncompletedFriends()
    }

    private fun initViews() {
        achievementNameView = binding.achievementNameView
        achievementDescriptionView = binding.achievementDescriptionView
        completedView = binding.completedRecyclerView
        uncompletedView = binding.uncompletedRecyclerView
        completeActionView = binding.completeAchievementAction
    }

    private fun setUpChallengeName() {
        achievementId = args.achievementId
        viewModel.setCurrentAchievement(achievementId)
    }

    private fun observeCurrentAchievement() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.viewStateCurrentAchievement.collect {
                    Log.d("observeCurrentAchievement", "New view state: $it")
                    when (it) {
                        is AchievementDetailsViewState.Success -> {
                            currentAchievement = it.data
                            setAchievementName(currentAchievement)
                            setAchievementDescription(currentAchievement)
                            Log.d(
                                "observeCurrentAchievement",
                                "Success view state, data: ${it.data}"
                            )
                        }

                        is AchievementDetailsViewState.Loading -> {
                        }

                        is AchievementDetailsViewState.Failure -> {
                        }
                    }
                }
            }
        }
    }

    private fun setAchievementName(achievement: Achievement) {
        val achievementTitle =
            getString(R.string.achievement_details_title, achievement.achievementName)
        achievementNameView.text = achievementTitle
    }

    private fun setAchievementDescription(achievement: Achievement) {
        achievementDescriptionView.text = achievement.description
    }

    private fun initAdapter() {
        initCompletedAdapter()
        initUncompletedAdapter()
    }

    private fun initCompletedAdapter() {
        completedView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        completedAdapter = CompletedAdapter(mutableListOf()) { completedFriend ->
        }
        completedView.adapter = completedAdapter
    }

    private fun initUncompletedAdapter() {
        uncompletedView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        uncompletedAdapter = UncompletedAdapter(mutableListOf())
        uncompletedView.adapter = uncompletedAdapter
    }

    private fun setUpCompletedFriends() {
        viewModel.setUpCompletedFriends(achievementId)
    }

    private fun setUpUncompletedFriends() {
        viewModel.setUpUncompletedFriends(achievementId)
    }

    private fun observeCompletedFriends() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.viewStateCompletedFriends.collect {
                    Log.d("observeCompletedFriends", "New view state: $it")
                    when (it) {
                        is AchievementDetailsViewState.Success -> {
                            loadCompletedFriends(it.data)
                            Log.d("observeCompletedFriends", "Success view state, data: ${it.data}")
                        }

                        is AchievementDetailsViewState.Loading -> {
                        }

                        is AchievementDetailsViewState.Failure -> {
                        }
                    }
                }
            }
        }
    }

    private fun loadCompletedFriends(completedFriends: MutableList<UserProfile>) {
        completedAdapter.setCompletedFriends(completedFriends)
    }

    private fun observeUncompletedFriends() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.viewStateUncompletedFriends.collect {
                    Log.d("observeUncompletedFriends", "New view state: $it")
                    when (it) {
                        is AchievementDetailsViewState.Success -> {
                            loadUncompletedFriends(it.data)
                            Log.d(
                                "observeUncompletedFriends",
                                "Success view state, data: ${it.data}"
                            )
                        }

                        is AchievementDetailsViewState.Loading -> {
                        }

                        is AchievementDetailsViewState.Failure -> {
                        }
                    }
                }
            }
        }
    }

    private fun loadUncompletedFriends(uncompletedFriends: MutableList<UserProfile>) {
        Log.d("AchievementDetailsView", "Uncompleted friends $uncompletedFriends")
        uncompletedAdapter.setUncompletedFriends(uncompletedFriends)
    }
}