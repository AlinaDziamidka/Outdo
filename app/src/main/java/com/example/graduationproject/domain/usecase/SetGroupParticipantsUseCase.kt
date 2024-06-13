package com.example.graduationproject.domain.usecase

import android.util.Log
import com.example.graduationproject.domain.entity.UserProfile
import com.example.graduationproject.domain.repository.local.GroupLocalRepository
import com.example.graduationproject.domain.repository.local.UserGroupLocalRepository
import com.example.graduationproject.domain.repository.remote.UserGroupRemoteRepository
import com.example.graduationproject.domain.util.Event
import com.example.graduationproject.domain.util.NonReturningUseCase
import com.example.graduationproject.domain.util.writeToLocalDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SetGroupParticipantsUseCase @Inject constructor(
    private val userGroupRemoteRepository: UserGroupRemoteRepository,
    private val userGroupLocalRepository: UserGroupLocalRepository,
) : NonReturningUseCase<SetGroupParticipantsUseCase.Params> {

    data class Params(
        val groupId: String,
        val friends: List<UserProfile>
    )

    override suspend fun invoke(params: Params) {
        val groupId = params.groupId
        val friends = params.friends

        friends.map { userProfile ->
            val event = userGroupRemoteRepository.insertUserGroup(userProfile.userId, groupId)
            if (event is Event.Success) {
                withContext(Dispatchers.IO) {
                    writeToLocalDatabase(
                        userGroupLocalRepository::insertOne,
                        event.data
                    )
                }
            } else Log.e(
                "SetGroupParticipantsUseCase",
                "Failed to set user group with user ID: ${userProfile.userId}"
            )
        }
    }
}