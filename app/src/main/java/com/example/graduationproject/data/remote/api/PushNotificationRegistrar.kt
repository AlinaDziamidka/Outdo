package com.example.graduationproject.data.remote.api

import android.content.Context
import android.widget.Toast
import com.backendless.Messaging
import com.backendless.async.callback.AsyncCallback
import com.backendless.exceptions.BackendlessFault
import com.backendless.push.DeviceRegistrationResult
import javax.inject.Inject

//class PushNotificationRegistrar @Inject constructor(private val messaging: Messaging) {
//
//    fun registerDeviceForPushNotifications(context: Context) {
//        val channels = listOf("default")
//        messaging.registerDevice(channels, object : AsyncCallback<DeviceRegistrationResult> {
//            override fun handleResponse(response: DeviceRegistrationResult?) {
//                Toast.makeText(context, "Device registered!", Toast.LENGTH_LONG).show()
//            }
//
//            override fun handleFault(fault: BackendlessFault?) {
//                Toast.makeText(context, "Error registering: ${fault?.message}", Toast.LENGTH_LONG)
//                    .show()
//            }
//        })
//    }
//}