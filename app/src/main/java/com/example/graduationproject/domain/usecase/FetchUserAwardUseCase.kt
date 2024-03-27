package com.example.graduationproject.domain.usecase

import com.example.graduationproject.domain.entity.Award
import com.example.graduationproject.domain.repository.remote.AwardRepository
import com.example.graduationproject.domain.repository.remote.UserAwardRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow

class FetchUserAwardUseCase(
    private val awardRepository: AwardRepository,
    private val userAwardRepository: UserAwardRepository
) {

    suspend operator fun invoke(userId: Long): Flow<List<Award>> {
        val userAwards = userAwardRepository.fetchAllAwardsByUserId(userId)
        return userAwards.flatMapConcat { userAward ->
            flow {
                val awards = mutableListOf<Award>()
                for (award in userAward) {
                    val fetchedAward =
                        awardRepository.fetchUserAward(award.awardId)
                    awards.add(fetchedAward)
                }
                emit(awards)
            }
        }
    }
}