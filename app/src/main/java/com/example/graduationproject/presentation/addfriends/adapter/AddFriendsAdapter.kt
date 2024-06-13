package com.example.graduationproject.presentation.addfriends.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.graduationproject.R
import com.example.graduationproject.databinding.FriendsChecklistBinding
import com.example.graduationproject.domain.entity.UserProfile

class AddFriendsAdapter(
    private var friendsList: MutableList<UserProfile>
) :
    RecyclerView.Adapter<AddFriendsAdapter.AddFriendsViewHolder>() {

    private val selectedFriends = mutableSetOf<UserProfile>()

    class AddFriendsViewHolder(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {

        private val binding = FriendsChecklistBinding.bind(itemView)
        private lateinit var friendNameView: TextView
        lateinit var friendCheckView: CheckBox

        init {
            bindViews()
        }

        private fun bindViews() {
            friendNameView = binding.friendNameView
            friendCheckView = binding.friendCheckView
        }

        fun onBind(friend: UserProfile, isSelected: Boolean) {
            friendNameView.text = friend.username
            friendCheckView.isChecked = isSelected
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AddFriendsAdapter.AddFriendsViewHolder {
        val itemView =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.friends_checklist, parent, false)
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
        val isSelected = selectedFriends.contains(friend)
        holder.onBind(friend, isSelected)

        holder.friendCheckView.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                selectedFriends.add(friend)
            } else {
                selectedFriends.remove(friend)
            }
        }
    }

    fun setFriends(friends: MutableList<UserProfile>) {
        this.friendsList = friends
        notifyDataSetChanged()
    }

    fun getSelectedFriends(): List<UserProfile> {
        return selectedFriends.toList()
    }

    fun addFriend(friend: UserProfile) {
        friendsList.add(friend)
        notifyDataSetChanged()
    }

    fun removeFriend(friend: UserProfile) {
        friendsList.add(friend)
        notifyDataSetChanged()
    }

    fun clear() {
        friendsList.clear()
        selectedFriends.clear()
        notifyDataSetChanged()
    }
}