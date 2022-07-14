package com.example.droidconnyc22.model.remote

import kotlinx.serialization.Serializable

@Serializable
data class BookmarkRequest(
    val isBookmarked: Boolean
)
