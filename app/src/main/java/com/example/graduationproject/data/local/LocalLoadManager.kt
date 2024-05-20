package com.example.graduationproject.data.local

import android.util.Log
import com.example.graduationproject.domain.entity.Challenge
import com.example.graduationproject.domain.entity.Group
import com.example.graduationproject.domain.entity.UserProfile
import com.example.graduationproject.domain.repository.local.ChallengeLocalRepository
import com.example.graduationproject.domain.repository.local.GroupChallengeLocalRepository
import com.example.graduationproject.domain.repository.local.GroupLocalRepository
import com.example.graduationproject.domain.repository.local.UserGroupLocalRepository
import com.example.graduationproject.domain.repository.local.UserLocalRepository
import com.example.graduationproject.domain.util.LoadManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LocalLoadManager @Inject constructor(
    private val userGroupLocalRepository: UserGroupLocalRepository,
    private val groupLocalRepository: GroupLocalRepository,
    private val groupChallengeLocalRepository: GroupChallengeLocalRepository,
    private val challengeLocalRepository: ChallengeLocalRepository,
    private val userLocalRepository: UserLocalRepository
) : LoadManager {
    override suspend fun fetchGroupsByUserId(userId: String): List<Group> =
        withContext(Dispatchers.IO) {
            val userGroups = userGroupLocalRepository.fetchGroupsByUserId(userId)
            val groups = mutableListOf<Group>()

            for (userGroup in userGroups) {
                Log.d("LocalLoadManager", "Fetching groups for group ${userGroup.groupId}")
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
                Log.d("LocalLoadManager", "Received group users ${groupUser.userId}")
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
}