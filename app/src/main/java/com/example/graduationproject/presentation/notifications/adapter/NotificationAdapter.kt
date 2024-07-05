package com.example.graduationproject.presentation.notifications.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.RecyclerView
import com.example.graduationproject.R
import com.example.graduationproject.databinding.NotificationDescriptionCardBinding
import com.example.graduationproject.domain.entity.Group
import com.example.graduationproject.domain.entity.UserProfile

class NotificationAdapter(
    private var notifications: MutableList<Pair<UserProfile, Group>>,
    private val onMoveToGroupClick: (Pair<UserProfile, Group>) -> Unit,
    private val onLeaveGroupClick: (Pair<UserProfile, Group>) -> Unit
) :
    RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder>() {

    class NotificationViewHolder(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {

        private val binding = NotificationDescriptionCardBinding.bind(itemView)
        private lateinit var descriptionView: TextView
        private lateinit var moveToGroupAction: AppCompatButton
        private lateinit var leaveGroupAction: AppCompatButton

        init {
            bindViews()
        }

        private fun bindViews() {
            descriptionView = binding.descriptionView
            moveToGroupAction = binding.actionToGroupView
            leaveGroupAction = binding.actionLeaveGroupView
        }

        fun onBind(
            notification: Pair<UserProfile, Group>,
            onMoveToGroupClick: (Pair<UserProfile, Group>) -> Unit,
            onLeaveGroupClick: (Pair<UserProfile, Group>) -> Unit
        ) {
            val (creator, group) = notification
            descriptionView.text = itemView.context.getString(
                R.string.notification_screen_description,
                creator.username,
                group.groupName
            )

            moveToGroupAction.setOnClickListener {
                onMoveToGroupClick(notification)
            }

            leaveGroupAction.setOnClickListener {
                onLeaveGroupClick(notification)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): NotificationViewHolder {
        val itemView =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.notification_description_card, parent, false)
        return NotificationViewHolder(
            itemView
        )
    }

    override fun getItemCount(): Int = notifications.size

    override fun onBindViewHolder(
        holder: NotificationViewHolder,
        position: Int
    ) {
        val notification = notifications[position]
        holder.onBind(notification, onMoveToGroupClick, onLeaveGroupClick)
    }

    fun setNotifications(notifications: MutableList<Pair<UserProfile, Group>>) {
        this.notifications = notifications
        notifyDataSetChanged()
    }

    fun addNotification(notification: Pair<UserProfile, Group>) {
        notifications.add(notification)
        notifyDataSetChanged()
    }

    fun removeNotification(notification: Pair<UserProfile, Group>) {
        notifications.remove(notification)
        notifyDataSetChanged()
    }

    fun clear() {
        notifications.clear()
        notifyDataSetChanged()
    }
}