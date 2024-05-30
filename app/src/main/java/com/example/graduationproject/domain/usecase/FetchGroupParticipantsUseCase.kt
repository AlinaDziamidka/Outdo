package com.example.graduationproject.domain.usecase

import com.example.graduationproject.di.qualifiers.Local
import com.example.graduationproject.di.qualifiers.Remote
import com.example.graduationproject.domain.entity.UserProfile
import com.example.graduationproject.domain.util.LoadManager
import com.example.graduationproject.domain.util.UseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class FetchGroupParticipantsUseCase @Inject constructor(
    @Remote private val remoteLoadManager: LoadManager,
    @Local private val localLoadManager: LoadManager
) : UseCase<FetchGroupParticipantsUseCase.Params, MutableList<UserProfile>> {

    data class Params(
        val groupId: String
    )

    override suspend fun invoke(params: Params): Flow<MutableList<UserProfile>> =
        flow {
            val groupId = params.groupId
            val participants = localLoadManager.fetchUsersByGroupId(groupId)
                .takeIf { it.isNotEmpty() }
                ?: remoteLoadManager.fetchUsersByGroupId(groupId)
            emit(participants.toMutableList())
        }
}