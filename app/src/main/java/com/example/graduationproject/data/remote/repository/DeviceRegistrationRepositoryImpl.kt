package com.example.graduationproject.data.remote.repository

import android.os.Build
import android.util.Log
import com.example.graduationproject.data.remote.api.request.DeviceRegistrationRequest
import com.example.graduationproject.data.remote.api.service.DeviceRegistrationApiService
import com.example.graduationproject.data.remote.api.util.DeviceInfoUtil
import com.example.graduationproject.data.remote.prefs.PrefsDataSource
import com.example.graduationproject.domain.repository.remote.DeviceRegistrationRepository
import com.example.graduationproject.domain.util.Event
import doCall
import javax.inject.Inject

class DeviceRegistrationRepositoryImpl @Inject constructor(
    private val prefsDataSource: PrefsDataSource,
    private val deviceRegistrationApiService: DeviceRegistrationApiService,
) : DeviceRegistrationRepository {

    override suspend fun registerDevice() {
        val deviceId = DeviceInfoUtil.getDeviceId()
        val deviceToken = DeviceInfoUtil.getDeviceToken()
        val os = "ANDROID"
        val osVersion = Build.VERSION.RELEASE
        val channels = listOf("default")
        val userToken = prefsDataSource.fetchToken()

        val event = doCall {
            val request = DeviceRegistrationRequest(
                deviceToken,
                deviceId,
                os,
                osVersion,
                channels
            )
            return@doCall deviceRegistrationApiService.registerDevice(userToken, request)
        }

        when (event) {
            is Event.Success -> {
                val response = event.data
                saveRegistrationId(response.registrationId)
            }

            is Event.Failure -> {
                Log.e(
                    "DeviceRegistrationRepositoryImpl",
                    "Error register device: ${event.exception}"
                )
            }
        }
    }

    override suspend fun saveRegistrationId(registrationId: String) {
        prefsDataSource.saveRegistrationId(registrationId)

    }

    override suspend fun getRegisteredDevice(): Event<String> {
        val registrationId = prefsDataSource.fetchRegistrationId()
        val userToken = prefsDataSource.fetchToken()
        val query = "objectId=\'$registrationId\'"
        val event = doCall {
            return@doCall deviceRegistrationApiService.getRegisteredDevice(userToken, query)
        }

        return when (event) {
            is Event.Success -> {
                val response = event.data.first()
                Event.Success(response.registrationId)
            }

            is Event.Failure -> {
                val error = event.exception
                Event.Failure(error)
            }
        }
    }

    override suspend fun getDeviceId(userIdQuery: String): Event<String> {
        val query = "user.objectId=\'$userIdQuery\'"
        val event = doCall {
            return@doCall deviceRegistrationApiService.getDeviceId(query)
        }

        return when (event) {
            is Event.Success -> {
                val response = event.data.first()
                Event.Success(response.deviceId)
            }

            is Event.Failure -> {
                val error = event.exception
                Event.Failure(error)
            }
        }
    }
}