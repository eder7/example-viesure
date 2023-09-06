package io.viesure.test.usecases.utils

import kotlinx.coroutines.delay

/**
 * Executes [body] until no throwing an exception up to [times] times
 * with a delay of [delayMillis] in between. Also returns the value returned by [body].
 */
internal suspend inline fun <R> retry(times: Int = 1, delayMillis: Long = 0, body: () -> R): R {
    var attempt = 0
    while (true) {
        attempt++
        try {
            return body()
        } catch (throwable: Throwable) {
            if (attempt >= times) {
                throw Exception("Failed after $attempt attempts: ${throwable.message}", throwable)
            } else {
                delay(delayMillis)
            }
        }
    }
}
