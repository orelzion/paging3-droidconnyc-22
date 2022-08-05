package com.example.droidconnyc22

inline fun <reified T> tryOrNull(block: () -> T): T? {
    return try {
        block()
    } catch (e: Exception) {
        null
    }
}