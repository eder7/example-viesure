package io.viesure.test.usecases.platform

import kotlinx.coroutines.CoroutineDispatcher

interface Dispatchers {
    val default: CoroutineDispatcher
    val main: CoroutineDispatcher
    val unconfined: CoroutineDispatcher
    val io: CoroutineDispatcher
}
