package com.example.graduationproject.domain.usecase

import com.example.graduationproject.data.repository.GroupRepositoryImpl
import com.example.graduationproject.data.repository.UserGroupRepositoryImpl
import com.example.graduationproject.domain.entity.Group
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow


class FetchUserGroupsUseCase(
    private val groupRepository: GroupRepositoryImpl,
    private val userGroupRepository: UserGroupRepositoryImpl
) {

    suspend operator fun invoke(userId: Long): Flow<List<Group>> {
        val userGroups = userGroupRepository.fetchAllGroupsByUserId(userId)
        return userGroups.flatMapConcat { userGroup ->
            flow {
                val groups = mutableListOf<Group>()
                for (group in userGroup) {
                    val fetchedGroup = groupRepository.fetchUserGroup(group.groupId)
                    groups.add(fetchedGroup)
                }
                emit(groups)
            }
        }
    }
}