package com.example.graduationproject.presentation.home

import android.os.CountDownTimer
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.example.graduationproject.databinding.DailyAchievementCardBinding
import com.example.graduationproject.domain.entity.Achievement
import java.util.Locale

class DailyView (rootView: DailyAchievementCardBinding){
    private lateinit var timeView: TextView
    private lateinit var countdownTimer: CountDownTimer
    private lateinit var dailyNameView: TextView
    private lateinit var descriptionView: TextView
    private lateinit var participateAction: Button
    private lateinit var iconView: ImageView

    init {
        bindViews(rootView)
    }

    private fun bindViews(rootView: DailyAchievementCardBinding) {
        timeView = rootView.timeView
        dailyNameView = rootView.dailyAchievementNameView
        descriptionView = rootView.descriptionView
        participateAction = rootView.participateAction
        iconView = rootView.iconView
    }

    fun updateDailyAchievement(achievement: Achievement) {
        val endTime = achievement.endTime
        setUpTimeView(endTime)
        dailyNameView.text = achievement.achievementName
        descriptionView.text = achievement.description
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

        timeView.text = formattedTime
    }
}