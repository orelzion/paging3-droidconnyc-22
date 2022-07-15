package com.example.droidconnyc22.model.remote

import com.example.droidconnyc22.model.Patient
import com.example.droidconnyc22.model.PatientDataSource
import com.example.droidconnyc22.model.PatientFilter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PatientRemoteDataSource(
    private val patientService: PatientService
) : PatientDataSource {

    override suspend fun getPatientListBy(filter: PatientFilter): List<Patient> =
        withContext(Dispatchers.Default) {
            when (filter) {
                PatientFilter.All -> patientService.getAllPatients()
                PatientFilter.Bookmarks -> patientService.getBookmarkedPatients()
                is PatientFilter.TypeFilter -> patientService.getPatientsByType(filter.type)
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