package com.example.graduationproject.presentation.challengedetails.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.graduationproject.R
import com.example.graduationproject.databinding.AchievementCardBinding
import com.example.graduationproject.domain.entity.Achievement

class AchievementsAdapter(
    private var achievementsList: MutableList<Achievement>,
    private val onAchievementClickListener: (Achievement) -> Unit
) :
    RecyclerView.Adapter<AchievementsAdapter.AchievementsViewHolder>() {

    class AchievementsViewHolder(
        itemView: View,
        private val onAchievementClickListener: (Achievement) -> Unit
    ) : RecyclerView.ViewHolder(itemView) {

        private val binding = AchievementCardBinding.bind(itemView)
        private lateinit var achievementNameView: TextView

        init {
            bindViews()
        }

        private fun bindViews() {
            achievementNameView = binding.achievementNameView
        }

        fun onBind(achievement: Achievement) {
            achievementNameView.text = achievement.achievementName
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AchievementsAdapter.AchievementsViewHolder {
        val itemView =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.achievement_card, parent, false)
        return AchievementsAdapter.AchievementsViewHolder(
            itemView,
            onAchievementClickListener
        )
    }

    override fun getItemCount(): Int = achievementsList.size

    override fun onBindViewHolder(
        holder: AchievementsAdapter.AchievementsViewHolder,
        position: Int
    ) {
        holder.onBind(achievementsList[position])
    }

    fun setAchievements(achievements: MutableList<Achievement>) {
        this.achievementsList = achievements
        notifyDataSetChanged()
    }

    fun addAchievement(achievement: Achievement) {
        achievementsList.add(achievement)
        notifyDataSetChanged()
    }

    fun removeAchievement(achievement: Achievement) {
        achievementsList.add(achievement)
        notifyDataSetChanged()
    }

    fun clear() {
        achievementsList.clear()
        notifyDataSetChanged()
    }
}