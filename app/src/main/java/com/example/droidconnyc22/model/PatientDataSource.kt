package com.example.droidconnyc22.model

interface PatientDataSource {
    suspend fun getPatientListBy(filterId: String): List<Patient>
    suspend fun updatePatient(patient: Patient)
}