package com.example.droidconnyc22.model

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query

class PatientDB {
}

@Dao
interface PatientDao {
    @Query("SELECT * FROM patiententity WHERE :patientId == patientId")
    suspend fun getPatientBy(patientId: Int): PatientEntity

    @Query("SELECT * FROM patiententity WHERE :filterId == filterId")
    suspend fun getAllFor(filterId: String): List<PatientEntity>

    @Insert(onConflict = REPLACE)
    suspend fun createOrUpdate(patients: List<PatientEntity>)

    @Delete
    suspend fun remove(patientEntity: PatientEntity)

    @Query("DELETE FROM patiententity WHERE :filterId = filterId")
    suspend fun removeAllFor(filterId: String)
}