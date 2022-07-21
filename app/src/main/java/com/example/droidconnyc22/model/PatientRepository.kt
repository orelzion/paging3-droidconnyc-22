package com.example.droidconnyc22.model

import com.example.droidconnyc22.model.db.PatientDbDataSource
import com.example.droidconnyc22.model.remote.PatientRemote
import com.example.droidconnyc22.model.remote.PatientRemoteDataSource

class PatientRepository(
    private val patientDbDataSource: PatientDbDataSource,
    private val patientRemoteDataSource: PatientRemoteDataSource
) {

    suspend fun fetchListFor(filter: PatientFilter, cleanFetch: Boolean = false): Result<List<Patient>> =
        kotlin.runCatching {

            val cachedList = if (cleanFetch) {
                emptyList<Patient>().also { clearFilter(filter) }
            } else {
                patientDbDataSource.getPatientListBy(filter)
            }

            return Result.success(
                cachedList.ifEmpty {
                    fetchAndSave(filter)
                }
            )
        }

    private suspend fun PatientRepository.fetchAndSave(filter: PatientFilter): List<Patient> {
        return patientRemoteDataSource.getPatientListBy(filter)
            .also { saveToLocal(it, filter) }
    }

    private suspend fun saveToLocal(remotePatients: List<Patient>, filter: PatientFilter) {
        patientDbDataSource.updateListWith(
            remotePatients.map { it as PatientRemote },
            filter
        )
    }

    private suspend fun clearFilter(filter: PatientFilter) {
        patientDbDataSource.clearFilter(filter)
    }

    suspend fun toggleBookmark(forPatient: Patient, toBookmark: Boolean): Result<Patient> {
        patientDbDataSource.toggleBookmark(forPatient, toBookmark)

        val result = kotlin.runCatching {
            patientRemoteDataSource.toggleBookmark(forPatient, toBookmark)
        }

        result.onSuccess {
            patientDbDataSource.updatePatient(it)
        }
        result.onFailure {
            patientDbDataSource.toggleBookmark(forPatient, toBookmark.not())
        }

        return result
    }
}