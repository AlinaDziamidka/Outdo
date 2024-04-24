package com.example.graduationproject.domain.util

suspend fun <T> writeToRepository(insertFunction: suspend (T) -> Unit, data: T) =
    insertFunction(data)
