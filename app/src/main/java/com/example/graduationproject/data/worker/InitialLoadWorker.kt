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
import kotlinx.coroutines.delay

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
                delay(SMALL_DELAY_MILLIS)
                challenges = remoteLoadManager.fetchUserChallengesByGroupId(group.groupId)
                delay(SMALL_DELAY_MILLIS)
            }
            delay(LARGE_DELAY_MILLIS)
            challenges.map { challenge ->
                remoteLoadManager.fetchAchievementsByChallengeId(challenge.challengeId)
            }
            delay(LARGE_DELAY_MILLIS)
            remoteLoadManager.fetchNotificationsByUserId(userId)
            delay(SMALL_DELAY_MILLIS)
            remoteLoadManager.fetchFriendsByUserId(userId)
            delay(LARGE_DELAY_MILLIS)
            Log.d("InitialLoadWorker", "Work completed successfully")
            Result.success()
        } catch (e: Exception) {
            Log.e("InitialLoadWorker", "Error in doWork", e)
            Result.retry()
        }
    }

    companion object {
        const val LARGE_DELAY_MILLIS = 30000L
        const val SMALL_DELAY_MILLIS = 3000L
    }
}