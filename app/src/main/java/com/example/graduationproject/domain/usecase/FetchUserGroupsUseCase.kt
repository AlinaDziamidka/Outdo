package com.example.graduationproject.domain.usecase

import com.example.graduationproject.di.qualifiers.Local
import com.example.graduationproject.di.qualifiers.Remote
import com.example.graduationproject.domain.entity.GroupParticipants
import com.example.graduationproject.domain.util.LoadManager
import com.example.graduationproject.domain.util.UseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class FetchUserGroupsUseCase @Inject constructor(
    @Remote private val remoteLoadManager: LoadManager,
    @Local private val localLoadManager: LoadManager
) : UseCase<FetchUserGroupsUseCase.Params, MutableList<GroupParticipants>> {
    data class Params(
        val userId: String
    )

    override suspend fun invoke(params: Params): Flow<MutableList<GroupParticipants>> =
        flow {
            val userId = params.userId
            val groups = localLoadManager.fetchGroupsByUserId(userId)

            val groupParticipants = groups.map { group ->
                var participants = localLoadManager.fetchUsersByGroupId(group.groupId)
                if (participants.isEmpty()){
                    participants = remoteLoadManager.fetchUsersByGroupId(group.groupId)
                }
                GroupParticipants(group, participants)
            }.toMutableList()
            emit(groupParticipants)
        }
}