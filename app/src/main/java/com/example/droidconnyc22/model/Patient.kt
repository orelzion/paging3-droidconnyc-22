package com.example.droidconnyc22.model

interface Patient {
    val patientId: String
    val name: String
    val bookmarkCount: Int
    val isBookmarked: Boolean
    val photoUrl: String?
}