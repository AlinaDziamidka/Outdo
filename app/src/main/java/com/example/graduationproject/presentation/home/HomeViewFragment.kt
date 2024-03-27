package com.example.graduationproject.presentation.home

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.graduationproject.databinding.FragmentHomeBinding
import com.example.graduationproject.presentation.home.adapter.ChallengesAdapter
import com.example.graduationproject.presentation.signup.SignUpViewFragmentDirections
import dagger.hilt.android.AndroidEntryPoint


//@AndroidEntryPoint
class HomeViewFragment : Fragment() {

//    private val viewModel: HomeViewModel by viewModels()
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    val args by navArgs<HomeViewArgs>()
    private lateinit var adapter: ChallengesAdapter
    private lateinit var challengeView: RecyclerView



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

        initAdapter()
        binding.challengeRecyclerView.adapter = adapter

        return binding.root
    }

    private fun initAdapter() {
        challengeView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        adapter = ChallengesAdapter(mutableListOf()) { challenge ->
            moveToChallengeDetailsScreen()
        }
    }

    private fun moveToChallengeDetailsScreen() {
        val action = HomeViewFragmentDirections.actionHomeViewFragmentToChallengeDetailsView()
        findNavController().navigate(action)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
    }

    private fun initViews() {
        val username = args.username
        binding.userNameView.setText(username)


    }
}