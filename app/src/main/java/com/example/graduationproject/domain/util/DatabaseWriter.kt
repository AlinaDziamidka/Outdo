package com.example.graduationproject.domain.util

suspend fun <T> writeToLocalDatabase(insertFunction: suspend (T) -> Unit, data: T) =
    insertFunction(data)
