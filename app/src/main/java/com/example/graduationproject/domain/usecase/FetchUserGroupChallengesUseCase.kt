package com.example.graduationproject.domain.usecase

import com.example.graduationproject.domain.entity.Challenge
import com.example.graduationproject.domain.entity.GroupChallenge
import com.example.graduationproject.domain.entity.UserGroup
import com.example.graduationproject.domain.repository.remote.ChallengeRepository
import com.example.graduationproject.domain.repository.remote.GroupChallengeRepository
import com.example.graduationproject.domain.repository.remote.GroupRepository
import com.example.graduationproject.domain.repository.remote.UserGroupRepository
import com.example.graduationproject.domain.util.Event
import com.example.graduationproject.domain.util.UseCase
import doCall
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FetchUserGroupChallengesUseCase(
    private val userGroupRepository: UserGroupRepository,
    private val groupRepository: GroupRepository,
    private val groupChallengeRepository: GroupChallengeRepository,
    private val challengeRepository: ChallengeRepository
) : UseCase<FetchUserGroupChallengesUseCase.Params, List<Challenge>> {
    data class Params(
        val userId: String,
    )

    override suspend fun invoke(params: FetchUserGroupChallengesUseCase.Params): Flow<List<Challenge>> =
        flow {
            val userId = params.userId
            val event = getChallengesEvents(userId)

            when (event) {
                is Event.Success -> {
                    emit(event.data)
                }
                is Event.Failure -> {
                    throw Exception(event.exception)
                }
            }
        }


    private suspend fun getChallengesEvents(userId: String): Event<List<Challenge>> {

        val groupEvent = getUserGroups(userId)
        return when (groupEvent) {
            is Event.Success -> {
                val groupChallengeEvent = getGroupChallenges(groupEvent.data)
                when (groupChallengeEvent) {
                    is Event.Success -> {
                        getChallenges(groupChallengeEvent.data)
                    }

                    is Event.Failure -> {
                        groupChallengeEvent
                    }
                }
            }

            is Event.Failure -> {
                groupEvent
            }
        }
    }


    private suspend fun getUserGroups(userId: String): Event<List<UserGroup>> {
        val userGroupsEvent = doCall {
            return@doCall userGroupRepository.fetchAllGroupsByUserId(userId)
        }

        return when (userGroupsEvent) {
            is Event.Success -> {
                Event.Success(userGroupsEvent.data)
            }

            is Event.Failure -> {
                val error = userGroupsEvent.exception
                Event.Failure(error)
            }
        }
    }
    private suspend fun getGroupChallenges(userGroups: List<UserGroup>): Event<List<GroupChallenge>> {

        val groupChallenges = mutableListOf<GroupChallenge>()
        userGroups.map { groupChallenge ->
            val groupChallengeEvent = doCall {
                return@doCall groupChallengeRepository.fetchAllChallengesByGroupId(
                    groupChallenge.groupId
                )
            }
            if (groupChallengeEvent is Event.Success) {
                groupChallenges.addAll(groupChallengeEvent.data)
            } else {
                return Event.Failure("Not found groupChallenge")
            }
        }
        return Event.Success(groupChallenges)
    }

    private suspend fun getChallenges(groupChallenges: List<GroupChallenge>): Event<List<Challenge>> {
        val challenges = mutableListOf<Challenge>()
        groupChallenges.map { challenge ->
            val challengeEvent = doCall {
                return@doCall challengeRepository.fetchChallengesById(challenge.challengeId)
            }
            if (challengeEvent is Event.Success) {
                challenges.addAll(challengeEvent.data)
            } else {
                return Event.Failure("Not found groupChallenge")
            }
        }
        return Event.Success(challenges)
    }
}
//    private suspend fun getGroupsList(userGroups: List<UserGroup>): List<Group> {
//        val groupsEvent = userGroups.map { userGroup ->
//            doCall {
//                return@doCall groupRepository.fetchGroupsByGroupId(userGroup.groupId)
//            }
//        }
//        return groupsEvent.filterIsInstance<Event.Success<List<Group>>>().flatMap { it.data }
//    }

