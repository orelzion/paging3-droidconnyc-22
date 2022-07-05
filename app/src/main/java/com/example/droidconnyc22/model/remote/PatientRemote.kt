package com.example.droidconnyc22.model.remote

import com.example.droidconnyc22.model.Patient
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PatientRemote(
    @SerialName("patient_id")
    override val patientId: String,
    override val name: String,
    @SerialName("bookmark_count")
    override val bookmarkCount: Int,
    @SerialName("is_bookmarked")
    override val isBookmarked: Boolean,
    @SerialName("photo_url")
    override val photoUrl: String?
): Patient
