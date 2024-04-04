package com.example.graduationproject.presentation.home

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
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
        val sharedPreferences =
            requireContext().getSharedPreferences("session_prefs", Context.MODE_PRIVATE)
        val userId = sharedPreferences.getString("current_user_id", "  ") ?: "  "
        Log.d("HomeViewFragment", "User ID: $userId")
        viewModel.setUpUserGroups(userId)
    }

    private fun initViews() {
        progressView = binding.progressView
        challengeView = binding.challengeRecyclerView
    }

    private fun setUserName() {
        val username = args.username
        binding.userNameView.setText(username)
    }


    private fun observeChallenges() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.viewState.collect {
                    when (it) {
                        is HomeViewState.Success -> {
                            val groupAndChallengesPairs = transformToGroupAndChallengesPair(it.data)
                            Log.d("HomeViewFragment", "Success: $groupAndChallengesPairs")
                            handleOnSuccess(groupAndChallengesPairs)
                        }

                        is HomeViewState.Loading -> {
                            Log.d("HomeViewFragment", "Loading")
                            progressView.visibility = View.VISIBLE
                            challengeView.visibility = View.GONE
                        }

                        is HomeViewState.Failure -> {
                            Log.d("HomeViewFragment", "Failure: ${it.message}")
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
        return groupAndChallengesPairs
    }

    private fun handleOnSuccess(groupAndChallenges: MutableList<Pair<Group, Challenge>>) {
        progressView.visibility = View.GONE
        adapter.setGroupAndChallenges(groupAndChallenges)
        challengeView.visibility = View.VISIBLE
    }


}