package com.example.graduationproject.presentation.notifications

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.graduationproject.databinding.FragmentNotificationScreenBinding
import com.example.graduationproject.domain.entity.Group
import com.example.graduationproject.domain.entity.UserProfile
import com.example.graduationproject.presentation.notifications.adapter.NotificationAdapter
import com.facebook.shimmer.ShimmerFrameLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class NotificationView : Fragment() {

    private val viewModel: NotificationViewModel by viewModels()
    private var _binding: FragmentNotificationScreenBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: NotificationAdapter
    private lateinit var actionBack: LinearLayout
    private lateinit var notifications: RecyclerView
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var userId: String
    private lateinit var shimmerLayout: ShimmerFrameLayout
    private lateinit var errorView: CardView
    private lateinit var updateError: LinearLayout

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
        _binding = FragmentNotificationScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        getCurrentUserId()
        initAdapter()
        setNotifications()
        observeNotifications()
        setUpErrorUpdateAction()
        onPressedBackAction()
    }

    private fun initViews() {
        actionBack = binding.actionPressedBackContainer
        notifications = binding.notificationsRecyclerView
        shimmerLayout = binding.shimmerLayout
        errorView = binding.errorView.errorRootContainer
        updateError = binding.errorView.updateContainer
    }

    private fun getCurrentUserId() {
        sharedPreferences =
            requireContext().getSharedPreferences("session_prefs", Context.MODE_PRIVATE)
        userId = sharedPreferences.getString("current_user_id", "  ") ?: "  "
    }

    private fun initAdapter() {
        notifications.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        adapter = NotificationAdapter(mutableListOf(), { notification ->
            moveToGroupDetailsScreen(notification)
        }, { notification ->
            leaveGroup(notification)
        })
        notifications.adapter = adapter
    }

    private fun moveToGroupDetailsScreen(notification: Pair<UserProfile, Group>) {
        val groupId = notification.second.groupId
        deleteNotification(notification)
        val action =
            NotificationViewDirections.actionNotificationViewToGroupDetailsView(groupId)
        findNavController().navigate(action)
    }

    private fun leaveGroup(notification: Pair<UserProfile, Group>) {
        val groupId = notification.second.groupId
        deleteNotification(notification)
        adapter.removeNotification(notification)
        viewModel.deleteUserGroup(userId, groupId)
    }

    private fun deleteNotification(notification: Pair<UserProfile, Group>) {
        val groupId = notification.second.groupId
        viewModel.deleteNotification(userId, groupId)
    }

    private fun setNotifications() {
        viewModel.setNotifications(userId)
    }

    private fun observeNotifications() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.viewState.collect {
                    when (it) {
                        is NotificationsViewState.Success -> {
                            handleOnSuccess(it.data)
                        }

                        is NotificationsViewState.Loading -> {
                            startShimmer()
                        }

                        is NotificationsViewState.Failure -> {
                            handleOnFailure()
                        }
                    }
                }
            }
        }
    }

    private fun handleOnSuccess(notificationPairs: MutableList<Pair<UserProfile, Group>>) {
        adapter.setNotifications(notificationPairs)
        stopShimmer()
    }

    private fun startShimmer() {
        shimmerLayout.startShimmer()
        shimmerLayout.visibility = View.VISIBLE
        notifications.visibility = View.GONE
    }

    private fun stopShimmer() {
        shimmerLayout.stopShimmer()
        shimmerLayout.visibility = View.GONE
        notifications.visibility = View.VISIBLE
    }

    private fun handleOnFailure() {
        stopShimmer()
        errorView.visibility = View.VISIBLE
    }

    private fun setUpErrorUpdateAction() {
        updateError.setOnClickListener {
            errorView.visibility = View.GONE
            setNotifications()
        }
    }

    private fun onPressedBackAction() {
        actionBack.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }
}
