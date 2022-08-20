package com.example.droidconnyc22

import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

inline fun <reified T> tryOrNull(block: () -> T): T? {
    return try {
        block()
    } catch (e: Exception) {
        null
    }
}

suspend inline fun <R> runCatchingInContext(
    context: CoroutineContext,
    crossinline block: suspend () -> R
): Result<R> = runCatching {
    withContext(context) {
        block()
    }
}