package com.example.graduationproject.presentation.home.adapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.graduationproject.R
import com.example.graduationproject.databinding.ChallengeCardBinding
import com.example.graduationproject.domain.entity.Challenge

class ChallengesAdapter(
    private var challenges: MutableList<Challenge>,
    private val onChallengeClickListener: (Challenge) -> Unit
) :
    RecyclerView.Adapter<ChallengesAdapter.ChallengeViewHolder>() {
    class ChallengeViewHolder(
        itemView: View,
        private val onChallengeClickListener: (Challenge) -> Unit
    ) :
        RecyclerView.ViewHolder(itemView) {

        val binding = ChallengeCardBinding.bind(itemView)

        fun onBind(challenge: Challenge) {
//            val group =
            binding.challengeNameView.text = challenge.challengeName
//            binding.groupNameView.text = group.groupName
            itemView.setOnClickListener {
                onChallengeClickListener(challenge)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChallengeViewHolder {
        val itemView =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.challenge_card, parent, false)
        return ChallengeViewHolder(itemView, onChallengeClickListener)
    }

    override fun getItemCount(): Int = challenges.size

    override fun onBindViewHolder(holder: ChallengeViewHolder, position: Int) {
        holder.onBind(challenges[position])
    }

    fun setChallenges(challenges: MutableList<Challenge>) {
        this.challenges = challenges
        notifyDataSetChanged()
    }

    fun addChallenge(challenge: Challenge) {
        challenges.add(challenge)
        notifyDataSetChanged()
    }

    fun removeChallenge(challenge: Challenge) {
        challenges.remove(challenge)
        notifyDataSetChanged()
    }

    fun clear() {
        challenges.clear()
        notifyDataSetChanged()
    }
}




