package com.example.graduationproject.domain.usecase

import com.example.graduationproject.data.repository.ChallengeRepositoryImpl
import com.example.graduationproject.data.repository.GroupChallengeRepositoryImpl
import com.example.graduationproject.domain.entity.Challenge
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow

class FetchGroupChallengesUseCase(
    private val groupChallengeRepository: GroupChallengeRepositoryImpl,
    private val challengeRepository: ChallengeRepositoryImpl
) {

    suspend operator fun invoke(groupId: Long): Flow<List<Challenge>> {
        val groupChallenges = groupChallengeRepository.fetchAllChallengesByGroupId(groupId)
        return groupChallenges.flatMapConcat { groupChallenge ->
            flow {
                val challenges = mutableListOf<Challenge>()
                for (challenge in groupChallenge) {
                    val fetchedChallenge =
                        challengeRepository.fetchChallenge(challenge.challengeId)
                    challenges.add(fetchedChallenge)
                }
                emit(challenges)
            }
        }
    }
}