package com.example.graduationproject.presentation.util


import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import java.io.Serializable

fun Fragment.putArguments(vararg pairs: Pair<String, Any?>): Fragment {
    val bundle = Bundle()
    for ((key, value) in pairs) {
        when (value) {
            is String -> bundle.putString(key, value)
            is Int -> bundle.putInt(key, value)
            is Boolean -> bundle.putBoolean(key, value)
            is Serializable -> bundle.putSerializable(key, value)
        }
    }
    arguments = bundle
    return this
}

fun <T : Serializable> Bundle.getSerializableCompat(key: String, clazz: Class<T>): T? {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        getSerializable(key, clazz)
    } else {
        @Suppress("DEPRECATION")
        getSerializable(key) as? T
    }
}