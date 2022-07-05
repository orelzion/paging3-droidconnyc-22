package com.example.droidconnyc22.model.db

import com.example.droidconnyc22.model.Patient
import com.example.droidconnyc22.model.PatientDataSource
import com.example.droidconnyc22.model.remote.PatientRemote
import com.example.droidconnyc22.model.toPatientEntity

class PatientDbDataSource(private val patientDao: PatientDao) : PatientDataSource {

    override suspend fun getPatientListBy(filterId: String): List<PatientEntity> {
        return patientDao.getAllFor(filterId)
    }

    override suspend fun updatePatient(patient: Patient) {
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

    suspend fun updateListWith(patients: List<PatientRemote>, forFilterId: String) {
        patientDao.createOrUpdate(patients.map { it.toPatientEntity(forFilterId) })
    }

    suspend fun clearFilter(forFilterId: String) {
        patientDao.removeAllFor(forFilterId)
    }
}