package com.example.graduationproject.domain.entity

data class UserDevice(
    val deviceToken: String,
    val deviceId: String,
    val os: String,
    val osVersion: String,
    val channels: List<String>? = null,
    val registrationId: String
)
