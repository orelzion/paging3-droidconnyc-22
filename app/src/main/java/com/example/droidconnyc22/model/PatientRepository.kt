package com.example.droidconnyc22.model

import com.example.droidconnyc22.model.db.PatientDbDataSource
import com.example.droidconnyc22.model.remote.PatientRemote
import com.example.droidconnyc22.model.remote.PatientRemoteDataSource

class PatientRepository(
    private val patientDbDataSource: PatientDbDataSource,
    private val patientRemoteDataSource: PatientRemoteDataSource
) {

    suspend fun fetchListFor(filterId: String, cleanFetch: Boolean = false): Result<List<Patient>> =
        kotlin.runCatching {

            val cachedList = if (cleanFetch) {
                emptyList<Patient>().also { clearFilter(filterId) }
            } else {
                patientDbDataSource.getPatientListBy(filterId)
            }

            return Result.success(
                cachedList.ifEmpty {
                    patientRemoteDataSource.getPatientListBy(filterId)
                        .also { saveToLocal(it, filterId) }
                }
            )
        }

    private suspend fun saveToLocal(remotePatients: List<Patient>, inFilterId: String) {
        patientDbDataSource.updateListWith(
            remotePatients.map { it as PatientRemote },
            inFilterId
        )
    }

    private suspend fun clearFilter(forFilterId: String) {
        patientDbDataSource.clearFilter(forFilterId)
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