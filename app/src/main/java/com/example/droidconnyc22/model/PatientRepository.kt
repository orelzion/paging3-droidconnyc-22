package com.example.droidconnyc22.model

import com.example.droidconnyc22.model.db.PagingDao
import com.example.droidconnyc22.model.db.PagingEntity
import com.example.droidconnyc22.model.db.PatientDbDataSource
import com.example.droidconnyc22.model.remote.PatientRemote
import com.example.droidconnyc22.model.remote.PatientRemoteDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PatientRepository(
    private val patientDbDataSource: PatientDbDataSource,
    private val patientRemoteDataSource: PatientRemoteDataSource,
    private val pagingDao: PagingDao
) {

    // Droidcon 5
    suspend fun fetchListFor(
        filter: PatientFilter,
        cleanFetch: Boolean = false
    ): Result<List<Patient>> =
        kotlin.runCatching {

            if (cleanFetch) {
                clearFilter(filter)
            }

            // Driodcon 6
            val pagingData = getPagingData(filter)
            if (pagingData?.hasReachLimit == true) {
                return Result.success(patientDbDataSource.getPatientListBy(filter))
            }

            // Driodcon 7
            fetchAndSave(filter, pagingData?.lastCursorId)

            // Driodcon 8
            return Result.success(
                patientDbDataSource.getPatientListBy(filter)
            )
        }

    private suspend fun getPagingData(filter: PatientFilter) = withContext(Dispatchers.IO) {
        pagingDao.getFor(filter.filterId).firstOrNull()
    }

    private suspend fun fetchAndSave(filter: PatientFilter, lastPatientId: String?): List<Patient> {
        return patientRemoteDataSource.getPatientListBy(filter, lastPatientId, limit = PAGE_SIZE)
            .also {
                saveToLocal(it, filter)
                updatePagingData(it, filter)
            }
    }

    private suspend fun updatePagingData(remotePatients: List<Patient>, filter: PatientFilter) =
        withContext(Dispatchers.IO) {
            val pagingEntity = PagingEntity(
                filterId = filter.filterId,
                lastCursorId = remotePatients.last().patientId,
                hasReachLimit = remotePatients.size < PAGE_SIZE
            )
            pagingDao.createOrUpdate(pagingEntity)
        }

    private suspend fun saveToLocal(remotePatients: List<Patient>, filter: PatientFilter) {
        patientDbDataSource.updateListWith(
            remotePatients.map { it as PatientRemote },
            filter
        )
    }

    private suspend fun clearFilter(filter: PatientFilter) = withContext(Dispatchers.IO) {
        patientDbDataSource.clearFilter(filter)
        pagingDao.clear(filter.filterId)
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

    companion object {
        private const val PAGE_SIZE = 5
    }
}