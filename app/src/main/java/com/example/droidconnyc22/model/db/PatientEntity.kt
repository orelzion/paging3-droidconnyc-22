package com.example.droidconnyc22.model.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.droidconnyc22.model.Patient

@Entity
data class PatientEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    override val patientId: String,
    override val name: String,
    override val bookmarkCount: Int,
    override val isBookmarked: Boolean,
    override val photoUrl: String?,
    val filterId: String,
): Patient
