package com.example.droidconnyc22.model.db

import com.example.droidconnyc22.model.Patient
import com.example.droidconnyc22.model.PatientDataSource
import com.example.droidconnyc22.model.remote.PatientRemote
import com.example.droidconnyc22.model.toPatientEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PatientDbDataSource(private val patientDao: PatientDao) : PatientDataSource {

    override suspend fun getPatientListBy(filterId: String): List<PatientEntity> =
        withContext(Dispatchers.IO) {
            patientDao.getAllFor(filterId)
        }

    override suspend fun toggleBookmark(forPatient: Patient, toBookmark: Boolean): Patient {
        val updatedPatient = forPatient.copy(_isBookmarked = toBookmark)
        updatePatient(updatedPatient)

        return updatedPatient
    }

    suspend fun updatePatient(patient: Patient) = withContext(Dispatchers.IO) {
        val patientId = patient.patientId
        patientDao.getPatientBy(patientId).forEach { patientEntity ->
            val updatedEntity = patientEntity.copy(
                name = patient.name,
                bookmarkCount = patient.bookmarkCount,
                isBookmarked = patient.isBookmarked,
                photoUrl = patient.photoUrl
            )

            patientDao.update(updatedEntity)
        }
    }

    suspend fun updateListWith(patients: List<PatientRemote>, forFilterId: String) =
        withContext(Dispatchers.IO) {
            patientDao.createOrUpdate(patients.map { it.toPatientEntity(forFilterId) })
        }

    suspend fun clearFilter(forFilterId: String) = withContext(Dispatchers.IO) {
        patientDao.removeAllFor(forFilterId)
    }
}