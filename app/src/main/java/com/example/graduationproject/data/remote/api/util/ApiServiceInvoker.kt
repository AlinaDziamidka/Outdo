package com.example.cartrader.data.api.util

import android.util.Log
import com.example.graduationproject.domain.util.Event
import retrofit2.Response

suspend fun <T : Any> doCall(call: suspend () -> Response<T>): Event<T> {
    Log.d("DoCall", "Making API call")
    val response = call()
    Log.d("DoCall", "Response: $response")
    return if (response.isSuccessful) {
        val body = response.body()
        if (body != null) {
            Log.d("DoCall", "API call successful: $body")
            Event.Success(body)
        } else {
            Event.Failure("Unknown error")
        }
    } else {
        val errorBody = response.errorBody()?.string()
        if (!errorBody.isNullOrBlank()) {
            val apiError = errorBody.toApiError()
            Log.d("DoCall", "API error: $apiError")
            // TODO: by default we take backendless message, but you can write any code to use you custom message.
            //  This is the right place to do it
            Event.Failure(apiError.message)
        } else {
            Event.Failure("Unknown error")
        }
    }
}