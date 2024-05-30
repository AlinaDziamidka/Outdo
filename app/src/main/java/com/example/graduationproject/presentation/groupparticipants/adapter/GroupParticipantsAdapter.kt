package com.example.graduationproject.presentation.groupparticipants.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.graduationproject.R
import com.example.graduationproject.databinding.ParticipantsCardBinding
import com.example.graduationproject.domain.entity.UserProfile

class GroupParticipantsAdapter(
    private var participantsList: MutableList<UserProfile>
) :
    RecyclerView.Adapter<GroupParticipantsAdapter.GroupParticipantsViewHolder>() {

    class GroupParticipantsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val binding = ParticipantsCardBinding.bind(itemView)
        private lateinit var participantNameView: TextView

        init {
            bindViews()
        }

        private fun bindViews() {
            participantNameView = binding.participantNameView
        }

        fun onBind(participant: UserProfile) {
            participantNameView.text = participant.username
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): GroupParticipantsAdapter.GroupParticipantsViewHolder {
        val itemView =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.participants_card, parent, false)
        return GroupParticipantsAdapter.GroupParticipantsViewHolder(itemView)
    }

    override fun getItemCount(): Int = participantsList.size

    override fun onBindViewHolder(
        holder: GroupParticipantsAdapter.GroupParticipantsViewHolder,
        position: Int
    ) {
        holder.onBind(participantsList[position])
    }

    fun setParticipants(participants: MutableList<UserProfile>) {
        this.participantsList = participants
        notifyDataSetChanged()
    }

    fun addParticipant(participant: UserProfile) {
        participantsList.add(participant)
        notifyDataSetChanged()
    }

    fun removeParticipant(participant: UserProfile) {
        participantsList.remove(participant)
        notifyDataSetChanged()
    }

    fun clear() {
        participantsList.clear()
        notifyDataSetChanged()
    }
}