package com.example.graduationproject.presentation.createchallenge.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.graduationproject.R
import com.example.graduationproject.databinding.FriendsCardBinding
import com.example.graduationproject.domain.entity.UserProfile

class AddFriendsAdapter(
    private var friendsList: MutableList<UserProfile>,
    private val onDeleteClick: (UserProfile) -> Unit
) :
    RecyclerView.Adapter<AddFriendsAdapter.AddFriendsViewHolder>() {

    class AddFriendsViewHolder(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {

        private val binding = FriendsCardBinding.bind(itemView)
        private lateinit var friendNameView: TextView
        private lateinit var deleteAction: TextView

        init {
            bindViews()
        }

        private fun bindViews() {
            friendNameView = binding.friendNameView
            deleteAction = binding.deleteFriendView
        }

        fun onBind(friend: UserProfile, onDeleteClick: (UserProfile) -> Unit) {
            friendNameView.text = friend.username
            deleteAction.setOnClickListener {
                onDeleteClick(friend)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AddFriendsAdapter.AddFriendsViewHolder {
        val itemView =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.friends_card, parent, false)
        return AddFriendsAdapter.AddFriendsViewHolder(
            itemView
        )
    }

    override fun getItemCount(): Int = friendsList.size

    override fun onBindViewHolder(
        holder: AddFriendsAdapter.AddFriendsViewHolder,
        position: Int
    ) {
        val friend = friendsList[position]
        holder.onBind(friend) { friendToDelete ->
            removeFriend(friendToDelete)
        }
    }

    fun setFriends(friends: MutableList<UserProfile>) {
        this.friendsList = friends
        notifyDataSetChanged()
    }

    fun addFriend(friend: UserProfile) {
        friendsList.add(friend)
        notifyDataSetChanged()
    }

    fun removeFriend(friend: UserProfile) {
        friendsList.remove(friend)
        notifyDataSetChanged()
    }

    fun clear() {
        friendsList.clear()
        notifyDataSetChanged()
    }
}