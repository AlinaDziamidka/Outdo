package com.example.graduationproject.presentation.createchallenge


import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.graduationproject.R
import com.example.graduationproject.databinding.CreateAchievementCardBinding
import com.example.graduationproject.databinding.FragmentCreateChallengeBinding
import com.example.graduationproject.databinding.FragmentCreateGroupBinding
import com.example.graduationproject.domain.entity.Achievement
import com.example.graduationproject.domain.entity.Group
import com.example.graduationproject.domain.entity.UserProfile
import com.example.graduationproject.presentation.addfriends.AddFriendsView.Companion.SELECTED_FRIENDS_LIST_KEY
import com.example.graduationproject.presentation.addfriends.AddFriendsView.Companion.SELECTED_FRIENDS_REQUEST_KEY
import com.example.graduationproject.presentation.createchallenge.adapter.AddFriendsAdapter
import com.example.graduationproject.presentation.createchallenge.adapter.CreateAchievementAdapter
import com.example.graduationproject.presentation.creategroup.CreateGroupView.Companion.ADDED_FRIENDS_TO_GROUP_LIST_KEY
import com.example.graduationproject.presentation.creategroup.CreateGroupView.Companion.ADDED_FRIENDS_TO_GROUP_REQUEST_KEY
import com.example.graduationproject.presentation.creategroup.CreateGroupViewDirections
import com.example.graduationproject.presentation.creategroup.adapter.CreateGroupAdapter
import com.example.graduationproject.presentation.groupdetails.adapter.ChallengesAdapter
import com.example.graduationproject.presentation.groupdetails.adapter.ChallengesHistoryAdapter
import com.example.graduationproject.presentation.util.getSerializableCompat
import com.example.graduationproject.presentation.util.putArguments
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.io.Serializable

@AndroidEntryPoint
class CreateChallengeView : Fragment() {

    private val viewModel: CreateChallengeViewModel by viewModels()
    private var _binding: FragmentCreateChallengeBinding? = null
    private val binding get() = _binding!!
    private val args: CreateChallengeViewArgs by navArgs()
    private lateinit var saveChallengeAction: Button
    private lateinit var challengeNameView: EditText
    private lateinit var challengeDescriptionView: EditText
    private lateinit var startDateView: EditText
    private lateinit var finishDateView: EditText
    private lateinit var addFriendsAction: RelativeLayout
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var friendsView: RecyclerView
    private lateinit var achievementsView: RecyclerView
    private lateinit var addAchievementCard: TextView

    //    private var achievementCards = mutableListOf<AchievementCard>()
    private lateinit var friendsAdapter: AddFriendsAdapter
    private lateinit var achievementAdapter: CreateAchievementAdapter

    companion object {
        const val ADDED_FRIENDS_TO_CHALLENGE_REQUEST_KEY = "ADDED_FRIENDS_TO_CHALLENGE_REQUEST_KEY"
        const val ADDED_FRIENDS_TO_CHALLENGE_LIST_KEY = "ADDED_FRIENDS_TO_CHALLENGE_LIST_KEY"
    }

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
        _binding = FragmentCreateChallengeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        initAdapter()
        setUpAddAchievementCard()
//        observeAchievements()
        showAchievements()
        setFriends()
        observeFriends()
        setUpAddFriendsButton()
    }


    private fun initViews() {
        saveChallengeAction = binding.saveChallengeAction
        challengeNameView = binding.challengeNameContent
        challengeDescriptionView = binding.challengeDescriptionContent
        startDateView = binding.startDateContent
        finishDateView = binding.finishDateContent
        achievementsView = binding.achievementRecyclerView
        addFriendsAction = binding.addFriendsAction.rootButtonContainer
        friendsView = binding.friendsRecyclerView
        addAchievementCard = binding.addOneMoreAchievement
    }

    private fun initAdapter() {
        initFriendsAdapter()
        initAchievementAdapter()
    }

    private fun initFriendsAdapter() {
        friendsView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        friendsAdapter = AddFriendsAdapter(mutableListOf()) { friend ->
            viewModel.deleteFriend(friend)
        }
        friendsView.adapter = friendsAdapter
    }

    private fun initAchievementAdapter() {
        achievementsView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        achievementAdapter = CreateAchievementAdapter(mutableListOf()) { position, name, description ->
            viewModel.updateAchievementCard(position, Pair(name, description))
        }
        achievementsView.adapter = achievementAdapter
    }

//    private fun showAllAchievements() {
//        achievementContainer.removeAllViews()
//        val achievementCards = viewModel.achievementCards.value
//        Log.d("CreateChallengeView", "AchievementsCards: $achievementCards")
//        if (achievementCards.isEmpty()) {
//            Log.d("CreateChallengeView", "AchievementsCards is empty")
//            addAchievementCard()
//        } else {
//            achievementCards.forEach { achievementCard ->
//                val parent = achievementCard.rootView.root.parent as? ViewGroup
//                parent?.removeView(achievementCard.rootView.root)
//                achievementContainer.addView(achievementCard.rootView.root)
//            }
//        }
//    }

    private fun setUpAddAchievementCard() {
        addAchievementCard.setOnClickListener {
            addAchievementCard()
        }
    }

    private fun addAchievementCard() {
        val achievement = Pair("", "")
        achievementAdapter.addAchievement(achievement)
        viewModel.addAchievementCard(achievement)
    }
//
//    private fun addAchievementCard() {
//        val achievementNumber = viewModel.achievementCards.value.size + 1
//        val inflater = LayoutInflater.from(context)
//        val achievementBinding =
//            CreateAchievementCardBinding.inflate(inflater, achievementContainer, false)
//        val achievementCard =
//            AchievementCard(requireContext(), achievementBinding, achievementNumber)
//        achievementContainer.addView(achievementBinding.root)
//        viewModel.addAchievementCard(achievementCard)
//    }

//    private fun observeAchievements() {
//        lifecycleScope.launch {
//            repeatOnLifecycle(Lifecycle.State.STARTED) {
//                viewModel.achievementCards.collect { achievementCards ->
//                    Log.d("CreateChallengeView", "AchievementsCards: $achievementCards")
//                    showAchievements(achievementCards)
//                }
//            }
//        }
//    }

    private fun showAchievements() {
        val achievementCards = viewModel.achievementCards.value
        Log.d("CreateChallengeView", "AchievementsCards: $achievementCards")
        if (achievementCards.isEmpty()) {
            Log.d("CreateChallengeView", "AchievementsCards is empty")
            addAchievementCard()
        } else {
            achievementAdapter.setAchievements(achievementCards)
        }
    }

    private fun setFriends() {
        parentFragmentManager.setFragmentResultListener(
            SELECTED_FRIENDS_REQUEST_KEY,
            this
        ) { _, bundle ->
            val friendsArrayList = bundle.getSerializableCompat(
                SELECTED_FRIENDS_LIST_KEY,
                ArrayList::class.java
            ) as? ArrayList<UserProfile>
            friendsArrayList?.let {
                viewModel.addFriends(it)
            }
        }
    }

    private fun observeFriends() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.addedFriends.collect { friends ->
                    friendsAdapter.setFriends(friends)
                }
            }
        }
    }

    private fun setUpAddFriendsButton() {
        addFriendsAction.setOnClickListener {
            val groupId = args.groupId
            Log.d("CreateChallengeView", "groupId: $groupId")
            setArgumentsToAddFriendsView()
            moveToAddFriendsView(groupId)
        }
    }

    private fun setArgumentsToAddFriendsView() {
        Log.d("CreateChallengeView", "Starting setArgumentsToAddFriendsView")
        val arrayList = ArrayList(viewModel.addedFriends.value)
        this.putArguments(ADDED_FRIENDS_TO_CHALLENGE_LIST_KEY to arrayList as Serializable)
        setFragmentResult(ADDED_FRIENDS_TO_CHALLENGE_REQUEST_KEY, arguments ?: Bundle())
    }

    private fun moveToAddFriendsView(groupId: String) {
        Log.d("CreateChallengeView", "Starting moveToAddFriendsView")
        val action =
            CreateChallengeViewDirections.actionCreateChallengeViewToAddFriendsView(groupId)
        findNavController().navigate(action)
    }

}