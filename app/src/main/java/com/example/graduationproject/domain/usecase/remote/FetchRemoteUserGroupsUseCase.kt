package com.example.graduationproject.domain.usecase.remote

import android.util.Log
import com.example.graduationproject.data.remote.RemoteLoadManager
import com.example.graduationproject.domain.entity.Group
import com.example.graduationproject.domain.entity.GroupParticipants
import com.example.graduationproject.domain.entity.UserGroup
import com.example.graduationproject.domain.entity.UserProfile
import com.example.graduationproject.domain.repository.local.GroupLocalRepository
import com.example.graduationproject.domain.repository.local.UserGroupLocalRepository
import com.example.graduationproject.domain.repository.local.UserLocalRepository
import com.example.graduationproject.domain.repository.remote.UserGroupRemoteRepository
import com.example.graduationproject.domain.repository.remote.UserRemoteRepository
import com.example.graduationproject.domain.util.Event
import com.example.graduationproject.domain.util.UseCase
import com.example.graduationproject.domain.util.writeToLocalDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FetchRemoteUserGroupsUseCase @Inject constructor(
//    private val userGroupLocalRepository: UserGroupLocalRepository,
//    private val groupLocalRepository: GroupLocalRepository,
//    private val userGroupRemoteRepository: UserGroupRemoteRepository,
//    private val userRemoteRepository: UserRemoteRepository,
//    private val userLocalRepository: UserLocalRepository
    private val remoteLoadManager: RemoteLoadManager,
    private val localLoadManager: RemoteLoadManager
) : UseCase<FetchRemoteUserGroupsUseCase.Params, MutableList<GroupParticipants>> {
    data class Params(
        val userId: String
    )

    override suspend fun invoke(params: Params): Flow<MutableList<GroupParticipants>> =
        flow {
            val userId = params.userId
            val groups = localLoadManager.fetchGroupsByUserId(userId)

            val groupParticipants = groups.map { group ->
                val participants = remoteLoadManager.fetchUsersByGroupId(group.groupId)
                GroupParticipants(group, participants)
            }.toMutableList()

//            val userGroupsByUserId = getUserGroupsByUserId(userId)
//            val groupParticipants = userGroupsByUserId.map { userGroupByUserId ->
//                Log.d(
//                    "FetchRemoteUserGroupsUseCase",
//                    "Fetching groups for group ${userGroupByUserId.groupId}"
//                )
//                val group = getGroup(userGroupByUserId.groupId)
//                val userGroupsByGroupId = getUserGroupsByGroupId(userGroupByUserId.groupId)
//                val participants = userGroupsByGroupId.map { userGroupByGroupId ->
//                    getParticipants(userGroupByGroupId.userId)
//                }.toMutableList()
//                GroupParticipants(group, participants)
//            }.toMutableList()

            emit(groupParticipants)
        }

//    private suspend fun getUserGroupsByUserId(userId: String): List<UserGroup> =
//        withContext(Dispatchers.IO) {
//            return@withContext userGroupLocalRepository.fetchGroupsByUserId(userId)
////            if (userGroups.isNotEmpty()) {
////                return@withContext userGroups
////            } else {
////                throw Exception("UserGroups not found")
////            }
//        }
//
//    private suspend fun getGroup(groupId: String): Group = withContext(Dispatchers.IO) {
//        val group = groupLocalRepository.fetchById(groupId)
//        return@withContext group ?: throw Exception("Group not found")
//    }
//
//    private suspend fun getUserGroupsByGroupId(groupId: String): List<UserGroup> =
//        withContext(Dispatchers.IO) {
//            val event = userGroupRemoteRepository.fetchAllUsersByGroupId(groupId)
//            when (event) {
//                is Event.Success -> {
//                    Log.d("FetchRemoteUserGroupsUseCase", "Received user groups: ${event.data}")
//                    event.data.map {
//                        writeToLocalDatabase(userGroupLocalRepository::insertOne, it)
//                    }
//                    event.data
//                }
//
//                is Event.Failure -> {
//                    Log.e(
//                        "FetchRemoteUserGroupsUseCase",
//                        "Failed to fetch user groups: ${event.exception}"
//                    )
//                    throw Exception(event.exception)
//                }
//            }
//        }
//
//    private suspend fun getParticipants(userId: String): UserProfile = withContext(
//        Dispatchers.IO
//    ) {
//        val event = userRemoteRepository.fetchUserById(userId)
//        Log.d("FetchRemoteUserGroupsUseCase", "Received users: ${event}")
//        when (event) {
//            is Event.Success -> {
//                Log.d("FetchRemoteUserGroupsUseCase", "Received users: ${event.data}")
//                writeToLocalDatabase(userLocalRepository::insertOne, event.data)
//                event.data
//            }
//
//            is Event.Failure -> {
//                Log.e("FetchRemoteUserGroupsUseCase", "Failed to fetch users: ${event.exception}")
//                throw Exception(event.exception)
//            }
//        }
//    }
}