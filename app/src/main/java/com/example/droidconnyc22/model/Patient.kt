package com.example.droidconnyc22.model

import kotlinx.datetime.Instant

interface Patient {
    val patientId: String
    val name: String
    val bookmarkCount: Int
    val isBookmarked: Boolean
    val updatedAt: Instant
    val photoUrl: String?

    fun copy(_isBookmarked: Boolean, _bookmarkCount: Int): Patient
}