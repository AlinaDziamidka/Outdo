package com.example.graduationproject.data.remote.repository

import android.os.Build
import com.example.graduationproject.data.remote.api.request.DeviceRegistrationRequest
import com.example.graduationproject.data.remote.api.service.DeviceRegistrationApiService
import com.example.graduationproject.data.remote.api.util.DeviceInfoUtil
import com.example.graduationproject.data.remote.prefs.PrefsDataSource
import com.example.graduationproject.data.remote.transormer.UserDeviceTransformer
import com.example.graduationproject.domain.entity.UserDevice
import com.example.graduationproject.domain.repository.remote.DeviceRegistrationRepository
import com.example.graduationproject.domain.util.Event
import doCall
import java.util.Date
import javax.inject.Inject

class DeviceRegistrationRepositoryImpl @Inject constructor(
    private val prefsDataSource: PrefsDataSource,
    private val deviceRegistrationApiService: DeviceRegistrationApiService,
) : DeviceRegistrationRepository {

    val userDeviceTransformer = UserDeviceTransformer()

    override suspend fun registerDevice(userId: String): Event<UserDevice> {
        val deviceId = DeviceInfoUtil.getDeviceId()
        val deviceToken = DeviceInfoUtil.getDeviceToken()
        val os = "ANDROID"
        val osVersion = Build.VERSION.RELEASE
        val channels = listOf("default")
        val expiration = Date().time
        val userId = userId

        val event = doCall {
            val request = DeviceRegistrationRequest(
                deviceToken,
                deviceId,
                os,
                osVersion,
                channels,
                expiration,
                userId
            )
            return@doCall deviceRegistrationApiService.registerDevice(request)
        }

        return when (event) {
            is Event.Success -> {
                val response = event.data
                val userDevice = userDeviceTransformer.fromResponse(response)
                Event.Success(userDevice)
            }

            is Event.Failure -> {
                val error = event.exception
                Event.Failure(error)
            }
        }

    }

    override suspend fun saveRegistrationId(registrationId: String) =
        prefsDataSource.saveRegistrationId(registrationId)


    override suspend fun getRegisteredDevice(userIdQuery: String): Event<List<UserDevice>> {
        val query = "userId=\'$userIdQuery\'"
        val event = doCall {
            return@doCall deviceRegistrationApiService.getRegisteredDevice(query)
        }

        return when (event) {
            is Event.Success -> {
                val response = event.data
                val userDevices = response.map {
                    userDeviceTransformer.fromResponse(it)
                }
                Event.Success(userDevices)
            }

            is Event.Failure -> {
                val error = event.exception
                Event.Failure(error)
            }
        }
    }
}