package com.example.graduationproject.data.local

import android.util.Log
import com.example.graduationproject.domain.entity.Achievement
import com.example.graduationproject.domain.entity.AchievementType
import com.example.graduationproject.domain.entity.Challenge
import com.example.graduationproject.domain.entity.ChallengeStatus
import com.example.graduationproject.domain.entity.ChallengeType
import com.example.graduationproject.domain.entity.Group
import com.example.graduationproject.domain.entity.UserProfile
import com.example.graduationproject.domain.repository.local.AchievementLocalRepository
import com.example.graduationproject.domain.repository.local.ChallengeAchievementsLocalRepository
import com.example.graduationproject.domain.repository.local.ChallengeLocalRepository
import com.example.graduationproject.domain.repository.local.GroupChallengeLocalRepository
import com.example.graduationproject.domain.repository.local.GroupLocalRepository
import com.example.graduationproject.domain.repository.local.UserAchievementLocalRepository
import com.example.graduationproject.domain.repository.local.UserFriendLocalRepository
import com.example.graduationproject.domain.repository.local.UserGroupLocalRepository
import com.example.graduationproject.domain.repository.local.UserLocalRepository
import com.example.graduationproject.domain.repository.local.UserNotificationsLocalRepository
import com.example.graduationproject.domain.util.Event
import com.example.graduationproject.domain.util.LoadManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LocalLoadManager @Inject constructor(
    private val userGroupLocalRepository: UserGroupLocalRepository,
    private val groupLocalRepository: GroupLocalRepository,
    private val groupChallengeLocalRepository: GroupChallengeLocalRepository,
    private val challengeLocalRepository: ChallengeLocalRepository,
    private val userLocalRepository: UserLocalRepository,
    private val achievementLocalRepository: AchievementLocalRepository,
    private val challengeAchievementsLocalRepository: ChallengeAchievementsLocalRepository,
    private val achievementsLocalRepository: AchievementLocalRepository,
    private val userFriendLocalRepository: UserFriendLocalRepository,
    private val userAchievementLocalRepository: UserAchievementLocalRepository,
    private val userNotificationsLocalRepository: UserNotificationsLocalRepository
) : LoadManager {
    override suspend fun fetchGroupsByUserId(userId: String): List<Group> =
        withContext(Dispatchers.IO) {
            val userGroups = userGroupLocalRepository.fetchGroupsByUserId(userId)
            val groups = mutableListOf<Group>()

            for (userGroup in userGroups) {
                try {
                    val group = groupLocalRepository.fetchById(userGroup.groupId)
                    groups.add(group)
                } catch (e: Exception) {
                    Log.e("LocalLoadManager", "Error fetching group: ${userGroup.groupId}", e)
                }
            }
            return@withContext groups
        }

    override suspend fun fetchUsersByGroupId(groupId: String): List<UserProfile> =
        withContext(Dispatchers.IO) {
            val groupUsers = userGroupLocalRepository.fetchUsersByGroupId(groupId)
            val participants = mutableListOf<UserProfile>()

            for (groupUser in groupUsers) {
                try {
                    val participant = userLocalRepository.fetchById(groupUser.userId)
                    participants.add(participant)
                } catch (e: Exception) {
                    Log.e("LocalLoadManager", "Error fetching user: ${groupUser.userId}", e)
                }
            }
            return@withContext participants
        }

    override suspend fun fetchUserChallengesByGroupId(groupId: String): List<Challenge> =
        withContext(
            Dispatchers.IO
        ) {
            val groupChallenges = groupChallengeLocalRepository.fetchChallengesByGroupId(groupId)
            return@withContext groupChallenges.map { groupChallenge ->
                challengeLocalRepository.fetchById(groupChallenge.challengeId)
            }
        }

    override suspend fun fetchDailyAchievement(achievementType: AchievementType): Event<Achievement> =
        withContext(Dispatchers.IO) {
            return@withContext achievementLocalRepository.fetchDailyAchievement(achievementType.stringValue)
        }

    override suspend fun fetchWeekChallenge(challengeType: ChallengeType): Event<Challenge> =
        withContext(Dispatchers.IO) {
            return@withContext challengeLocalRepository.fetchWeekChallenge(challengeType.stringValue)
        }

    override suspend fun fetchChallengesByStatusAndId(
        groupId: String,
        challengeStatus: ChallengeStatus
    ): List<Challenge> =
        withContext(
            Dispatchers.IO
        ) {
            val groupChallenges = groupChallengeLocalRepository.fetchChallengesByGroupId(groupId)
            val challenges = mutableListOf<Challenge>()

            for (groupChallenge in groupChallenges) {
                try {
                    val event = challengeLocalRepository.fetchChallengeSByStatusAndId(
                        groupChallenge.challengeId,
                        challengeStatus.stringValue
                    )
                    if (event is Event.Success)
                        challenges.add(event.data)
                } catch (e: Exception) {
                    Log.e("LocalLoadManager", "Error fetching challenges", e)
                }
            }
            return@withContext challenges
        }

    override suspend fun fetchAchievementsByChallengeId(challengeId: String): List<Achievement> =
        withContext(
            Dispatchers.IO
        ) {
            val challengeAchievements =
                challengeAchievementsLocalRepository.fetchAchievementsByChallengeId(challengeId)
            return@withContext challengeAchievements.map { challengeAchievement ->
                achievementsLocalRepository.fetchById(challengeAchievement.achievementId)
            }
        }

    override suspend fun fetchFriendsByUserId(userId: String): List<UserProfile> =
        withContext(
            Dispatchers.IO
        ) {
            val userFriends =
                userFriendLocalRepository.fetchFriendsByUserId(userId)
            return@withContext userFriends.map { userFriend ->
                userLocalRepository.fetchById(userFriend.friendId)
            }
        }

    override suspend fun fetchAchievementsByUserId(userId: String): List<Achievement> =
        withContext(
            Dispatchers.IO
        ) {
            val userAchievements = userAchievementLocalRepository.fetchAchievementsByUserId(userId)
            return@withContext userAchievements.map { userAchievement ->
                achievementLocalRepository.fetchById(userAchievement.achievementId)
            }
        }

    override suspend fun fetchNotificationsByUserId(userId: String): List<Pair<UserProfile, Group>> =
        withContext(Dispatchers.IO) {
            val notificationPair = mutableListOf<Pair<UserProfile, Group>>()
            val userNotifications = userNotificationsLocalRepository.fetchAll()
            userNotifications.sortedBy { it.created }
            userNotifications.forEach { notification ->
                val creator = userLocalRepository.fetchById(notification.userId)
                val group = groupLocalRepository.fetchById(notification.groupId)
                if (creator != null && group != null) {
                    notificationPair.add(Pair(creator, group))
                }
            }
            return@withContext notificationPair.toList()
        }
}
