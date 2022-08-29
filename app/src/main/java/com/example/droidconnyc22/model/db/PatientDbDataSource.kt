package com.example.droidconnyc22.model.db

import com.example.droidconnyc22.model.Patient
import com.example.droidconnyc22.model.PatientDataSource
import com.example.droidconnyc22.model.PatientFilter
import com.example.droidconnyc22.model.remote.PatientRemote
import com.example.droidconnyc22.model.toPatientEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PatientDbDataSource(private val patientDao: PatientDao) : PatientDataSource {

    override suspend fun getPatientListBy(
        filter: PatientFilter,
        lastPatientId: String?,
        limit: Int
    ): List<PatientEntity> =
        withContext(Dispatchers.IO) {
            patientDao.getAllFor(filter.filterId)
        }

    fun getPatientListPaging(filter: PatientFilter) = patientDao.getAllPagedFor(filter.filterId)

    override suspend fun toggleBookmark(forPatient: Patient, toBookmark: Boolean): Patient {
        val updatedPatient =
            forPatient.copy(_isBookmarked = toBookmark, _bookmarkCount = forPatient.bookmarkCount)
        updatePatient(updatedPatient)

        return updatedPatient
    }

    suspend fun updatePatient(patient: Patient) = withContext(Dispatchers.IO) {
        patientDao.updatePatientById(toPatient = patient)
    }

    suspend fun updateListWith(patients: List<PatientRemote>, filter: PatientFilter) =
        withContext(Dispatchers.IO) {
            patientDao.createOrUpdate(patients.map { it.toPatientEntity(filter.filterId) })
        }

    suspend fun clearFilter(filter: PatientFilter) = withContext(Dispatchers.IO) {
        patientDao.removeAllFor(filter.filterId)
    }
}