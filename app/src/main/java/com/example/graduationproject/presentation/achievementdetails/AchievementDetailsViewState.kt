package com.example.graduationproject.presentation.achievementdetails

interface AchievementDetailsViewState <out T> {

    data class Success<T>(val data: T) : AchievementDetailsViewState<T>
    data class Failure(val message: String) : AchievementDetailsViewState<Nothing>
    data object Loading : AchievementDetailsViewState<Nothing>
}