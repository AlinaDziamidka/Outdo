package com.example.graduationproject.domain.usecase

import android.util.Log
import com.example.graduationproject.domain.entity.Achievement
import com.example.graduationproject.domain.entity.AchievementStatus
import com.example.graduationproject.domain.entity.Challenge
import com.example.graduationproject.domain.entity.ChallengeAchievement
import com.example.graduationproject.domain.entity.Group
import com.example.graduationproject.domain.entity.GroupChallenge
import com.example.graduationproject.domain.entity.UserProfile
import com.example.graduationproject.domain.repository.local.AchievementLocalRepository
import com.example.graduationproject.domain.repository.local.ChallengeAchievementsLocalRepository
import com.example.graduationproject.domain.repository.local.ChallengeLocalRepository
import com.example.graduationproject.domain.repository.local.GroupChallengeLocalRepository
import com.example.graduationproject.domain.repository.local.GroupLocalRepository
import com.example.graduationproject.domain.repository.local.UserAchievementLocalRepository
import com.example.graduationproject.domain.repository.local.UserGroupLocalRepository
import com.example.graduationproject.domain.repository.remote.AchievementRemoteRepository
import com.example.graduationproject.domain.repository.remote.ChallengeAchievementRemoteRepository
import com.example.graduationproject.domain.repository.remote.ChallengeRemoteRepository
import com.example.graduationproject.domain.repository.remote.GroupChallengeRemoteRepository
import com.example.graduationproject.domain.repository.remote.GroupRemoteRepository
import com.example.graduationproject.domain.repository.remote.UserAchievementRemoteRepository
import com.example.graduationproject.domain.repository.remote.UserGroupRemoteRepository
import com.example.graduationproject.domain.util.Event
import com.example.graduationproject.domain.util.UseCase
import com.example.graduationproject.domain.util.writeToLocalDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CreateChallengeUseCase @Inject constructor(
    private val challengeRemoteRepository: ChallengeRemoteRepository,
    private val challengeLocalRepository: ChallengeLocalRepository,
    private val groupChallengeRemoteRepository: GroupChallengeRemoteRepository,
    private val groupChallengeLocalRepository: GroupChallengeLocalRepository,
    private val challengeAchievementRemoteRepository: ChallengeAchievementRemoteRepository,
    private val challengeAchievementLocalRepository: ChallengeAchievementsLocalRepository,
    private val userAchievementRemoteRepository: UserAchievementRemoteRepository,
    private val userAchievementLocalRepository: UserAchievementLocalRepository,
    private val achievementRemoteRepository: AchievementRemoteRepository,
    private val achievementsLocalRepository: AchievementLocalRepository
) : UseCase<CreateChallengeUseCase.Params, Challenge> {

    data class Params(
        val groupId: String,
        val challengeName: String,
        val creatorId: String,
        val startDate: Long,
        val finishDate: Long,
        val description: String,
        val achievements: List<Pair<String, String>>,
        val friends: List<UserProfile>
    )

    override suspend fun invoke(params: Params): Flow<Challenge> =
        flow {
            val groupId = params.groupId
            val challengeName = params.challengeName
            val creatorId = params.creatorId
            val startDate = params.startDate
            val finishDate = params.finishDate
            val description = params.description
            val achievements = params.achievements
            val friends = params.friends

            val event = challengeRemoteRepository.insertChallenge(
                challengeName,
                creatorId,
                description,
                finishDate,
                startDate
            )

            when (event) {
                is Event.Success -> {
                    val challenge = event.data
                    Log.d("CreateChallengeUseCase", "Insert challenge ${challenge}")
                    withContext(Dispatchers.IO) {
                        writeToLocalDatabase(challengeLocalRepository::insertOne, challenge)
                    }
                    val insertedGroupChallengeEvent = insertGroupChallenge(groupId, challenge)
                    if (insertedGroupChallengeEvent is Event.Success) {
                        achievements.forEach { achievement ->
                            insertAchievement(challenge, friends, achievement)
                        }
                        emit(challenge)
                    } else {
                        Log.e(
                            "CreateGroupUseCase",
                            "Failed to insert challenge: ${insertedGroupChallengeEvent}"
                        )
                        throw Exception(insertedGroupChallengeEvent.toString())
                    }
                }

                is Event.Failure -> {
                    Log.e("CreateGroupUseCase", "Failed to insert challenge: ${event.exception}")
                    throw Exception(event.exception)
                }
            }

        }

    private suspend fun insertAchievement(
        challenge: Challenge,
        friends: List<UserProfile>,
        achievement: Pair<String, String>
    ): Event<Achievement> {
        val achievementEvent =
            achievementRemoteRepository.insertAchievement(
                achievement.first,
                achievement.second,
                challenge.endTime
            )

        return when (achievementEvent) {
            is Event.Success -> {
                val response = achievementEvent.data
                Log.d("CreateChallengeUseCase", "Insert achievement ${response}")
                withContext(Dispatchers.IO) {
                    writeToLocalDatabase(achievementsLocalRepository::insertOne, response)
                }
                insertChallengeAchievement(challenge.challengeId, response.achievementId)
                insertUserAchievement(response.achievementId, friends)
                Event.Success(response)
            }

            is Event.Failure -> {
                Log.e(
                    "CreateChallengeUseCase",
                    "Failed to insert achievement: ${achievementEvent.exception}"
                )
                throw Exception(achievementEvent.exception)
            }
        }
    }

    private suspend fun insertUserAchievement(achievementId: String, friends: List<UserProfile>) {
        friends.forEach { friend ->
            val userAchievementEvent =
                userAchievementRemoteRepository.insertUserAchievements(friend.userId, achievementId)
            when (userAchievementEvent) {
                is Event.Success -> {
                    val userAchievement = userAchievementEvent.data
                    withContext(Dispatchers.IO) {
                        writeToLocalDatabase(
                            userAchievementLocalRepository::insertOne,
                            userAchievement
                        )
                    }
                }

                is Event.Failure -> {
                    Log.e(
                        "CreateChallengeUseCase",
                        "Failed to insert userAchievement: ${userAchievementEvent.exception}"
                    )
                    throw Exception(userAchievementEvent.exception)
                }
            }
        }
    }

    private suspend fun insertChallengeAchievement(challengeId: String, achievementId: String) {
        val challengeAchievementEvent =
            challengeAchievementRemoteRepository.insertChallengeAchievement(
                challengeId,
                achievementId
            )
        when (challengeAchievementEvent) {
            is Event.Success -> {
                val challengeAchievement = challengeAchievementEvent.data
                withContext(Dispatchers.IO) {
                    writeToLocalDatabase(
                        challengeAchievementLocalRepository::insertOne,
                        challengeAchievement
                    )
                }
            }

            is Event.Failure -> {
                Log.e(
                    "CreateChallengeUseCase",
                    "Failed to insert challengeAchievement: ${challengeAchievementEvent.exception}"
                )
                throw Exception(challengeAchievementEvent.exception)
            }
        }
    }


    private suspend fun insertGroupChallenge(
        groupId: String,
        challenge: Challenge
    ): Event<GroupChallenge> {
        val groupChallengeEvent =
            groupChallengeRemoteRepository.insertGroupChallenge(groupId, challenge.challengeId)
        return when (groupChallengeEvent) {
            is Event.Success -> {
                val groupChallenge = groupChallengeEvent.data
                Log.d("CreateChallengeUseCase", "Insert groupChallenge ${groupChallenge}")
                withContext(Dispatchers.IO) {
                    writeToLocalDatabase(groupChallengeLocalRepository::insertOne, groupChallenge)
                }
                Event.Success(groupChallenge)
            }

            is Event.Failure -> {
                Log.e(
                    "CreateChallengeUseCase",
                    "Failed to insert groupChallenge: ${groupChallengeEvent.exception}"
                )
                throw Exception(groupChallengeEvent.exception)
            }
        }
    }
}