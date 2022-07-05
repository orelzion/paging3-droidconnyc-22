package com.example.droidconnyc22.model.db

import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE

@Database(entities = [PatientEntity::class], version = 1, exportSchema = false)
abstract class PatientDB: RoomDatabase() {
    abstract val patientDao: PatientDao
}

@Dao
interface PatientDao {
    @Query("SELECT * FROM patiententity WHERE :patientId = patientId")
    fun getPatientBy(patientId: String): List<PatientEntity>

    @Query("SELECT * FROM patiententity WHERE :filterId = filterId")
    fun getAllFor(filterId: String): List<PatientEntity>

    @Insert(onConflict = REPLACE)
    fun createOrUpdate(patients: List<PatientEntity>)

    @Delete
    fun remove(patientEntity: PatientEntity)

    @Query("DELETE FROM patiententity WHERE :filterId = filterId")
    fun removeAllFor(filterId: String)

    @Update
    fun update(patient: PatientEntity)
}