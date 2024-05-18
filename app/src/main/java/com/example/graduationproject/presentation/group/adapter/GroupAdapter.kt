package com.example.graduationproject.presentation.group.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.graduationproject.R
import com.example.graduationproject.databinding.GroupCardBinding
import com.example.graduationproject.domain.entity.GroupParticipants


class GroupAdapter(
    private var groupParticipantsList: MutableList<GroupParticipants>,
    private val onGroupClickListener: (GroupParticipants) -> Unit
) :
    RecyclerView.Adapter<GroupAdapter.GroupViewHolder>() {

    class GroupViewHolder(
        itemView: View,
        private val onGroupClickListener: (GroupParticipants) -> Unit
    ) :
        RecyclerView.ViewHolder(itemView) {

        private val binding = GroupCardBinding.bind(itemView)
        private lateinit var groupAvatar: TextView
        private lateinit var groupNameView: TextView
        private lateinit var participantsCountView: TextView

        init {
            bindViews()
        }

        private fun bindViews() {
            groupAvatar = binding.avatarView
            groupNameView = binding.groupNameView
            participantsCountView = binding.participantsCountView
        }

        fun onBind(groupParticipants: GroupParticipants) {
            val group = groupParticipants.group
            val participants = groupParticipants.users

            groupNameView.text = group.groupName
            participantsCountView.text = participants.size.toString()

            itemView.setOnClickListener {
                onGroupClickListener(groupParticipants)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupViewHolder {
        val itemView =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.group_card, parent, false)
        return GroupViewHolder(itemView, onGroupClickListener)
    }

    override fun getItemCount(): Int = groupParticipantsList.size

    override fun onBindViewHolder(holder: GroupViewHolder, position: Int) {
        holder.onBind(groupParticipantsList[position])
    }

    fun setGroupParticipants(groupParticipants: MutableList<GroupParticipants>) {
        this.groupParticipantsList = groupParticipants
        notifyDataSetChanged()
    }

    fun addGroupParticipants(groupParticipants: GroupParticipants) {
        groupParticipantsList.add(groupParticipants)
        notifyDataSetChanged()
    }

    fun removeGroupAndChallenges(groupParticipants: GroupParticipants) {
        groupParticipantsList.remove(groupParticipants)
        notifyDataSetChanged()
    }

    fun clear() {
        groupParticipantsList.clear()
        notifyDataSetChanged()
    }
}




