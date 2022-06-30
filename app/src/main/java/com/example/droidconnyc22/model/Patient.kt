package com.example.droidconnyc22.model

data class Patient(
    val id: String,
    val name: String,
    val bookmarkCount: Int,
    val isBookmarked: Boolean,
    val photoUrl: String?
)
