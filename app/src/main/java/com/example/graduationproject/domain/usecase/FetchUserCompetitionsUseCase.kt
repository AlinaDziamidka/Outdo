package com.example.graduationproject.domain.usecase

import com.example.graduationproject.data.repository.CompetitionRepositoryImpl
import com.example.graduationproject.data.repository.UserCompetitionRepositoryImpl
import com.example.graduationproject.domain.entity.Competition
import com.example.graduationproject.domain.repository.CompetitionRepository
import com.example.graduationproject.domain.repository.UserCompetitionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow

class FetchUserCompetitionsUseCase(
    private val competitionRepository: CompetitionRepository,
    private val userCompetitionRepository: UserCompetitionRepository
) {
    suspend operator fun invoke(userId: Long): Flow<List<Competition>> {
        val userCompetitions = userCompetitionRepository.fetchAllCompetitionsByUserId(userId)
        return userCompetitions.flatMapConcat { userCompetition ->
            flow {
                val competitions = mutableListOf<Competition>()
                for (competition in userCompetition) {
                    val fetchedCompetition =
                        competitionRepository.fetchUserCompetition(competition.competitionId)
                    competitions.add(fetchedCompetition)
                }
                emit(competitions)
            }
        }
    }
}