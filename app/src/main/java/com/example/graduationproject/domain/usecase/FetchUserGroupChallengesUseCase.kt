package com.example.graduationproject.domain.usecase

import android.util.Log
import com.example.graduationproject.domain.entity.Challenge
import com.example.graduationproject.domain.entity.Group
import com.example.graduationproject.domain.entity.GroupAndChallenges
import com.example.graduationproject.domain.entity.UserGroup
import com.example.graduationproject.domain.repository.remote.GroupChallengeRepository
import com.example.graduationproject.domain.repository.remote.GroupRepository
import com.example.graduationproject.domain.repository.remote.UserGroupRepository
import com.example.graduationproject.domain.util.Event
import com.example.graduationproject.domain.util.UseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class FetchUserGroupChallengesUseCase @Inject constructor(
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
            Log.d(
                "FetchUserGroupChallengesUseCase",
                "invoke: Fetching user groups for userId: $userId"
            )
            val userGroups = getUserGroups(userId)
            Log.d("FetchUserGroupChallengesUseCase", "invoke: Fetched user groups: $userGroups")

            val groupAndChallengesList = userGroups.map { userGroup ->
                val group = getGroup(userGroup.groupId)
                Log.d(
                    "FetchUserGroupChallengesUseCase",
                    "invoke: Fetching group for groupId: $userGroup.groupId"
                )
                Log.d("FetchUserGroupChallengesUseCase", "invoke: Fetched group: $group")

                Log.d(
                    "FetchUserGroupChallengesUseCase",
                    "invoke: Fetching challenges for groupId: $userGroup.groupId"
                )
                val challenges = getCurrentChallenges(userGroup.groupId)
                Log.d("FetchUserGroupChallengesUseCase", "invoke: Fetched challenges: $challenges")
                GroupAndChallenges(group, challenges)
            }.toList()

            emit(groupAndChallengesList)
        }

    private suspend fun getUserGroups(userId: String): List<UserGroup> {
        Log.d(
            "FetchUserGroupChallengesUseCase",
            "getUserGroups: Fetching user groups for userId: $userId"
        )
        val event = userGroupRepository.fetchAllGroupsByUserId(userId)
        return when (event) {
            is Event.Success -> {
                Log.d(
                    "FetchUserGroupChallengesUseCase",
                    "getUserGroups: Fetched user groups: ${event.data}"
                )
                event.data
            }

            is Event.Failure -> throw Exception(event.exception)
        }
    }

    private suspend fun getGroup(groupId: String): Group {
        Log.d("FetchUserGroupChallengesUseCase", "getGroup: Fetching group for groupId: $groupId")
        val event = groupRepository.fetchGroupsByGroupId(groupId)
        return when (event) {
            is Event.Success -> {
                Log.d("FetchUserGroupChallengesUseCase", "getGroup: Fetched group: ${event.data}")
                event.data
            }

            is Event.Failure -> throw Exception(event.exception)
        }
    }

    private suspend fun getCurrentChallenges(groupId: String): List<Challenge> {
        Log.d(
            "FetchUserGroupChallengesUseCase",
            "getCurrentChallenges: Fetching challenges for groupId: $groupId"
        )
        val event = groupChallengeRepository.fetchAllChallengesByGroupId(groupId)
        return when (event) {
            is Event.Success -> {
                Log.d(
                    "FetchUserGroupChallengesUseCase",
                    "getCurrentChallenges: Fetched challenges: ${event.data}"
                )
                event.data
            }

            is Event.Failure -> throw Exception(event.exception)
        }
    }
}

