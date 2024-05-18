package com.example.graduationproject.data.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.graduationproject.domain.repository.local.ChallengeLocalRepository
import com.example.graduationproject.domain.repository.local.GroupChallengeLocalRepository
import com.example.graduationproject.domain.repository.local.GroupLocalRepository
import com.example.graduationproject.domain.repository.local.UserGroupLocalRepository
import com.example.graduationproject.domain.repository.local.UserLocalRepository
import com.example.graduationproject.domain.repository.remote.GroupChallengeRemoteRepository
import com.example.graduationproject.domain.repository.remote.GroupRemoteRepository
import com.example.graduationproject.domain.repository.remote.UserGroupRemoteRepository
import com.example.graduationproject.domain.repository.remote.UserRemoteRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class InitialLoadWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val userGroupRemoteRepository: UserGroupRemoteRepository,
    private val groupRemoteRepository: GroupRemoteRepository,
    private val groupChallengeRemoteRepository: GroupChallengeRemoteRepository,
    private val userGroupLocalRepository: UserGroupLocalRepository,
    private val groupLocalRepository: GroupLocalRepository,
    private val groupChallengeLocalRepository: GroupChallengeLocalRepository,
    private val challengeLocalRepository: ChallengeLocalRepository,
    private val userRemoteRepository: UserRemoteRepository,
    private val userLocalRepository: UserLocalRepository
    ) : CoroutineWorker(appContext, workerParams) {

        override suspend fun doWork(): Result {
            return try {
                val data = repository.fetchDataFromServer()
                repository.saveDataToLocalDatabase(data)
                Result.success()
            } catch (e: Exception) {
                Result.retry()
            }
        }
    }
}