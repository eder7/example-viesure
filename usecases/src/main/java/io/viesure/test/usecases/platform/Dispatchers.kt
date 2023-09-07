package io.viesure.test.usecases.platform

import kotlinx.coroutines.CoroutineDispatcher

interface Dispatchers {
    val default: CoroutineDispatcher
    val main: CoroutineDispatcher
    val unconfined: CoroutineDispatcher
    val io: CoroutineDispatcher

    companion object {
        /**
         * For testing purposes
         */
        fun create(coroutineDispatcher: CoroutineDispatcher) = object : Dispatchers {
            override val default = coroutineDispatcher
            override val main = coroutineDispatcher
            override val unconfined = coroutineDispatcher
            override val io = coroutineDispatcher
        }
    }
}
