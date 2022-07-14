package com.example.droidconnyc22.model.remote

import com.example.droidconnyc22.model.Patient
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class PatientRemote(
    override val patientId: String,
    override val name: String,
    override val bookmarkCount: Int,
    override val isBookmarked: Boolean,
    override val photoUrl: String?,
    override val updatedAt: Instant
) : Patient {
    override fun copy(_isBookmarked: Boolean) = this.copy(isBookmarked = _isBookmarked)
}
