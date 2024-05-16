package com.example.graduationproject

import android.app.Application
import coil.ImageLoader
import coil.decode.SvgDecoder
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application() {

    companion object {
        lateinit var instance: App

        var isLoaded: Boolean = false
            get() {
                return field
            }
            set(value) {
                field = value
            }
    }

    override fun onCreate() {
        super.onCreate()
        instance = this

        val imageLoader = ImageLoader.Builder(applicationContext)
            .components {
                add(SvgDecoder.Factory())
            }
            .build()

    }
}