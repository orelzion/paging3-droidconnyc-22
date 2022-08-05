package com.example.droidconnyc22.model

interface PatientDataSource {
    suspend fun getPatientListBy(
        filter: PatientFilter,
        lastPatientId: String? = null,
        limit: Int = Int.MAX_VALUE
    ): List<Patient>

    suspend fun toggleBookmark(forPatient: Patient, toBookmark: Boolean): Patient
}