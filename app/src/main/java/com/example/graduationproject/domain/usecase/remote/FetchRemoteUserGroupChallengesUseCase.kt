package com.example.graduationproject.domain.usecase.remote

import android.util.Log
import com.example.graduationproject.domain.entity.Challenge
import com.example.graduationproject.domain.entity.Group
import com.example.graduationproject.domain.entity.GroupAndChallenges
import com.example.graduationproject.domain.entity.GroupChallenge
import com.example.graduationproject.domain.entity.UserGroup
import com.example.graduationproject.domain.repository.local.ChallengeLocalRepository
import com.example.graduationproject.domain.repository.local.GroupChallengeLocalRepository
import com.example.graduationproject.domain.repository.local.GroupLocalRepository
import com.example.graduationproject.domain.repository.local.UserGroupLocalRepository
import com.example.graduationproject.domain.repository.remote.GroupChallengeRepository
import com.example.graduationproject.domain.repository.remote.GroupRepository
import com.example.graduationproject.domain.repository.remote.UserGroupRepository
import com.example.graduationproject.domain.util.Event
import com.example.graduationproject.domain.util.UseCase
import com.example.graduationproject.domain.util.writeToLocalDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FetchRemoteUserGroupChallengesUseCase @Inject constructor(
    private val userGroupRepository: UserGroupRepository,
    private val groupRepository: GroupRepository,
    private val groupChallengeRepository: GroupChallengeRepository,
    private val userGroupLocalRepository: UserGroupLocalRepository,
    private val groupLocalRepository: GroupLocalRepository,
    private val groupChallengeLocalRepository: GroupChallengeLocalRepository,
    private val challengeLocalRepository: ChallengeLocalRepository
) : UseCase<FetchRemoteUserGroupChallengesUseCase.Params, List<GroupAndChallenges>> {
    data class Params(
        val userId: String,
    )

    override suspend fun invoke(params: Params): Flow<List<GroupAndChallenges>> =
        flow {
            val userId = params.userId
            val userGroups = getUserGroups(userId)

            val groupAndChallengesList = userGroups.map { userGroup ->
                Log.d("FetchRemoteUserGroupChallengesUseCase", "Fetching group and challenges for group ${userGroup.groupId}")
                val group = getGroup(userGroup.groupId)
                val challenges = getCurrentChallenges(userGroup.groupId)
                GroupAndChallenges(group, challenges)
            }.toList()

            emit(groupAndChallengesList)
        }

    private suspend fun getUserGroups(userId: String): List<UserGroup> =
        withContext(Dispatchers.IO) {
            val event = userGroupRepository.fetchAllGroupsByUserId(userId)
            when (event) {
                is Event.Success -> {
                    Log.d("FetchRemoteUserGroupChallengesUseCase", "Received user groups: ${event.data}")
                    event.data.map {
                        writeToLocalDatabase(userGroupLocalRepository::insertOne, it)
                    }
                    event.data
                }

                is Event.Failure -> {
                    Log.e("FetchRemoteUserGroupChallengesUseCase", "Failed to fetch user groups: ${event.exception}")
                    throw Exception(event.exception)
                }
            }
        }

    private suspend fun getGroup(groupId: String): Group = withContext(Dispatchers.IO) {
        val event = groupRepository.fetchGroupsByGroupId(groupId)
        when (event) {
            is Event.Success -> {
                Log.d("FetchRemoteUserGroupChallengesUseCase", "Received group: ${event.data}")
                writeToLocalDatabase(groupLocalRepository::insertOne, event.data)
                event.data
            }

            is Event.Failure -> {
                Log.e("FetchRemoteUserGroupChallengesUseCase", "Failed to fetch group: ${event.exception}")
                throw Exception(event.exception)
            }
        }
    }

    private suspend fun getCurrentChallenges(groupId: String): List<Challenge> = withContext(
        Dispatchers.IO
    ) {
        val event = groupChallengeRepository.fetchAllChallengesByGroupId(groupId)
        Log.d("FetchRemoteUserGroupChallengesUseCase", "Received challenges: ${event}")
        when (event) {
            is Event.Success -> {
                Log.d("FetchRemoteUserGroupChallengesUseCase", "Received challenges: ${event.data}")
                saveToLocalDatabase(event, groupId)
                event.data
            }

            is Event.Failure -> {
                Log.e("FetchRemoteUserGroupChallengesUseCase", "Failed to fetch challenges: ${event.exception}")
                throw Exception(event.exception)
            }
        }
    }

    private suspend fun saveToLocalDatabase(
        event: Event.Success<List<Challenge>>,
        groupId: String
    ) {
        event.data.map { challenge ->
            writeToLocalDatabase(challengeLocalRepository::insertOne, challenge)
            writeToLocalDatabase(
                groupChallengeLocalRepository::insertOne,
                GroupChallenge(groupId, challenge.challengeId)
            )
        }
    }
}

