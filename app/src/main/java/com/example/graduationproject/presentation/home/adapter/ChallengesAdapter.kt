package com.example.graduationproject.presentation.home.adapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.graduationproject.R
import com.example.graduationproject.databinding.ChallengeCardBinding
import com.example.graduationproject.domain.entity.Challenge
import com.example.graduationproject.domain.entity.Group
import com.example.graduationproject.domain.entity.GroupAndChallenges

class ChallengesAdapter(
    private var groupAndChallengesPairs: MutableList<Pair<Group, Challenge>>,
    private val onChallengeClickListener: (Challenge) -> Unit
) :
    RecyclerView.Adapter<ChallengesAdapter.ChallengeViewHolder>() {
    class ChallengeViewHolder(
        itemView: View,
        private val onChallengeClickListener: (Challenge) -> Unit
    ) :
        RecyclerView.ViewHolder(itemView) {

        private val binding = ChallengeCardBinding.bind(itemView)

        fun onBind(pair: Pair<Group, Challenge>) {
            val (group, challenge) = pair

            binding.groupNameView.text = group.groupName
            binding.challengeNameView.text = challenge.challengeName

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




