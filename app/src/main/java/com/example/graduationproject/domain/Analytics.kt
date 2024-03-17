package com.example.graduationproject.domain

interface Analytics {

    fun trackEvent(event: String)
}