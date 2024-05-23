package com.example.graduationproject.data.worker

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.graduationproject.data.remote.RemoteLoadManager
import com.example.graduationproject.di.qualifiers.Remote
import com.example.graduationproject.domain.entity.UserGroup
import com.example.graduationproject.domain.repository.local.ChallengeLocalRepository
import com.example.graduationproject.domain.repository.local.GroupChallengeLocalRepository
import com.example.graduationproject.domain.repository.local.GroupLocalRepository
import com.example.graduationproject.domain.repository.local.UserGroupLocalRepository
import com.example.graduationproject.domain.repository.local.UserLocalRepository
import com.example.graduationproject.domain.repository.remote.GroupChallengeRemoteRepository
import com.example.graduationproject.domain.repository.remote.GroupRemoteRepository
import com.example.graduationproject.domain.repository.remote.UserGroupRemoteRepository
import com.example.graduationproject.domain.repository.remote.UserRemoteRepository
import com.example.graduationproject.domain.util.Event
import com.example.graduationproject.domain.util.writeToLocalDatabase
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@HiltWorker
class InitialLoadWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
//   @Remote
    private val remoteLoadManager: RemoteLoadManager
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        return try {
            val userId = inputData.getString("USER_ID")!!
            val groups = remoteLoadManager.fetchGroupsByUserId(userId)
            groups.map { group ->
                remoteLoadManager.fetchUsersByGroupId(group.groupId)
                remoteLoadManager.fetchUserChallengesByGroupId(group.groupId)
            }
            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }
}