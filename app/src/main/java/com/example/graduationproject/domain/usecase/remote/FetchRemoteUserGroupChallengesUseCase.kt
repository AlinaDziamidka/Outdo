package com.example.graduationproject.domain.usecase.remote

import android.util.Log
import com.example.graduationproject.data.local.LocalLoadManager
import com.example.graduationproject.data.remote.RemoteLoadManager
import com.example.graduationproject.di.qualifiers.Local
import com.example.graduationproject.di.qualifiers.Remote
import com.example.graduationproject.domain.entity.GroupAndChallenges
import com.example.graduationproject.domain.util.LoadManager
import com.example.graduationproject.domain.util.UseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class FetchRemoteUserGroupChallengesUseCase @Inject constructor(
//    private val userGroupRemoteRepository: UserGroupRemoteRepository,
//    private val groupRemoteRepository: GroupRemoteRepository,
//    private val groupChallengeRemoteRepository: GroupChallengeRemoteRepository,
//    private val userGroupLocalRepository: UserGroupLocalRepository,
//    private val groupLocalRepository: GroupLocalRepository,
//    private val groupChallengeLocalRepository: GroupChallengeLocalRepository,
//    private val challengeLocalRepository: ChallengeLocalRepository
    @Remote private val remoteLoadManager: LoadManager,
    @Local private val localLoadManager: LoadManager
) : UseCase<FetchRemoteUserGroupChallengesUseCase.Params, MutableList<GroupAndChallenges>> {
    data class Params(
        val userId: String,
    )

    override suspend fun invoke(params: Params): Flow<MutableList<GroupAndChallenges>> =
        flow {
            val userId = params.userId
            val groupAndChallenges : MutableList<GroupAndChallenges>
            var groups = localLoadManager.fetchGroupsByUserId(userId)

            if (groups.isNotEmpty()) {
                groupAndChallenges = groups.map { group ->
                    val challenges = localLoadManager.fetchUserChallengesByGroupId(group.groupId)
                    GroupAndChallenges(group, challenges)
                }.toMutableList()
            }
            else {
                groups = remoteLoadManager.fetchGroupsByUserId(userId)
                groupAndChallenges = groups.map { group ->
                    val challenges = remoteLoadManager.fetchUserChallengesByGroupId(group.groupId)
                    GroupAndChallenges(group, challenges)
                }.toMutableList()
            }


            //            val userGroups = getUserGroups(userId)
//
//            val groupAndChallengesList = userGroups.map { userGroup ->
//                Log.d("FetchRemoteUserGroupChallengesUseCase", "Fetching group and challenges for group ${userGroup.groupId}")
//                val group = getGroup(userGroup.groupId)
//                val challenges = getCurrentChallenges(userGroup.groupId)
//                GroupAndChallenges(group, challenges)
//            }.toList()

            emit(groupAndChallenges)
        }

//    private suspend fun getUserGroups(userId: String): List<UserGroup> =
//        withContext(Dispatchers.IO) {
//            val event = userGroupRemoteRepository.fetchAllGroupsByUserId(userId)
//            when (event) {
//                is Event.Success -> {
//                    Log.d("FetchRemoteUserGroupChallengesUseCase", "Received user groups: ${event.data}")
//                    event.data.map {
//                        writeToLocalDatabase(userGroupLocalRepository::insertOne, it)
//                    }
//                    event.data
//                }
//
//                is Event.Failure -> {
//                    Log.e("FetchRemoteUserGroupChallengesUseCase", "Failed to fetch user groups: ${event.exception}")
//                    throw Exception(event.exception)
//                }
//            }
//        }
//
//    private suspend fun getGroup(groupId: String): Group = withContext(Dispatchers.IO) {
//        val event = groupRemoteRepository.fetchGroupsByGroupId(groupId)
//        when (event) {
//            is Event.Success -> {
//                Log.d("FetchRemoteUserGroupChallengesUseCase", "Received group: ${event.data}")
//                writeToLocalDatabase(groupLocalRepository::insertOne, event.data)
//                event.data
//            }
//
//            is Event.Failure -> {
//                Log.e("FetchRemoteUserGroupChallengesUseCase", "Failed to fetch group: ${event.exception}")
//                throw Exception(event.exception)
//            }
//        }
//    }
//
//    private suspend fun getCurrentChallenges(groupId: String): List<Challenge> = withContext(
//        Dispatchers.IO
//    ) {
//        val event = groupChallengeRemoteRepository.fetchAllChallengesByGroupId(groupId)
//        Log.d("FetchRemoteUserGroupChallengesUseCase", "Received challenges: ${event}")
//        when (event) {
//            is Event.Success -> {
//                Log.d("FetchRemoteUserGroupChallengesUseCase", "Received challenges: ${event.data}")
//                saveToLocalDatabase(event, groupId)
//                event.data
//            }
//
//            is Event.Failure -> {
//                Log.e("FetchRemoteUserGroupChallengesUseCase", "Failed to fetch challenges: ${event.exception}")
//                throw Exception(event.exception)
//            }
//        }
//    }
//
//    private suspend fun saveToLocalDatabase(
//        event: Event.Success<List<Challenge>>,
//        groupId: String
//    ) {
//        event.data.map { challenge ->
//            writeToLocalDatabase(challengeLocalRepository::insertOne, challenge)
//            writeToLocalDatabase(
//                groupChallengeLocalRepository::insertOne,
//                GroupChallenge(groupId, challenge.challengeId)
//            )
//        }
//    }
}


