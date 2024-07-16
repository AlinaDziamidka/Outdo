package com.example.graduationproject.presentation.createchallenge.adapter

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.graduationproject.R
import com.example.graduationproject.databinding.CreateAchievementCardBinding

class CreateAchievementAdapter(
    private var achievements: MutableList<Pair<String, String>>,
    private val onTextChanged: (Int, String, String) -> Unit
) :
    RecyclerView.Adapter<CreateAchievementAdapter.CreateAchievementViewHolder>() {

    class CreateAchievementViewHolder(
        itemView: View,
        private val onTextChanged: (Int, String, String) -> Unit
    ) : RecyclerView.ViewHolder(itemView) {

        private val binding = CreateAchievementCardBinding.bind(itemView)
        private lateinit var achievementTitle: TextView
        private lateinit var achievementNameView: EditText
        private lateinit var achievementDescriptionView: EditText

        init {
            bindViews()
        }

        private fun bindViews() {
            achievementTitle = binding.achievementView
            achievementNameView = binding.achievementNameView
            achievementDescriptionView = binding.descriptionView
        }

        fun onBind(achievement: Pair<String, String>, number: Int, position: Int) {
            val (achievementName, achievementDescription) = achievement
            achievementTitle.text =
                itemView.context.getString(
                    R.string.create_challenge_screen_achievement,
                    number.toString()
                )
            achievementNameView.setText(achievementName)
            achievementDescriptionView.setText(achievementDescription)

            // Remove existing TextWatchers to avoid duplication
            achievementNameView.removeTextChangedListener(textWatcherName)
            achievementDescriptionView.removeTextChangedListener(textWatcherDescription)

            // Set new TextWatchers
            achievementNameView.addTextChangedListener(textWatcherName)
            achievementDescriptionView.addTextChangedListener(textWatcherDescription)

            // Keep the current position
            textWatcherName.position = position
            textWatcherDescription.position = position
        }

        private val textWatcherName = object : TextWatcher {
            var position: Int = -1

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Invoke callback only if the position is valid
                if (position >= 0) {
                    onTextChanged(position, s.toString(), achievementDescriptionView.text.toString())
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        }

        private val textWatcherDescription = object : TextWatcher {
            var position: Int = -1

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Invoke callback only if the position is valid
                if (position >= 0) {
                    onTextChanged(position, achievementNameView.text.toString(), s.toString())
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        }

            // Remove existing text watchers to prevent duplicate triggers
//            achievementNameView.addTextChangedListener(null)
//            achievementDescriptionView.addTextChangedListener(null)

            // Add text watchers

//            addTextWatcher(achievementNameView, position)
//            addTextWatcher(achievementDescriptionView, position)
        }
//            achievementDescriptionView.addTextChangedListener(object : TextWatcher {
//                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
//
//                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//                    onTextChanged(position, achievementNameView.text.toString(), s.toString())
//                }
//
//                override fun afterTextChanged(s: Editable?) {}
//            })
//        }

//
//        private fun CreateAchievementViewHolder.addTextWatcher(textView: EditText, position: Int) {
//            textView.addTextChangedListener(null)
//            textView.addTextChangedListener(object : TextWatcher {
//                override fun beforeTextChanged(
//                    s: CharSequence?,
//                    start: Int,
//                    count: Int,
//                    after: Int
//                ) {
//                }
//
//                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//                    onTextChanged(
//                        position,
//                        s.toString(),
//                        textView.text.toString()
//                    )
//                }
//
//                override fun afterTextChanged(s: Editable?) {}
//            })
//        }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CreateAchievementAdapter.CreateAchievementViewHolder {
        val itemView =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.create_achievement_card, parent, false)
        return CreateAchievementAdapter.CreateAchievementViewHolder(
            itemView, onTextChanged
        )
    }

    override fun getItemCount(): Int = achievements.size

    override fun onBindViewHolder(
        holder: CreateAchievementAdapter.CreateAchievementViewHolder,
        position: Int
    ) {
        val achievement = achievements[position]
        holder.onBind(achievement, position + 1, position)
    }

    fun setAchievements(achievements: MutableList<Pair<String, String>>) {
        this.achievements = achievements
        notifyDataSetChanged()
    }

    fun addAchievement(achievement: Pair<String, String>) {
        achievements.add(achievement)
        notifyItemInserted(achievements.size - 1)
    }

    fun getAchievements(): MutableList<Pair<String, String>> = achievements

    fun removeAchievement(achievement: Pair<String, String>) {
        achievements.remove(achievement)
        notifyDataSetChanged()
    }

    fun clear() {
        achievements.clear()
        notifyDataSetChanged()
    }
}