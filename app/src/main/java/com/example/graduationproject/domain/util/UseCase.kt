package com.example.graduationproject.domain.util

import com.example.graduationproject.Event

    interface UseCase<T, R> {
        suspend operator fun invoke(params: T): Event<R>

        class None
    }