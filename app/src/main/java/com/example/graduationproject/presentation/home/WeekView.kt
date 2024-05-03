package com.example.graduationproject.presentation.home

import android.os.CountDownTimer
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.example.graduationproject.databinding.WeeklyChallengeCardBinding
import com.example.graduationproject.domain.entity.Challenge
import java.util.Locale

class WeekView(rootView: WeeklyChallengeCardBinding){

    private lateinit var timeView: TextView
    private lateinit var countdownTimer: CountDownTimer
    private lateinit var weeklyNameView: TextView
    private lateinit var descriptionView: TextView
    private lateinit var participateAction: Button
    private lateinit var iconView: ImageView

    init {
        bindViews(rootView)
    }

    private fun bindViews(rootView: WeeklyChallengeCardBinding) {
        timeView = rootView.timeView
        weeklyNameView = rootView.weeklyChallengeNameView
        descriptionView = rootView.descriptionView
        participateAction = rootView.participateAction
        iconView = rootView.iconView
    }

    fun updateWeeklyChallenge(challenge: Challenge) {
        val endTime = challenge.endTime
        setUpTimeView(endTime)
        weeklyNameView.text = challenge.challengeName
        descriptionView.text = challenge.challengeDescription
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