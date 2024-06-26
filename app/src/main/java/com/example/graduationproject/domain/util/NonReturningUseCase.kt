package com.example.graduationproject.domain.util

interface NonReturningUseCase<T> {

    suspend operator fun invoke(params: T)

    object None
}