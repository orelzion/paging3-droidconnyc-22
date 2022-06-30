package com.example.droidconnyc22.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PatientEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val patientId: String,
    val name: String,
    val bookmarkCount: Int,
    val isBookmarked: Boolean,
    val imageUrl: String?,
    val filterId: String
)
