package com.example.graduationproject.presentation.groupdetails.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.graduationproject.R
import com.example.graduationproject.databinding.ChallengeCardBinding
import com.example.graduationproject.domain.entity.Challenge
import com.example.graduationproject.domain.entity.Group

class ChallengesHistoryAdapter(
    private var challengeList: MutableList<Challenge>,
    private var group: Group,
    private val onChallengeClickListener: (Challenge) -> Unit
) :
    RecyclerView.Adapter<ChallengesHistoryAdapter.ChallengesHistoryViewHolder>() {

    class ChallengesHistoryViewHolder(
        itemView: View,
        private val onChallengeClickListener: (Challenge) -> Unit
    ) :
        RecyclerView.ViewHolder(itemView) {

        private val binding = ChallengeCardBinding.bind(itemView)
        private lateinit var challengeNameView: TextView
        private lateinit var groupNameView: TextView

        init {
            bindViews()
        }

        private fun bindViews() {
            challengeNameView = binding.challengeNameView
            groupNameView = binding.groupNameView
        }

        fun onBind(challenge: Challenge, group: Group) {
            groupNameView.text = group.groupName
            challengeNameView.text = challenge.challengeName

            itemView.setOnClickListener {
                onChallengeClickListener(challenge)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ChallengesHistoryAdapter.ChallengesHistoryViewHolder {
        val itemView =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.challenge_card, parent, false)
        return ChallengesHistoryAdapter.ChallengesHistoryViewHolder(
            itemView,
            onChallengeClickListener
        )
    }

    override fun getItemCount(): Int = challengeList.size

    override fun onBindViewHolder(
        holder: ChallengesHistoryAdapter.ChallengesHistoryViewHolder,
        position: Int
    ) {
        holder.onBind(challengeList[position], group)
    }

    fun setChallenges(challenges: MutableList<Challenge>, group: Group) {
        this.challengeList = challenges
        this.group = group
        notifyDataSetChanged()
    }

    fun addChallenge(challenge: Challenge) {
        challengeList.add(challenge)
        notifyDataSetChanged()
    }

    fun removeChallenge(challenge: Challenge) {
        challengeList.remove(challenge)
        notifyDataSetChanged()
    }

    fun clear() {
        challengeList.clear()
        notifyDataSetChanged()
    }
}