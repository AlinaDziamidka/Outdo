package com.example.graduationproject.domain.usecase

import android.util.Log
import com.example.graduationproject.domain.entity.Achievement
import com.example.graduationproject.domain.entity.Group
import com.example.graduationproject.domain.repository.local.GroupLocalRepository
import com.example.graduationproject.domain.repository.local.UserGroupLocalRepository
import com.example.graduationproject.domain.repository.remote.GroupRemoteRepository
import com.example.graduationproject.domain.repository.remote.UserGroupRemoteRepository
import com.example.graduationproject.domain.util.Event
import com.example.graduationproject.domain.util.UseCase
import com.example.graduationproject.domain.util.writeToLocalDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CreateGroupUseCase @Inject constructor(
    private val groupRemoteRepository: GroupRemoteRepository,
    private val groupLocalRepository: GroupLocalRepository,
    private val userGroupRemoteRepository: UserGroupRemoteRepository,
    private val userGroupLocalRepository: UserGroupLocalRepository
) : UseCase<CreateGroupUseCase.Params, Group> {

    data class Params(
        val groupName: String,
        val creatorId: String,
        val groupAvatarPath: String?
    )

    override suspend fun invoke(params: Params): Flow<Group> =
        flow {
            val groupName = params.groupName
            val creatorId = params.creatorId
            val groupAvatarPath = params.groupAvatarPath
            val event = groupRemoteRepository.insertGroup(groupName, creatorId, groupAvatarPath)

            when (event) {
                is Event.Success -> {
                    val group = event.data
                    Log.d("CreateGroupUseCase", "Insert group ${group}")
                    withContext(Dispatchers.IO) {
                        writeToLocalDatabase(groupLocalRepository::insertOne, group)
                    }
                    insertUserGroup(creatorId, group)
                    emit(group)
                }

                is Event.Failure -> {
                    Log.e("CreateGroupUseCase", "Failed to insert group: ${event.exception}")
                    throw Exception(event.exception)
                }
            }

        }

    private suspend fun insertUserGroup(
        creatorId: String,
        group: Group
    ) {
        val userGroupEvent =
            userGroupRemoteRepository.insertUserGroup(creatorId, group.groupId)
        when (userGroupEvent) {
            is Event.Success -> {
                val userGroup = userGroupEvent.data
                Log.d("CreateGroupUseCase", "Insert userGroup ${userGroup}")
                withContext(Dispatchers.IO) {
                    writeToLocalDatabase(userGroupLocalRepository::insertOne, userGroup)
                }
            }

            is Event.Failure -> {
                Log.e(
                    "CreateGroupUseCase",
                    "Failed to insert userGroup: ${userGroupEvent.exception}"
                )
                throw Exception(userGroupEvent.exception)
            }
        }
    }
}