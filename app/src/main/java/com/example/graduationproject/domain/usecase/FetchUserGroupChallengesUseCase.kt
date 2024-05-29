package com.example.graduationproject.domain.usecase

import com.example.graduationproject.di.qualifiers.Local
import com.example.graduationproject.di.qualifiers.Remote
import com.example.graduationproject.domain.entity.ChallengeStatus
import com.example.graduationproject.domain.entity.GroupAndChallenges
import com.example.graduationproject.domain.util.LoadManager
import com.example.graduationproject.domain.util.UseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class FetchUserGroupChallengesUseCase @Inject constructor(
    @Remote private val remoteLoadManager: LoadManager,
    @Local private val localLoadManager: LoadManager
) : UseCase<FetchUserGroupChallengesUseCase.Params, MutableList<GroupAndChallenges>> {
    data class Params(
        val userId: String,
        val challengeStatus: ChallengeStatus
    )

    override suspend fun invoke(params: Params): Flow<MutableList<GroupAndChallenges>> =
        flow {
            val userId = params.userId
            val challengeStatus = params.challengeStatus
            val groupAndChallenges: MutableList<GroupAndChallenges>
            var groups = localLoadManager.fetchGroupsByUserId(userId)

            if (groups.isNotEmpty()) {
                groupAndChallenges = groups.map { group ->
                    val challenges = localLoadManager.fetchChallengesByStatusAndId(
                        group.groupId,
                        challengeStatus
                    )
                    GroupAndChallenges(group, challenges)
                }.toMutableList()
            } else {
                groups = remoteLoadManager.fetchGroupsByUserId(userId)
                groupAndChallenges = groups.map { group ->
                    val challenges = remoteLoadManager.fetchChallengesByStatusAndId(
                        group.groupId,
                        challengeStatus
                    )
                    GroupAndChallenges(group, challenges)
                }.toMutableList()
            }
            emit(groupAndChallenges)
        }
}


