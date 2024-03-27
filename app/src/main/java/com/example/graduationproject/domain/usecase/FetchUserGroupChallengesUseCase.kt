package com.example.graduationproject.domain.usecase

import com.example.graduationproject.domain.entity.Challenge
import com.example.graduationproject.domain.entity.Group
import com.example.graduationproject.domain.entity.Session
import com.example.graduationproject.domain.entity.UserGroup
import com.example.graduationproject.domain.repository.remote.ChallengeRepository
import com.example.graduationproject.domain.repository.remote.GroupChallengeRepository
import com.example.graduationproject.domain.repository.remote.GroupRepository
import com.example.graduationproject.domain.repository.remote.UserGroupRepository
import com.example.graduationproject.domain.util.UseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class FetchUserGroupChallengesUseCase(
    private val userGroupRepository: UserGroupRepository,
    private val groupRepository: GroupRepository,
    private val groupChallengeRepository: GroupChallengeRepository,
    private val challengeRepository: ChallengeRepository
) : UseCase<FetchUserGroupChallengesUseCase.Params, List<Challenge>> {
    data class Params(
        val userId: String,
    )

    override suspend fun invoke(params: FetchUserGroupChallengesUseCase.Params): Flow<List<Challenge>> {
        val userId = params.userId
        val userGroups: Flow<List<UserGroup>> = userGroupRepository.fetchAllGroupsByUserId(userId)
        val groupIds: Flow<List<String>> = userGroups.map { groups ->
            groups.map { it.groupId }
        }

        val groups: Flow<List<Group>> = groupIds.flatMapConcat { groupId ->
            flow {
                val groups = mutableListOf<Group>()
                for (id in groupId) {
                    val fetchedGroups =
                        groupRepository.fetchGroupsByGroupId(id)
                    groups.add(fetchedGroups)
                }
                emit(groups)
            }
        }
    }
}