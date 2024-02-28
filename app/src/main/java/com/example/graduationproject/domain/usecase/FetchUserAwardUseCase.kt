package com.example.graduationproject.domain.usecase

import com.example.graduationproject.data.repository.AwardRepositoryImpl
import com.example.graduationproject.data.repository.UserAwardRepositoryImpl
import com.example.graduationproject.domain.entity.Award
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow

class FetchUserAwardUseCase(
    private val awardRepository: AwardRepositoryImpl,
    private val userAwardRepository: UserAwardRepositoryImpl
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