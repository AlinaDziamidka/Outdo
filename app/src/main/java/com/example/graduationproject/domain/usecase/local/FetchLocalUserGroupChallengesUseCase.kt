package com.example.graduationproject.domain.usecase.local

import com.example.graduationproject.data.local.LocalLoadManager
import com.example.graduationproject.domain.entity.Challenge
import com.example.graduationproject.domain.entity.Group
import com.example.graduationproject.domain.entity.GroupAndChallenges
import com.example.graduationproject.domain.entity.UserGroup
import com.example.graduationproject.domain.repository.local.ChallengeLocalRepository
import com.example.graduationproject.domain.repository.local.GroupChallengeLocalRepository
import com.example.graduationproject.domain.repository.local.GroupLocalRepository
import com.example.graduationproject.domain.repository.local.UserGroupLocalRepository
import com.example.graduationproject.domain.util.UseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FetchLocalUserGroupChallengesUseCase @Inject constructor(
//    private val userGroupLocalRepository: UserGroupLocalRepository,
//    private val groupLocalRepository: GroupLocalRepository,
//    private val groupChallengeLocalRepository: GroupChallengeLocalRepository,
//    private val challengeLocalRepository: ChallengeLocalRepository
    private val localLoadManager: LocalLoadManager
) : UseCase<FetchLocalUserGroupChallengesUseCase.Params, List<GroupAndChallenges>> {
    data class Params(
        val userId: String,
    )

    override suspend fun invoke(params: Params): Flow<List<GroupAndChallenges>> =
        flow {
            val userId = params.userId
            val groups = localLoadManager.fetchGroupsByUserId(userId)

            val groupAndChallengesList = groups.map { group ->
                val challenges = localLoadManager.fetchUserChallengesByGroupId(group.groupId)
                GroupAndChallenges(group, challenges)
            }

//            val userGroups = getUserGroups(userId)
//
//            val groupAndChallengesList = userGroups.map { userGroup ->
//                val group = getGroup(userGroup.groupId)
//                val challenges = getCurrentChallenges(userGroup.groupId)
//                GroupAndChallenges(group, challenges)
//            }.toList()

            emit(groupAndChallengesList)
        }

//    private suspend fun getUserGroups(userId: String): List<UserGroup> =
//        withContext(Dispatchers.IO) {
//            return@withContext userGroupLocalRepository.fetchGroupsByUserId(userId)
////            if (userGroups.isNotEmpty()) {
////                return@withContext userGroups
////            } else {
////                throw Exception("UserGroups not found")
////            }
//        }
//
//    private suspend fun getGroup(groupId: String): Group = withContext(Dispatchers.IO) {
//        val group = groupLocalRepository.fetchById(groupId)
//        return@withContext group ?: throw Exception("Group not found")
//    }
//
//    private suspend fun getCurrentChallenges(groupId: String): List<Challenge> = withContext(
//        Dispatchers.IO
//    ) {
//        val groupChallenges = groupChallengeLocalRepository.fetchChallengesByGroupId(groupId)
//            return@withContext groupChallenges.map { groupChallenge ->
//                challengeLocalRepository.fetchById(groupChallenge.challengeId)
//        }
//    }
}