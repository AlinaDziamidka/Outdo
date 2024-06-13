package com.example.graduationproject.domain.usecase

import com.example.graduationproject.di.qualifiers.Local
import com.example.graduationproject.di.qualifiers.Remote
import com.example.graduationproject.domain.entity.UserProfile
import com.example.graduationproject.domain.util.LoadManager
import com.example.graduationproject.domain.util.UseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class FetchFriendsUseCase @Inject constructor(
    @Remote private val remoteLoadManager: LoadManager,
    @Local private val localLoadManager: LoadManager
) : UseCase<FetchFriendsUseCase.Params, MutableList<UserProfile>> {

    data class Params(
        val userId: String
    )

    override suspend fun invoke(params: Params): Flow<MutableList<UserProfile>> =
        flow {
            val userId = params.userId
            val friends = localLoadManager.fetchFriendsByUserId(userId)
                .takeIf { it.isNotEmpty() }
                ?: remoteLoadManager.fetchFriendsByUserId(userId)
            emit(friends.toMutableList())
        }
}