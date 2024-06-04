package com.example.graduationproject.presentation.challengedetails

import android.opengl.Visibility
import android.view.View
import android.widget.TextView
import com.example.graduationproject.databinding.ChallengeDetailsCardBinding
import com.example.graduationproject.domain.entity.Challenge
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ChallengeDescriptionView(rootView: ChallengeDetailsCardBinding) {
    private lateinit var creatorNameView: TextView
    private lateinit var descriptionView: TextView
    private lateinit var startTimeView: TextView
    private lateinit var dateOfStartView: TextView
    private lateinit var finishTimeView: TextView
    private lateinit var dateOfFinishView: TextView

    init {
        bindViews(rootView)
    }

    private fun bindViews(rootView: ChallengeDetailsCardBinding) {
        creatorNameView = rootView.creatorNameView
        descriptionView = rootView.descriptionView
        startTimeView = rootView.timeOfStartView
        dateOfStartView = rootView.dateOfStartView
        finishTimeView = rootView.timeOfFinishView
        dateOfFinishView = rootView.dateOfFinishView
    }

    fun updateChallengeDescription(challenge: Challenge) {
        val challengeDescription = challenge.challengeDescription
        val endTime = challenge.endTime
        val currentLocale: Locale = Locale.getDefault()
        val (formattedFinishTime, formattedDateOfFinish) = formatDateAndTime(endTime, currentLocale)
        finishTimeView.text = formattedFinishTime
        dateOfFinishView.text = formattedDateOfFinish

        if (challengeDescription == null) {
            descriptionView.visibility = View.GONE
        } else {
            descriptionView.visibility = View.VISIBLE
            descriptionView.text = challengeDescription
        }
    }

    private fun formatDateAndTime(timestamp: Long, locale: Locale): Pair<String, String> {
        val date = Date(timestamp)
        val timeFormat = SimpleDateFormat("HH:mm:ss", locale)
        val timeFinishView = timeFormat.format(date)
        val dateFormat = SimpleDateFormat("dd MMMM", locale)
        val dateOfFinishView = dateFormat.format(date)

        return Pair(timeFinishView, dateOfFinishView)
    }
}