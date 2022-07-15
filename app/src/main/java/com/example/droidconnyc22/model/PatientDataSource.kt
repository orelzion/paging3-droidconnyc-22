package com.example.droidconnyc22.model

interface PatientDataSource {
    suspend fun getPatientListBy(filter: PatientFilter): List<Patient>
    suspend fun toggleBookmark(forPatient: Patient, toBookmark: Boolean): Patient
}