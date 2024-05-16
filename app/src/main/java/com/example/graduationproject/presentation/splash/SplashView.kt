package com.example.graduationproject.presentation.splash

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.graduationproject.App
import com.example.graduationproject.R
import com.example.graduationproject.presentation.home.HomeView
import com.example.graduationproject.presentation.signin.SignInView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SplashView : AppCompatActivity(R.layout.activity_splash) {

    private val viewModel: SplashViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        observeSplashState()
    }

    private fun observeSplashState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.viewState.collect {
                    Log.d("SplashView", "Received view state: $it")
                    when (it) {
                        SplashViewState.Success -> {
                            Log.d("SplashView", "Navigating to HomeView")
                            App.isLoaded = true
                            val intent = Intent(this@SplashView, HomeView::class.java)
//                                .apply {
//                                putExtra("loadedStatus", true)
//                            }
                            startActivity(intent)
                            finish()
                        }

                        SplashViewState.Failure -> {
                            Log.d("SplashView", "Navigation to SignInView due to failure")
                            startActivity(Intent(this@SplashView, SignInView::class.java))
                            finish()
                        }

                        SplashViewState.Loading -> {
                            Log.d("SplashView", "SplashViewState is Loading")
                        }
                    }
                }
            }
        }
    }
}