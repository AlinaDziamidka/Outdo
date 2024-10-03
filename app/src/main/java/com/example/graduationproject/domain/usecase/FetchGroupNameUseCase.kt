package com.example.graduationproject.domain.usecase

import com.example.graduationproject.domain.entity.Group
import com.example.graduationproject.domain.repository.local.GroupLocalRepository
import com.example.graduationproject.domain.util.UseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FetchGroupNameUseCase @Inject constructor(
    private val groupLocalRepository: GroupLocalRepository
) : UseCase<FetchGroupNameUseCase.Params, Group> {

    data class Params(
        val groupId: String
    )

    override suspend fun invoke(params: Params): Flow<Group> =
        flow {
            emit(withContext(Dispatchers.IO) {
                groupLocalRepository.fetchById(params.groupId)
            })
        }
}