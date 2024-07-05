package com.example.graduationproject.presentation.notifications

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
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
import dagger.hilt.android.AndroidEntryPoint
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
    }

    private fun initViews() {
        actionBack = binding.actionPressedBackContainer
        notifications = binding.notificationsRecyclerView
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


    private fun setNotifications() {
        viewModel.setNotifications(userId)
    }

    private fun observeNotifications() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.viewState.collect {
                    Log.d("observeNotifications", "New view state: $it")
                    when (it) {
                        is NotificationsViewState.Success -> {
                            handleOnSuccess(it.data)
                            Log.d("observeNotifications", "Success view state, data: ${it.data}")
                        }

                        is NotificationsViewState.Loading -> {
                            Log.d("observeNotifications", "Loading view state")
                        }

                        is NotificationsViewState.Failure -> {
                            Log.d("observeNotifications", "Failure view state")
                        }
                    }
                }
            }
        }
    }

    private fun handleOnSuccess(notificationPairs: MutableList<Pair<UserProfile, Group>>) {
        adapter.setNotifications(notificationPairs)
    }

    private fun deleteNotification (notification: Pair<UserProfile, Group>){
        val  groupId = notification.second.groupId
        viewModel.deleteNotification(userId, groupId)
    }
}
