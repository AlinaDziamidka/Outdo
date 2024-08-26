package com.example.graduationproject.data.remote.repository

import com.example.graduationproject.data.remote.api.service.UploadPhotoApiService
import com.example.graduationproject.domain.repository.remote.PhotoUploadRemoteRepository
import com.example.graduationproject.domain.util.Event
import doCall
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.util.UUID
import javax.inject.Inject

class PhotoUploadRemoteRepositoryImpl @Inject constructor(private val photoApiService: UploadPhotoApiService) :
    PhotoUploadRemoteRepository {

    override suspend fun uploadFile(file: File): Event<String> {

        val randomFileName = "${UUID.randomUUID()}.${file.extension}"
        val requestBody = file.asRequestBody("multipart/form-data".toMediaTypeOrNull())
        val multipartBody = MultipartBody.Part.createFormData("upload", randomFileName, requestBody)

        val event = doCall {
            return@doCall photoApiService.uploadFile(multipartBody, randomFileName)
        }

        return when (event) {
            is Event.Success -> {
                val response = event.data
                Event.Success(response.fileUrl)
            }

            is Event.Failure -> {
                val error = event.exception
                Event.Failure(error)
            }
        }
    }
}