package com.example.droidconnyc22.model.remote

import com.example.droidconnyc22.model.Patient
import com.example.droidconnyc22.model.PatientDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PatientRemoteDataSource(
    private val patientService: PatientService
) : PatientDataSource {

    override suspend fun getPatientListBy(filterId: String): List<Patient> =
        withContext(Dispatchers.Default) {
            PatientType.valueOfOrNull(filterId)?.let {
                patientService.getPatientsByType(it)
            } ?: kotlin.run {
                patientService.getAllPatients()
            }
        }

    override suspend fun toggleBookmark(forPatient: Patient, toBookmark: Boolean) =
        withContext(Dispatchers.Default) {
            patientService.setPatientBookmark(
                BookmarkRequest(isBookmarked = toBookmark),
                patientId = forPatient.patientId
            )
        }
}