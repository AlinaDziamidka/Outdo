package com.example.graduationproject.presentation.challenges.adapter

import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.graduationproject.R
import com.example.graduationproject.databinding.ChallengeCardBinding
import com.example.graduationproject.domain.entity.Challenge
import com.example.graduationproject.domain.entity.Group
import java.util.Locale

class AllChallengesAdapter(
    private var groupAndChallengesPairs: MutableList<Pair<Group, Challenge>>,
    private val onChallengeClickListener: (Challenge) -> Unit
) :
    RecyclerView.Adapter<AllChallengesAdapter.ChallengeViewHolder>() {

    class ChallengeViewHolder(
        itemView: View,
        private val onChallengeClickListener: (Challenge) -> Unit
    ) :
        RecyclerView.ViewHolder(itemView) {

        private val binding = ChallengeCardBinding.bind(itemView)
        private lateinit var countdownTimer: CountDownTimer
        private lateinit var challengeNameView: TextView
        private lateinit var groupNameView: TextView
        private lateinit var endTimeView: TextView

        init {
            bindViews()
        }

        private fun bindViews() {
            challengeNameView = binding.challengeNameView
            groupNameView = binding.groupNameView
            endTimeView = binding.timeView
        }

        fun onBind(pair: Pair<Group, Challenge>) {
            val (group, challenge) = pair

            val endTime = challenge.endTime
            setUpTimeView(endTime)

            groupNameView.text = group.groupName
            challengeNameView.text = challenge.challengeName

            itemView.setOnClickListener {
                onChallengeClickListener(challenge)
            }
        }

        private fun setUpTimeView(endTimeMillis: Long) {
            countdownTimer =
                object : CountDownTimer(calculateTimeUntilFinished(endTimeMillis), 1000) {
                    override fun onTick(timeUntilFinished: Long) {
                        updateCountdownText(timeUntilFinished)
                    }

                    override fun onFinish() {

                    }
                }
            countdownTimer.start()
        }

        private fun calculateTimeUntilFinished(endTimeMillis: Long): Long {
            val currentMillis = System.currentTimeMillis()
            return endTimeMillis - currentMillis
        }

        private fun updateCountdownText(timeUntilFinished: Long) {
            val seconds = timeUntilFinished / 1000
            val hours = seconds / 3600
            val minutes = (seconds % 3600) / 60
            val remainingSeconds = seconds % 60

            val formattedTime = if (hours < 24) {
                String.format(
                    Locale.getDefault(),
                    "%02d:%02d:%02d",
                    hours,
                    minutes,
                    remainingSeconds
                )
            } else {
                val days = hours / 24
                val remainingHours = hours % 24
                if (Locale.getDefault() == Locale.ENGLISH) {
                    String.format(Locale.getDefault(), "%d d %02d h", days, remainingHours)
                } else {
                    String.format(Locale.getDefault(), "%d дн %02d ч", days, remainingHours)
                }
            }
            endTimeView.text = formattedTime
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChallengeViewHolder {
        val itemView =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.challenge_card, parent, false)
        return ChallengeViewHolder(itemView, onChallengeClickListener)
    }

    override fun getItemCount(): Int = groupAndChallengesPairs.size

    override fun onBindViewHolder(holder: ChallengeViewHolder, position: Int) {
        holder.onBind(groupAndChallengesPairs[position])
    }

    fun setGroupAndChallenges(groupAndChallenges: MutableList<Pair<Group, Challenge>>) {
        this.groupAndChallengesPairs = groupAndChallenges
        notifyDataSetChanged()
    }

    fun addGroupAndChallenges(groupAndChallenges: Pair<Group, Challenge>) {
        groupAndChallengesPairs.add(groupAndChallenges)
        notifyDataSetChanged()
    }

    fun removeGroupAndChallenges(groupAndChallenges: Pair<Group, Challenge>) {
        groupAndChallengesPairs.remove(groupAndChallenges)
        notifyDataSetChanged()
    }

    fun clear() {
        groupAndChallengesPairs.clear()
        notifyDataSetChanged()
    }
}




