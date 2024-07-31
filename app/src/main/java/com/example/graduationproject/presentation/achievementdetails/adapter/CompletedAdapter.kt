package com.example.graduationproject.presentation.achievementdetails.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.graduationproject.R
import com.example.graduationproject.databinding.CompletedAchievementCardBinding
import com.example.graduationproject.domain.entity.UserProfile

class CompletedAdapter(
    private var completedFriends: MutableList<UserProfile>,
    private val onDetailsClick: (UserProfile) -> Unit
) :
    RecyclerView.Adapter<CompletedAdapter.CompletedAdapterViewHolder>() {

    class CompletedAdapterViewHolder(
        itemView: View,
    ) : RecyclerView.ViewHolder(itemView) {

        private val binding = CompletedAchievementCardBinding.bind(itemView)
        private lateinit var friendNameView: TextView
        private lateinit var detailsAction: TextView

        init {
            bindViews()
        }

        private fun bindViews() {
            friendNameView = binding.friendNameView
            detailsAction = binding.detailsView
        }

        fun onBind(friend: UserProfile, onDetailsClick: (UserProfile) -> Unit) {
            friendNameView.text = friend.username
            detailsAction.setOnClickListener {
                onDetailsClick(friend)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CompletedAdapter.CompletedAdapterViewHolder {
        val itemView =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.completed_achievement_card, parent, false)
        return CompletedAdapter.CompletedAdapterViewHolder(
            itemView
        )
    }

    override fun getItemCount(): Int = completedFriends.size

    override fun onBindViewHolder(
        holder: CompletedAdapter.CompletedAdapterViewHolder,
        position: Int
    ) {
        val friend = completedFriends[position]
        holder.onBind(friend, onDetailsClick)
    }

    fun setCompletedFriends(completedFriends: MutableList<UserProfile>) {
        this.completedFriends = completedFriends
        notifyDataSetChanged()
    }

    fun addCompletedFriend(friend: UserProfile) {
        completedFriends.add(friend)
        notifyDataSetChanged()
    }

    fun clear() {
        completedFriends.clear()
        notifyDataSetChanged()
    }
}