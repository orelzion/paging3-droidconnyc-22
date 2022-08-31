package com.example.droidconnyc22.model

import com.example.droidconnyc22.BuildConfig
import com.example.droidconnyc22.model.db.PatientEntity
import com.example.droidconnyc22.model.remote.PatientRemote

fun PatientRemote.toPatientEntity(withFilterId: String): PatientEntity {
    val remoteModel = this
    return PatientEntity(
        genId = 0,
        patientId = remoteModel.patientId,
        name = remoteModel.name,
        bookmarkCount = remoteModel.bookmarkCount,
        isBookmarked = remoteModel.isBookmarked,
        photoUrl = BuildConfig.LOCAL_IP + remoteModel.photoUrl,
        filterId = withFilterId,
        updatedAt = remoteModel.updatedAt
    )
}

fun PatientEntity.toPatientRemote(): PatientRemote {
    val patientEntity = this
    return PatientRemote(
        patientId = patientEntity.patientId,
        name = patientEntity.name,
        bookmarkCount = patientEntity.bookmarkCount,
        isBookmarked = patientEntity.isBookmarked,
        photoUrl = patientEntity.photoUrl,
        updatedAt = patientEntity.updatedAt
    )
}