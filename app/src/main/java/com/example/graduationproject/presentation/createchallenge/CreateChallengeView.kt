package com.example.graduationproject.presentation.createchallenge

import android.app.DatePickerDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
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
import com.example.graduationproject.databinding.FragmentCreateChallengeBinding
import com.example.graduationproject.domain.entity.Challenge
import com.example.graduationproject.domain.entity.UserProfile
import com.example.graduationproject.presentation.addfriends.AddFriendsView.Companion.SELECTED_FRIENDS_LIST_KEY
import com.example.graduationproject.presentation.addfriends.AddFriendsView.Companion.SELECTED_FRIENDS_REQUEST_KEY
import com.example.graduationproject.presentation.createchallenge.adapter.AddFriendsAdapter
import com.example.graduationproject.presentation.createchallenge.adapter.CreateAchievementAdapter
import com.example.graduationproject.presentation.util.getSerializableCompat
import com.example.graduationproject.presentation.util.putArguments
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

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
    private lateinit var friendsAdapter: AddFriendsAdapter
    private lateinit var achievementAdapter: CreateAchievementAdapter
    private lateinit var progressOverlay: FrameLayout
    private lateinit var emptyStartDateView: TextView
    private lateinit var emptyFinishDateView: TextView

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
        showAchievements()
        setFriends()
        observeFriends()
        setUpAddFriendsButton()
        setUpDatePickers()
        setUpCreateChallengeAction()
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
        progressOverlay = binding.progressOverlay
        emptyFinishDateView = binding.invalidEndDateView
        emptyStartDateView = binding.invalidStartDateView
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
        achievementAdapter =
            CreateAchievementAdapter(mutableListOf()) { position, name, description ->
                viewModel.updateAchievementCard(position, Pair(name, description))
            }
        achievementsView.adapter = achievementAdapter
    }

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

    private fun showAchievements() {
        val achievementCards = viewModel.achievementCards.value
        if (achievementCards.isEmpty()) {
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
            setArgumentsToAddFriendsView()
            moveToAddFriendsView(groupId)
        }
    }

    private fun setArgumentsToAddFriendsView() {
        val arrayList = ArrayList(viewModel.addedFriends.value)
        this.putArguments(ADDED_FRIENDS_TO_CHALLENGE_LIST_KEY to arrayList as Serializable)
        setFragmentResult(ADDED_FRIENDS_TO_CHALLENGE_REQUEST_KEY, arguments ?: Bundle())
    }

    private fun moveToAddFriendsView(groupId: String) {
        val action =
            CreateChallengeViewDirections.actionCreateChallengeViewToAddFriendsView(groupId)
        findNavController().navigate(action)
    }

    private fun setUpDatePickers() {
        setUpStartDate()
        setUpFinishDate()
    }

    private fun setUpStartDate() {
        startDateView.setOnClickListener {
            showDatePickerDialog { day, month, year ->
                startDateView.setText(
                    getString(
                        R.string.create_challenge_date_format,
                        day,
                        month + 1,
                        year
                    )
                )
            }
        }
    }

    private fun setUpFinishDate() {
        finishDateView.setOnClickListener {
            showDatePickerDialog { day, month, year ->
                finishDateView.setText(
                    getString(
                        R.string.create_challenge_date_format,
                        day,
                        month + 1,
                        year
                    )
                )
            }
        }
    }

    private fun showDatePickerDialog(onDateSet: (day: Int, month: Int, year: Int) -> Unit) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            requireContext(), R.style.DatePickerDialog,
            { _, selectedYear, selectedMonth, selectedDay ->
                onDateSet(selectedDay, selectedMonth, selectedYear)
            },
            year, month, day
        )
        datePickerDialog.show()
    }

    private fun setUpCreateChallengeAction() {
        saveChallengeAction.setOnClickListener {
            setChallenge()
        }
    }

    private fun setChallenge() {
        val startDateString = startDateView.text.toString()
        val finishDateString = finishDateView.text.toString()
        if (isDateCorrect(startDateString, finishDateString)) {
            val challengeName = challengeNameView.text.toString()
            val challengeDescription = challengeDescriptionView.text.toString()
            val startDate = convertStartDateToLong(startDateString)
            val finishDate = convertFinishDateToLong(finishDateString)
            val groupId = args.groupId
            val creatorId = getUserId()
            val achievements = viewModel.achievementCards.value
            val friends = viewModel.addedFriends.value.toList()

            viewModel.setChallenge(
                groupId = groupId,
                challengeName = challengeName,
                creatorId = creatorId,
                startDate = startDate,
                finishDate = finishDate,
                description = challengeDescription,
                achievements = achievements,
                friends = friends
            )

            handleOnChallengeSet()
        }
    }

    private fun isDateCorrect(startDate: String?, finishDate: String?): Boolean {
        var isCorrect = true
        if (startDate.isNullOrEmpty()) {
            emptyStartDateView.visibility = View.VISIBLE
            isCorrect = false
        } else {
            emptyStartDateView.visibility = View.GONE
        }

        if (finishDate.isNullOrEmpty()) {
            emptyFinishDateView.visibility = View.VISIBLE
            isCorrect = false
        } else {
            emptyFinishDateView.visibility = View.GONE
        }
        return isCorrect
    }

    private fun convertStartDateToLong(startDate: String): Long {
        return try {
            val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val date = sdf.parse(startDate)
            date?.time ?: 0L
        } catch (e: Exception) {
            e.printStackTrace()
            0L
        }
    }

    private fun convertFinishDateToLong(finishDate: String): Long {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val date = dateFormat.parse(finishDate)
        val calendar = Calendar.getInstance().apply {
            time = date
            set(Calendar.HOUR_OF_DAY, 23)
            set(Calendar.MINUTE, 59)
            set(Calendar.SECOND, 59)
            set(Calendar.MILLISECOND, 999)
        }
        return calendar.timeInMillis
    }

    private fun getUserId(): String {
        sharedPreferences =
            requireContext().getSharedPreferences("session_prefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("current_user_id", "  ") ?: "  "
    }

    private fun handleOnChallengeSet() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.viewState.collect() {
                    when (it) {
                        is CreateChallengeViewState.Success -> {
                            val challenge = it.data
                            handleOnSuccess(challenge)
                        }

                        is CreateChallengeViewState.Loading -> {
                            showLoading()
                        }

                        is CreateChallengeViewState.Failure -> {
                            hideLoading()
                        }
                    }
                }
            }
        }
    }

    private fun showLoading() {
        progressOverlay.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        progressOverlay.visibility = View.GONE
    }

    private fun handleOnSuccess(challenge: Challenge) {
        hideLoading()
        moveToChallengeDetailsScreen(challenge)
    }

    private fun moveToChallengeDetailsScreen(challenge: Challenge) {
        val challengeId = challenge.challengeId
        val action = CreateChallengeViewDirections.actionCreateChallengeViewToChallengeDetailsView(
            challengeId
        )
        findNavController().navigate(action)
    }
}