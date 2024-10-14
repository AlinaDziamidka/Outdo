package com.example.graduationproject.domain.usecase

import android.util.Log
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
                .takeIf {
                    it.isNotEmpty()
                }
                ?: remoteLoadManager.fetchGroupsByUserId(userId)

            val groupParticipants = groups.map { group ->
                val participants = localLoadManager.fetchUsersByGroupId(group.groupId)
                    .takeIf {
                        it.isNotEmpty() }
                    ?: remoteLoadManager.fetchUsersByGroupId(group.groupId)
                GroupParticipants(group, participants)
            }.toMutableList()
            emit(groupParticipants)
        }
}