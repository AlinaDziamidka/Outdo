package com.example.graduationproject.presentation.splash

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.example.graduationproject.R
import com.example.graduationproject.data.worker.InitialLoadWorker
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
//                            runWorker()
                            val intent = Intent(this@SplashView, HomeView::class.java)
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

    private fun runWorker() {
        val sharedPreferences =
            applicationContext.getSharedPreferences("session_prefs", Context.MODE_PRIVATE)
        val userId = sharedPreferences.getString("current_user_id", "  ") ?: "  "
        Log.d("SplashView", userId)
        val inputData = workDataOf("USER_ID" to userId)

        val workRequest = OneTimeWorkRequestBuilder<InitialLoadWorker>()
            .setInputData(inputData)
            .build()
        Log.d("SplashView", "Start WorkManager")
        WorkManager.getInstance(applicationContext).enqueue(workRequest)
    }
}