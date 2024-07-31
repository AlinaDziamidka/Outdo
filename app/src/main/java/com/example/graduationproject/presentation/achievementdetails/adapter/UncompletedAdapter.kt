package com.example.graduationproject.presentation.achievementdetails.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.graduationproject.R
import com.example.graduationproject.databinding.UncompletedAchievementCardBinding
import com.example.graduationproject.domain.entity.UserProfile

class UncompletedAdapter(
    private var uncompletedFriends: MutableList<UserProfile>
) :
    RecyclerView.Adapter<UncompletedAdapter.UncompletedAdapterViewHolder>() {

    class UncompletedAdapterViewHolder(
        itemView: View,
    ) : RecyclerView.ViewHolder(itemView) {

        private val binding = UncompletedAchievementCardBinding.bind(itemView)
        private lateinit var friendNameView: TextView

        init {
            bindViews()
        }

        private fun bindViews() {
            friendNameView = binding.friendNameView
        }

        fun onBind(friend: UserProfile) {
            Log.d("UncompletedAdapter", "Binding data: ${friend.username}")
            friendNameView.text = friend.username
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): UncompletedAdapter.UncompletedAdapterViewHolder {
        val itemView =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.uncompleted_achievement_card, parent, false)
        return UncompletedAdapter.UncompletedAdapterViewHolder(
            itemView
        )
    }

    override fun getItemCount(): Int = uncompletedFriends.size

    override fun onBindViewHolder(
        holder: UncompletedAdapter.UncompletedAdapterViewHolder,
        position: Int
    ) {
        val friend = uncompletedFriends[position]
        holder.onBind(friend)
    }

    fun setUncompletedFriends(uncompletedFriends: MutableList<UserProfile>) {
        this.uncompletedFriends = uncompletedFriends
        notifyDataSetChanged()
    }

    fun addUnCompletedFriend(friend: UserProfile) {
        uncompletedFriends.add(friend)
        notifyDataSetChanged()
    }

    fun clear() {
        uncompletedFriends.clear()
        notifyDataSetChanged()
    }
}