package com.example.graduationproject.data.remote

import android.util.Log
import com.example.graduationproject.domain.entity.Achievement
import com.example.graduationproject.domain.entity.AchievementType
import com.example.graduationproject.domain.entity.Challenge
import com.example.graduationproject.domain.entity.ChallengeType
import com.example.graduationproject.domain.entity.Group
import com.example.graduationproject.domain.entity.GroupChallenge
import com.example.graduationproject.domain.entity.UserGroup
import com.example.graduationproject.domain.entity.UserProfile
import com.example.graduationproject.domain.repository.local.AchievementLocalRepository
import com.example.graduationproject.domain.repository.local.ChallengeLocalRepository
import com.example.graduationproject.domain.repository.local.GroupChallengeLocalRepository
import com.example.graduationproject.domain.repository.local.GroupLocalRepository
import com.example.graduationproject.domain.repository.local.UserGroupLocalRepository
import com.example.graduationproject.domain.repository.local.UserLocalRepository
import com.example.graduationproject.domain.repository.remote.AchievementRemoteRepository
import com.example.graduationproject.domain.repository.remote.ChallengeRemoteRepository
import com.example.graduationproject.domain.repository.remote.GroupChallengeRemoteRepository
import com.example.graduationproject.domain.repository.remote.GroupRemoteRepository
import com.example.graduationproject.domain.repository.remote.UserGroupRemoteRepository
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
    private val achievementLocalRepository: AchievementLocalRepository
) : LoadManager {

    override suspend fun fetchGroupsByUserId(userId: String): List<Group> {
        val userGroups = getUserGroups(userId)
        val groups = mutableListOf<Group>()

        for (userGroup in userGroups) {
            Log.d("RemoteLoadManager", "Fetching groups for group ${userGroup.groupId}")
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
                    Log.d("RemoteLoadManager", "Received user groups: ${event.data}")
                    event.data.map {
                        writeToLocalDatabase(userGroupLocalRepository::insertOne, it)
                    }
                    event.data
                }

                is Event.Failure -> {
                    Log.e("RemoteLoadManager", "Failed to fetch user groups: ${event.exception}")
                    throw Exception(event.exception)
                }
            }
        }

    private suspend fun getGroup(groupId: String): Group = withContext(Dispatchers.IO) {
        val event = groupRemoteRepository.fetchGroupsByGroupId(groupId)
        when (event) {
            is Event.Success -> {
                Log.d("RemoteLoadManager", "Received group: ${event.data}")
                writeToLocalDatabase(groupLocalRepository::insertOne, event.data)
                event.data
            }

            is Event.Failure -> {
                Log.e("RemoteLoadManager", "Failed to fetch group: ${event.exception}")
                throw Exception(event.exception)
            }
        }
    }

    override suspend fun fetchUsersByGroupId(groupId: String): List<UserProfile> {
        val groupUsers = getUserGroupsByGroupId(groupId)
        val participants = mutableListOf<UserProfile>()

        for (groupUser in groupUsers) {
            Log.d("RemoteLoadManager", "Received user groups ${groupUser.userId}")
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
                    Log.d("RemoteLoadManager", "Received user groups: ${event.data}")
                    event.data.map {
                        writeToLocalDatabase(userGroupLocalRepository::insertOne, it)
                    }
                    event.data
                }

                is Event.Failure -> {
                    Log.e("RemoteLoadManager", "Failed to fetch user groups: ${event.exception}")
                    throw Exception(event.exception)
                }
            }
        }

    private suspend fun getParticipants(userId: String): UserProfile = withContext(
        Dispatchers.IO
    ) {
        val event = userRemoteRepository.fetchUserById(userId)
        Log.d("RemoteLoadManager", "Received user: ${event}")
        when (event) {
            is Event.Success -> {
                Log.d("RemoteLoadManager", "Received user: ${event.data}")
                writeToLocalDatabase(userLocalRepository::insertOne, event.data)
                event.data
            }

            is Event.Failure -> {
                Log.e("RemoteLoadManager", "Failed to fetch user: ${event.exception}")
                throw Exception(event.exception)
            }
        }
    }

    override suspend fun fetchUserChallengesByGroupId(groupId: String): List<Challenge> =
        withContext(
            Dispatchers.IO
        ) {
            val event = groupChallengeRemoteRepository.fetchAllChallengesByGroupId(groupId)
            Log.d("RemoteLoadManager", "Received challenges: ${event}")
            when (event) {
                is Event.Success -> {
                    Log.d("RemoteLoadManager", "Received challenges: ${event.data}")
                    saveToLocalDatabase(event, groupId)
                    event.data
                }

                is Event.Failure -> {
                    Log.e("RemoteLoadManager", "Failed to fetch challenges: ${event.exception}")
                    throw Exception(event.exception)
                }
            }
        }

    private suspend fun saveToLocalDatabase(
        event: Event.Success<List<Challenge>>,
        groupId: String
    ) {
        event.data.map { challenge ->
            writeToLocalDatabase(challengeLocalRepository::insertOne, challenge)
            writeToLocalDatabase(
                groupChallengeLocalRepository::insertOne,
                GroupChallenge(groupId, challenge.challengeId)
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
}