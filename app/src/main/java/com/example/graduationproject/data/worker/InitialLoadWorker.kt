package com.example.graduationproject.data.worker

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.graduationproject.di.qualifiers.Remote
import com.example.graduationproject.domain.entity.Challenge
import com.example.graduationproject.domain.util.LoadManager
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class InitialLoadWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    @Remote private val remoteLoadManager: LoadManager
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        return try {
            val userId = inputData.getString("USER_ID")!!
            Log.d("InitialLoadWorker", "UserID: $userId")
            val groups = remoteLoadManager.fetchGroupsByUserId(userId)
            var challenges = listOf<Challenge>()
            groups.map { group ->
                remoteLoadManager.fetchUsersByGroupId(group.groupId)
                challenges = remoteLoadManager.fetchUserChallengesByGroupId(group.groupId)
            }
            challenges.map { challenge ->
                remoteLoadManager.fetchAchievementsByChallengeId(challenge.challengeId)
            }
            Log.d("InitialLoadWorker", "Work completed successfully")
            Result.success()
        } catch (e: Exception) {
            Log.e("InitialLoadWorker", "Error in doWork", e)
            Result.retry()
        }
    }
}