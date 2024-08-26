package com.example.graduationproject.domain.repository.remote

import com.example.graduationproject.domain.util.Event
import java.io.File

interface PhotoUploadRemoteRepository {

    suspend fun uploadFile(file: File): Event<String>
}