package com.example.graduationproject.data.remote.prefs

import android.content.Context
import android.util.Log
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
            putString(userIdentity, userProfile.userEmail)
            putString(usernameKey, userProfile.username)
            apply()
        }
    }

    override fun saveRegistrationId(registrationId: String) {
        val prefs = context.getSharedPreferences(
            sessionPrefs, Context.MODE_PRIVATE
        )

        with(prefs.edit()) {
            putString(userDeviceId, registrationId)
            apply()
        }
    }

    override fun fetchRegistrationId(): String? {
        val prefs = context.getSharedPreferences(
            sessionPrefs, Context.MODE_PRIVATE
        )
        val registrationId = prefs.getString(userDeviceId, null)
        return registrationId
    }

    companion object {
        const val sessionPrefs = "session_prefs"
        const val tokenKey = "token_key"
        const val userIdKey = "current_user_id"
        const val usernameKey = "current_username"
        const val userIdentity = "current_user_userIdentity"
        const val userDeviceId = "current_user_device_id"
    }
}

interface PrefsDataSource {
    fun saveToken(token: String)
    fun fetchToken(): String?

    fun saveUserProfile(userProfile: UserProfile)
    fun saveRegistrationId (registrationId: String)
    fun fetchRegistrationId(): String?
}

