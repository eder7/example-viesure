package io.viesure.test.adapters.utils

import kotlinx.coroutines.delay

/**
 * Executes [body] until not throwing any [Throwable] up to [times] times
 * with a delay of [delayMillis] in between. Also returns the value returned by [body].
 */
suspend inline fun <R> retry(times: Int = 1, delayMillis: Long = 0, body: () -> R): R {
    var count = 0
    while (true) {
        count++
        try {
            return body()
        } catch (throwable: Throwable) {
            if (count >= times) {
                throw Exception("Failed after $count attempts: ${throwable.message}", throwable)
            } else {
                delay(delayMillis)
            }
        }
    }
}
