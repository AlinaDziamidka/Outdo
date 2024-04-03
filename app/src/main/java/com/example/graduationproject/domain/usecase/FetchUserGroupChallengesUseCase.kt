package com.example.graduationproject.domain.usecase

import com.example.graduationproject.domain.entity.Challenge
import com.example.graduationproject.domain.entity.Group
import com.example.graduationproject.domain.entity.GroupAndChallenges
import com.example.graduationproject.domain.entity.GroupChallenge
import com.example.graduationproject.domain.entity.UserGroup
import com.example.graduationproject.domain.repository.remote.ChallengeRepository
import com.example.graduationproject.domain.repository.remote.GroupChallengeRepository
import com.example.graduationproject.domain.repository.remote.GroupRepository
import com.example.graduationproject.domain.repository.remote.UserGroupRepository
import com.example.graduationproject.domain.util.Event
import com.example.graduationproject.domain.util.UseCase
import doCall
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FetchUserGroupChallengesUseCase(
    private val userGroupRepository: UserGroupRepository,
    private val groupRepository: GroupRepository,
    private val groupChallengeRepository: GroupChallengeRepository
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

    private suspend fun getUserGroups(userId: String): List<UserGroup> {
        val event = userGroupRepository.fetchAllGroupsByUserId(userId)
        return when (event) {
            is Event.Success -> event.data
            is Event.Failure -> throw Exception(event.exception)
        }
    }

    private suspend fun getGroup(groupId: String): Group {
        val event = groupRepository.fetchGroupsByGroupId(groupId)
        return when (event) {
            is Event.Success -> event.data
            is Event.Failure -> throw Exception(event.exception)
        }
    }

    private suspend fun getCurrentChallenges(groupId: String): List<Challenge> {
        val event = groupChallengeRepository.fetchAllChallengesByGroupId(groupId)
        return when (event) {
            is Event.Success -> event.data
            is Event.Failure -> throw Exception(event.exception)
        }
    }
}

