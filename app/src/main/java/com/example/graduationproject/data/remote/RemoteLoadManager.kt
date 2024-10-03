package com.example.graduationproject.data.remote

import android.util.Log
import com.example.graduationproject.domain.entity.Achievement
import com.example.graduationproject.domain.entity.AchievementType
import com.example.graduationproject.domain.entity.Challenge
import com.example.graduationproject.domain.entity.ChallengeAchievement
import com.example.graduationproject.domain.entity.ChallengeStatus
import com.example.graduationproject.domain.entity.ChallengeType
import com.example.graduationproject.domain.entity.Group
import com.example.graduationproject.domain.entity.GroupChallenge
import com.example.graduationproject.domain.entity.UserGroup
import com.example.graduationproject.domain.entity.UserNotifications
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
import com.example.graduationproject.domain.repository.remote.AchievementRemoteRepository
import com.example.graduationproject.domain.repository.remote.ChallengeAchievementRemoteRepository
import com.example.graduationproject.domain.repository.remote.ChallengeRemoteRepository
import com.example.graduationproject.domain.repository.remote.GroupChallengeRemoteRepository
import com.example.graduationproject.domain.repository.remote.GroupRemoteRepository
import com.example.graduationproject.domain.repository.remote.UserAchievementRemoteRepository
import com.example.graduationproject.domain.repository.remote.UserFriendRemoteRepository
import com.example.graduationproject.domain.repository.remote.UserGroupRemoteRepository
import com.example.graduationproject.domain.repository.remote.UserNotificationsRemoteRepository
import com.example.graduationproject.domain.repository.remote.UserRemoteRepository
import com.example.graduationproject.domain.util.Event
import com.example.graduationproject.domain.util.LoadManager
import com.example.graduationproject.domain.util.writeToLocalDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RemoteLoadManager @Inject constructor(
    private val userGroupRemoteRepository: UserGroupRemoteRepository,
    private val groupRemoteRepository: GroupRemoteRepository,
    private val groupChallengeRemoteRepository: GroupChallengeRemoteRepository,
    private val userGroupLocalRepository: UserGroupLocalRepository,
    private val groupLocalRepository: GroupLocalRepository,
    private val groupChallengeLocalRepository: GroupChallengeLocalRepository,
    private val challengeRemoteRepository: ChallengeRemoteRepository,
    private val challengeLocalRepository: ChallengeLocalRepository,
    private val userRemoteRepository: UserRemoteRepository,
    private val userLocalRepository: UserLocalRepository,
    private val achievementRemoteRepository: AchievementRemoteRepository,
    private val achievementLocalRepository: AchievementLocalRepository,
    private val challengeAchievementRemoteRepository: ChallengeAchievementRemoteRepository,
    private val challengeAchievementLocalRepository: ChallengeAchievementsLocalRepository,
    private val userFriendRemoteRepository: UserFriendRemoteRepository,
    private val userFriendLocalRepository: UserFriendLocalRepository,
    private val userAchievementRemoteRepository: UserAchievementRemoteRepository,
    private val userAchievementLocalRepository: UserAchievementLocalRepository,
    private val userNotificationsRemoteRepository: UserNotificationsRemoteRepository,
    private val usrUserNotificationsLocalRepository: UserNotificationsLocalRepository
) : LoadManager {

    override suspend fun fetchGroupsByUserId(userId: String): List<Group> {
        val userGroups = getUserGroups(userId)
        val groups = mutableListOf<Group>()

        for (userGroup in userGroups) {
            try {
                val group = getGroup(userGroup.groupId)
                groups.add(group)
            } catch (e: Exception) {
                Log.e("RemoteLoadManager", "Error fetching group: ${userGroup.groupId}", e)
            }
        }
        return groups
    }

    private suspend fun getUserGroups(userId: String): List<UserGroup> =
        withContext(Dispatchers.IO) {
            val event = userGroupRemoteRepository.fetchAllGroupsByUserId(userId)
            when (event) {
                is Event.Success -> {
                    event.data.map {
                        writeToLocalDatabase(userGroupLocalRepository::insertOne, it)
                    }
                    event.data
                }

                is Event.Failure -> {
                    throw Exception(event.exception)
                }
            }
        }

    private suspend fun getGroup(groupId: String): Group = withContext(Dispatchers.IO) {
        val event = groupRemoteRepository.fetchGroupsByGroupId(groupId)
        when (event) {
            is Event.Success -> {
                writeToLocalDatabase(groupLocalRepository::insertOne, event.data)
                event.data
            }

            is Event.Failure -> {
                throw Exception(event.exception)
            }
        }
    }

    override suspend fun fetchUsersByGroupId(groupId: String): List<UserProfile> {
        val groupUsers = getUserGroupsByGroupId(groupId)
        val participants = mutableListOf<UserProfile>()

        for (groupUser in groupUsers) {
            try {
                val participant = getParticipants(groupUser.userId)
                participants.add(participant)
            } catch (e: Exception) {
                Log.e("RemoteLoadManager", "Error fetching user: ${groupUser.userId}", e)
            }
        }
        return participants
    }

    private suspend fun getUserGroupsByGroupId(groupId: String): List<UserGroup> =
        withContext(Dispatchers.IO) {
            val event = userGroupRemoteRepository.fetchAllUsersByGroupId(groupId)
            when (event) {
                is Event.Success -> {
                    event.data.map {
                        writeToLocalDatabase(userGroupLocalRepository::insertOne, it)
                    }
                    event.data
                }

                is Event.Failure -> {
                    throw Exception(event.exception)
                }
            }
        }

    private suspend fun getParticipants(userId: String): UserProfile = withContext(
        Dispatchers.IO
    ) {
        val event = userRemoteRepository.fetchUserById(userId)
        when (event) {
            is Event.Success -> {
                writeToLocalDatabase(userLocalRepository::insertOne, event.data)
                event.data
            }

            is Event.Failure -> {
                throw Exception(event.exception)
            }
        }
    }

    override suspend fun fetchUserChallengesByGroupId(groupId: String): List<Challenge> =
        withContext(
            Dispatchers.IO
        ) {
            val event = groupChallengeRemoteRepository.fetchAllChallengesByGroupId(groupId)
            when (event) {
                is Event.Success -> {
                    saveChallengesToLocalDatabase(event, groupId)
                    event.data
                }

                is Event.Failure -> {
                    throw Exception(event.exception)
                }
            }
        }

    override suspend fun fetchChallengesByStatusAndId(
        groupId: String, challengeStatus: ChallengeStatus
    ): List<Challenge> {
        val event = groupChallengeRemoteRepository.fetchChallengesByGroupIdANDStatus(
            groupId, challengeStatus.stringValue
        )

        return when (event) {
            is Event.Success -> {
                saveChallengesToLocalDatabase(event, groupId)
                event.data
            }

            is Event.Failure -> {
                throw Exception(event.exception)
            }
        }
    }

    private suspend fun saveChallengesToLocalDatabase(
        event: Event.Success<List<Challenge>>, groupId: String
    ) {
        event.data.map { challenge ->
            writeToLocalDatabase(challengeLocalRepository::insertOne, challenge)
            writeToLocalDatabase(
                groupChallengeLocalRepository::insertOne,
                GroupChallenge(groupId, challenge.challengeId)
            )
        }
    }

    override suspend fun fetchAchievementsByChallengeId(challengeId: String): List<Achievement> =
        withContext(
            Dispatchers.IO
        ) {
            val event =
                challengeAchievementRemoteRepository.fetchAllAchievementsByChallengeId(challengeId)
            when (event) {
                is Event.Success -> {
                    saveChallengeAchievementsToLocalDatabase(event, challengeId)
                    event.data
                }

                is Event.Failure -> {
                    throw Exception(event.exception)
                }
            }
        }

    private suspend fun saveChallengeAchievementsToLocalDatabase(
        event: Event.Success<List<Achievement>>, challengeId: String
    ) {
        event.data.map { achievement ->
            writeToLocalDatabase(achievementLocalRepository::insertOne, achievement)
            writeToLocalDatabase(
                challengeAchievementLocalRepository::insertOne,
                ChallengeAchievement(challengeId, achievement.achievementId)
            )
        }
    }

    override suspend fun fetchDailyAchievement(achievementType: AchievementType): Event<Achievement> =
        withContext(Dispatchers.IO) {
            val event =
                achievementRemoteRepository.fetchDailyAchievement(achievementType.stringValue)
            when (event) {
                is Event.Success -> {
                    writeToLocalDatabase(achievementLocalRepository::insertOne, event.data)
                    Event.Success(event.data)
                }

                is Event.Failure -> {
                    val error = event.exception
                    Event.Failure(error)
                }
            }
        }

    override suspend fun fetchWeekChallenge(challengeType: ChallengeType): Event<Challenge> =
        withContext(Dispatchers.IO) {
            val event = challengeRemoteRepository.fetchWeekChallenge(challengeType.stringValue)
            when (event) {
                is Event.Success -> {
                    writeToLocalDatabase(challengeLocalRepository::insertOne, event.data)
                    Event.Success(event.data)
                }

                is Event.Failure -> {
                    val error = event.exception
                    Event.Failure(error)
                }
            }
        }

    override suspend fun fetchFriendsByUserId(userId: String): List<UserProfile> =
        withContext(Dispatchers.IO) {
            val friends = mutableListOf<UserProfile>()
            val event = userFriendRemoteRepository.fetchFriendsByUserId(userId)
            when (event) {
                is Event.Success -> {
                    event.data.map { userFriend ->
                        writeToLocalDatabase(userFriendLocalRepository::insertOne, userFriend)
                        val friendEvent = userRemoteRepository.fetchUserById(userFriend.friendId)
                        if (friendEvent is Event.Success) {
                            friends.add(friendEvent.data)
                            writeToLocalDatabase(
                                userLocalRepository::insertOne, friendEvent.data
                            )
                        } else {
                            Event.Failure("Not found friend")
                        }
                    }
                    friends
                }

                is Event.Failure -> {
                    throw Exception(event.exception)
                }
            }
        }

    override suspend fun fetchAchievementsByUserId(userId: String): List<Achievement> = withContext(
        Dispatchers.IO
    ) {
        val achievements = mutableListOf<Achievement>()
        val event = userAchievementRemoteRepository.fetchUserAchievementByUserId(userId)

        when (event) {
            is Event.Success -> {
                event.data.map { userAchievement ->
                    writeToLocalDatabase(
                        userAchievementLocalRepository::insertOne, userAchievement
                    )
                    val achievementEvent =
                        achievementRemoteRepository.fetchAchievementById(userAchievement.achievementId)
                    if (achievementEvent is Event.Success) {
                        achievements.add(achievementEvent.data)
                        writeToLocalDatabase(
                            achievementLocalRepository::insertOne, achievementEvent.data
                        )
                    } else {
                        Event.Failure("Not found achievements")
                    }
                }
                achievements
            }

            is Event.Failure -> {
                throw Exception(event.exception)
            }
        }
    }

    override suspend fun fetchNotificationsByUserId(userId: String): List<Pair<UserProfile, Group>> =
        withContext(Dispatchers.IO) {
            val notificationPair = mutableListOf<Pair<UserProfile, Group>>()
            val event = getUserNotifications(userId)

            when (event) {
                is Event.Success -> {
                    val notifications = event.data
                    notifications.forEach { notification ->
                        val creator = getCreatorProfile(notification)
                        val group = getGroupById(notification)
                        notificationPair.add(Pair(creator, group))
                    }
                    notificationPair.toList()
                }

                is Event.Failure -> {
                    throw Exception(event.exception)
                }
            }
        }

    private suspend fun getUserNotifications(userId: String): Event<List<UserNotifications>> {
        val event = userNotificationsRemoteRepository.fetchNotificationsByUserId(userId)
        return when (event) {
            is Event.Success -> {
                event.data.map {
                    writeToLocalDatabase(usrUserNotificationsLocalRepository::insertOne, it)
                }
                Event.Success(event.data)
            }

            is Event.Failure -> {
                Event.Failure(event.exception)
            }
        }
    }

    private suspend fun getCreatorProfile(notification: UserNotifications): UserProfile {
        val creatorId = notification.userId
        return getRemoteProfile(creatorId)
    }

    private suspend fun getRemoteProfile(creatorId: String): UserProfile {
        val event = userRemoteRepository.fetchUserById(creatorId)
        return when (event) {
            is Event.Success -> {
                writeToLocalDatabase(userLocalRepository::insertOne, event.data)
                event.data
            }

            is Event.Failure -> {
                throw Exception(event.exception)
            }
        }
    }

    private suspend fun getGroupById(notification: UserNotifications): Group {
        val groupId = notification.groupId
        return getRemoteGroup(groupId)
    }

    private suspend fun getRemoteGroup(groupId: String): Group {
        val event = groupRemoteRepository.fetchGroupsByGroupId(groupId)
        return when (event) {
            is Event.Success -> {
                writeToLocalDatabase(groupLocalRepository::insertOne, event.data)
                event.data
            }

            is Event.Failure -> {
                throw Exception(event.exception)
            }
        }
    }
}
