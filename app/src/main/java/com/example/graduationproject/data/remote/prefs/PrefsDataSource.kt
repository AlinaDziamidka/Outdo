package com.example.graduationproject.data.remote.prefs

import android.content.Context
import com.example.graduationproject.domain.entity.UserProfile
import javax.inject.Inject

class PrefsDataSourceImpl @Inject constructor(private val context: Context) : PrefsDataSource {

    override fun saveToken(token: String) {
        val prefs = context.getSharedPreferences(
            sessionPrefs, Context.MODE_PRIVATE
        )

        with(prefs.edit()) {
            putString(tokenKey, token)
            apply()
        }
    }

    override fun fetchToken(): String? {
        val prefs = context.getSharedPreferences(
            sessionPrefs, Context.MODE_PRIVATE
        )

        return prefs.getString(tokenKey, "")
    }

    override fun saveUserProfile(userProfile: UserProfile) {
        val prefs = context.getSharedPreferences(
            sessionPrefs, Context.MODE_PRIVATE
        )

        with(prefs.edit()) {
            putString(userIdKey, userProfile.userId)
            putString(userIdentity, userProfile.userIdentity)
            putString(usernameKey, userProfile.username)
            apply()
        }
    }

    companion object {
        const val sessionPrefs = "session_prefs"
        const val tokenKey = "token_key"
        const val userIdKey = "current_user_id"
        const val usernameKey = "current_username"
        const val userIdentity = "current_user_userIdentity"
    }
}

interface PrefsDataSource {
    fun saveToken(token: String)
    fun fetchToken(): String?

    fun saveUserProfile(userProfile: UserProfile)
}