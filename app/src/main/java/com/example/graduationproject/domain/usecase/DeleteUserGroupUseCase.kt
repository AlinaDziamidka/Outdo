package com.example.graduationproject.domain.usecase

import com.example.graduationproject.domain.entity.UserGroup
import com.example.graduationproject.domain.repository.local.UserGroupLocalRepository
import com.example.graduationproject.domain.repository.remote.UserGroupRemoteRepository
import com.example.graduationproject.domain.util.Event
import com.example.graduationproject.domain.util.NonReturningUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DeleteUserGroupUseCase @Inject constructor(
    private val userGroupRemoteRepository: UserGroupRemoteRepository,
    private val userGroupLocalRepository: UserGroupLocalRepository
) : NonReturningUseCase<DeleteUserGroupUseCase.Params> {

    data class Params(
        val userId: String,
        val groupId: String
    )

    override suspend fun invoke(params: Params) {
        val userId = params.userId
        val groupId = params.groupId

        val event = userGroupRemoteRepository.deleteUserGroup(userId, groupId)
        if (event is Event.Success) {
            withContext(Dispatchers.IO) {
                val userGroup = UserGroup(userId, groupId)
                userGroupLocalRepository.deleteOne(userGroup)
            }
        }
    }
}