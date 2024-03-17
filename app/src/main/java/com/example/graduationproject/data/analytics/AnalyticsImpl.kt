package com.example.graduationproject.data.analytics

import android.util.Log
import com.example.graduationproject.domain.Analytics
import javax.inject.Inject

class AnalyticsImpl @Inject constructor() : Analytics {
    override fun trackEvent(event: String) {
        Log.d("ANALYTICS", event)
    }
}