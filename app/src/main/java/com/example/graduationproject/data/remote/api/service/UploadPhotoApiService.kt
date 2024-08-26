package com.example.graduationproject.data.remote.api.service

import com.example.graduationproject.data.remote.api.response.PhotoUploadResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface UploadPhotoApiService {
    @Multipart
    @POST("files/achievement_photo/{filename}?overwrite=false")
    suspend fun uploadFile(
        @Part file: MultipartBody.Part,
        @Path("filename") filename: String
    ): Response<PhotoUploadResponse>
}