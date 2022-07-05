package com.example.droidconnyc22.model

import com.example.droidconnyc22.model.db.PatientDbDataSource
import com.example.droidconnyc22.model.remote.PatientRemote
import com.example.droidconnyc22.model.remote.PatientRemoteDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PatientRepository(
    private val patientDbDataSource: PatientDbDataSource,
    private val patientRemoteDataSource: PatientRemoteDataSource
) {

    suspend fun fetchListFor(filterId: String, cleanFetch: Boolean = false): Result<List<Patient>> =
        kotlin.runCatching {

            val cachedList = if (cleanFetch) {
                emptyList<Patient>().also { clearFilter(filterId) }
            } else withContext(Dispatchers.IO) {
                patientDbDataSource.getPatientListBy(filterId)
            }

            return Result.success(
                cachedList.ifEmpty {
                    withContext(Dispatchers.Default) {
                        patientRemoteDataSource.getPatientListBy(filterId)
                    }.also { saveToLocal(it, filterId) }
                }
            )
        }

    private suspend fun saveToLocal(remotePatients: List<Patient>, inFilterId: String) {
        withContext(Dispatchers.IO) {
            patientDbDataSource.updateListWith(
                remotePatients.map { it as PatientRemote },
                inFilterId
            )
        }
    }

    private suspend fun clearFilter(forFilterId: String) {
        withContext(Dispatchers.IO) {
            patientDbDataSource.clearFilter(forFilterId)
        }
    }
}