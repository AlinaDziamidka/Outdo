package com.example.graduationproject.domain.usecase

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
import com.example.graduationproject.domain.util.writeToRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FetchUserGroupChallengesUseCase @Inject constructor(
    private val userGroupRepository: UserGroupRepository,
    private val groupRepository: GroupRepository,
    private val groupChallengeRepository: GroupChallengeRepository,
    private val userGroupLocalRepository: UserGroupLocalRepository,
    private val groupLocalRepository: GroupLocalRepository,
    private val groupChallengeLocalRepository: GroupChallengeLocalRepository,
    private val challengeLocalRepository: ChallengeLocalRepository
) : UseCase<FetchUserGroupChallengesUseCase.Params, List<GroupAndChallenges>> {
    data class Params(
        val userId: String,
    )

    override suspend fun invoke(params: FetchUserGroupChallengesUseCase.Params): Flow<List<GroupAndChallenges>> =
        flow {
            val userId = params.userId
            val userGroups = getUserGroups(userId)

            val groupAndChallengesList = userGroups.map { userGroup ->
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
                    event.data.map {
                        writeToRepository(userGroupLocalRepository::insertOne, it)
                    }
                    event.data
                }

                is Event.Failure -> throw Exception(event.exception)
            }
        }

    private suspend fun getGroup(groupId: String): Group = withContext(Dispatchers.IO) {
        val event = groupRepository.fetchGroupsByGroupId(groupId)
        when (event) {
            is Event.Success -> {
                writeToRepository(groupLocalRepository::insertOne, event.data)
                event.data
            }

            is Event.Failure -> throw Exception(event.exception)
        }
    }

    private suspend fun getCurrentChallenges(groupId: String): List<Challenge> = withContext(
        Dispatchers.IO
    ) {
        val event = groupChallengeRepository.fetchAllChallengesByGroupId(groupId)
        when (event) {
            is Event.Success -> {
                event.data.map { challenge ->
                    writeToRepository(challengeLocalRepository::insertOne, challenge)
                    writeToRepository(
                        groupChallengeLocalRepository::insertOne,
                        GroupChallenge(groupId, challenge.challengeId)
                    )
                }
                event.data
            }

            is Event.Failure -> throw Exception(event.exception)
        }
    }
}


