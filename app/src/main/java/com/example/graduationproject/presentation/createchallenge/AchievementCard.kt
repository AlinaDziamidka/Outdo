package com.example.graduationproject.presentation.createchallenge

import android.content.Context
import android.widget.EditText
import android.widget.TextView
import androidx.core.widget.doAfterTextChanged
import com.example.graduationproject.R
import com.example.graduationproject.databinding.CreateAchievementCardBinding

class AchievementCard(
    private val context: Context,
    private val binding: CreateAchievementCardBinding,
    private val number: Int
) {
    private val achievementTitle: TextView = binding.achievementView
    private val achievementName: EditText = binding.achievementNameView
    private val description: EditText = binding.descriptionView

    private var title: String = ""
    private var descriptionText: String = ""

    init {
        bindViews()
    }

    private fun bindViews() {
        achievementTitle.text =
            context.getString(R.string.create_challenge_screen_achievement, number.toString())
        achievementName.setText(title)
        description.setText(descriptionText)

        achievementName.doAfterTextChanged {
            title = it.toString()
        }
        description.doAfterTextChanged {
            descriptionText = it.toString()
        }
    }

    fun updateData(title: String, descriptionText: String) {
        this.title = title
        this.descriptionText = descriptionText
        achievementName.setText(title)
        description.setText(descriptionText)
    }

    fun getData(): Pair<String, String> {
        return title to descriptionText
    }

    val rootView: CreateAchievementCardBinding
        get() = binding
}