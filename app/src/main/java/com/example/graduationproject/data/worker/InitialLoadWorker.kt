package com.example.graduationproject.data.worker

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
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

//@HiltWorker
//class InitialLoadWorker @AssistedInject constructor(
//    @Assisted appContext: Context,
//    @Assisted workerParams: WorkerParameters,
//    private val userGroupRemoteRepository: UserGroupRemoteRepository,
//    private val groupRemoteRepository: GroupRemoteRepository,
//    private val groupChallengeRemoteRepository: GroupChallengeRemoteRepository,
//    private val userGroupLocalRepository: UserGroupLocalRepository,
//    private val groupLocalRepository: GroupLocalRepository,
//    private val groupChallengeLocalRepository: GroupChallengeLocalRepository,
//    private val challengeLocalRepository: ChallengeLocalRepository,
//    private val userRemoteRepository: UserRemoteRepository,
//    private val userLocalRepository: UserLocalRepository
//    ) : CoroutineWorker(appContext, workerParams) {
//
//        override suspend fun doWork(): Result {
//
//            val userId = inputData.getString("USER_ID")
//            val userGroups = getUserGroups(userId)
//
//
//
//
//
//            return try {
//                val data = repository.fetchDataFromServer()
//                repository.saveDataToLocalDatabase(data)
//                Result.success()
//            } catch (e: Exception) {
//                Result.retry()
//            }
//        }
//
//    private suspend fun getUserGroups(userId: String): List<UserGroup> =
//        withContext(Dispatchers.IO) {
//            val event = userGroupRemoteRepository.fetchAllGroupsByUserId(userId)
//            when (event) {
//                is Event.Success -> {
//                    Log.d("FetchRemoteUserGroupChallengesUseCase", "Received user groups: ${event.data}")
//                    event.data.map {
//                        writeToLocalDatabase(userGroupLocalRepository::insertOne, it)
//                    }
//                    event.data
//                }
//                is Event.Failure -> {
//                    Log.e("FetchRemoteUserGroupChallengesUseCase", "Failed to fetch user groups: ${event.exception}")
//                    throw Exception(event.exception)
//                }
//            }
//
//
//
//
//        }
//
//
//
//
//    }
//}