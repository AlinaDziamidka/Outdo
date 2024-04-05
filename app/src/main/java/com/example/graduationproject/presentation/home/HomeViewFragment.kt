package com.example.graduationproject.presentation.home

import android.content.Context
import android.content.SharedPreferences
import android.opengl.Visibility
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
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.graduationproject.databinding.FragmentHomeBinding
import com.example.graduationproject.domain.entity.Challenge
import com.example.graduationproject.domain.entity.Group
import com.example.graduationproject.domain.entity.GroupAndChallenges
import com.example.graduationproject.presentation.home.adapter.ChallengesAdapter
import com.example.graduationproject.presentation.signin.SignInViewState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class HomeViewFragment : Fragment() {

    private val viewModel: HomeViewModel by viewModels()
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    val args by navArgs<HomeViewArgs>()
    private lateinit var adapter: ChallengesAdapter
    private lateinit var challengeView: RecyclerView
    private lateinit var progressView: ProgressBar
    private lateinit var currentChallengeView: TextView
    private lateinit var showAllChallengesView: TextView
    private lateinit var userName: TextView
    private lateinit var sharedPreferences: SharedPreferences

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

    private fun initAdapter() {
        challengeView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        adapter = ChallengesAdapter(mutableListOf()) { challenge ->
            moveToChallengeDetailsScreen()
        }
        challengeView.adapter = adapter
    }

    private fun moveToChallengeDetailsScreen() {
        val action = HomeViewFragmentDirections.actionHomeViewFragmentToChallengeDetailsView()
        findNavController().navigate(action)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        initAdapter()
        setUserName()
        setUpChallenges()
        observeChallenges()
    }

    private fun setUpChallenges() {
        val userId = sharedPreferences.getString("current_user_id", "  ") ?: "  "
        viewModel.setUpUserGroups(userId)
    }

    private fun initViews() {
        progressView = binding.progressView
        challengeView = binding.challengeRecyclerView
        currentChallengeView = binding.currentChallengesView
        showAllChallengesView = binding.showAllView
        userName = binding.userNameView
    }

    private fun setUserName() {
        sharedPreferences =
            requireContext().getSharedPreferences("session_prefs", Context.MODE_PRIVATE)
        val username = sharedPreferences.getString("current_username", null)
        if (username != null) {
            userName.text = username
        } else {
            userName.text = args.username
        }
    }


    private fun observeChallenges() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.viewState.collect {
                    when (it) {
                        is HomeViewState.Success -> {
                            val groupAndChallengesPairs = transformToGroupAndChallengesPair(it.data)
                            handleOnSuccess(groupAndChallengesPairs)
                        }

                        is HomeViewState.Loading -> {
                            progressView.visibility = View.VISIBLE
                            challengeView.visibility = View.GONE
                        }

                        is HomeViewState.Failure -> {
                            progressView.visibility = View.GONE
                            currentChallengeView.visibility = View.GONE
                            showAllChallengesView.visibility = View.GONE
                            progressView.visibility = View.VISIBLE
                            challengeView.visibility = View.GONE
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
}