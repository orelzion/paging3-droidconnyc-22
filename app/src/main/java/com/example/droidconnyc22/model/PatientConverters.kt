package com.example.droidconnyc22.model

import com.example.droidconnyc22.model.db.PatientEntity
import com.example.droidconnyc22.model.remote.PatientRemote

fun PatientRemote.toPatientEntity(withFilterId: String): PatientEntity {
    val remoteModel = this
    return PatientEntity(
        patientId = remoteModel.patientId,
        name = remoteModel.name,
        bookmarkCount = remoteModel.bookmarkCount,
        isBookmarked = remoteModel.isBookmarked,
        photoUrl = remoteModel.photoUrl,
        filterId = withFilterId
    )
}

fun PatientEntity.toPatientRemote(): PatientRemote {
    val patientEntity = this
    return PatientRemote(
        patientId = patientEntity.patientId,
        name = patientEntity.name,
        bookmarkCount = patientEntity.bookmarkCount,
        isBookmarked = patientEntity.isBookmarked,
        photoUrl = patientEntity.photoUrl
    )
}