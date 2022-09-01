package com.example.droidconnyc22.model.db

import androidx.paging.PagingSource
import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import com.example.droidconnyc22.model.Patient

@Database(entities = [PatientEntity::class, PagingEntity::class], version = 1, exportSchema = false)
@TypeConverters(Convertors::class)
abstract class PatientDB : RoomDatabase() {
    abstract val patientDao: PatientDao
}

@Dao
interface PatientDao {
    @Query("SELECT * FROM patiententity WHERE :patientId = patientId")
    fun getPatientBy(patientId: String): List<PatientEntity>

    @Query("""
        SELECT * FROM patiententity 
        WHERE :filterId = filterId
        ORDER BY genId
        """)
    fun getAllFor(filterId: String): List<PatientEntity>

    @Query("""
        SELECT * FROM patiententity 
        WHERE :filterId = filterId
        ORDER BY genId
        """)
    fun getAllPagedFor(filterId: String): PagingSource<Int, PatientEntity>

    @Query("SELECT COUNT(*) FROM patiententity WHERE :filterId = filterId")
    fun getFilterSize(filterId: String): Int

    @Insert(onConflict = REPLACE)
    fun createOrUpdate(patients: List<PatientEntity>)

    @Delete
    fun remove(patientEntity: PatientEntity)

    @Query("DELETE FROM patiententity WHERE :filterId = filterId")
    fun removeAllFor(filterId: String)

    @Update
    fun update(patient: PatientEntity)

    @Transaction
    fun updatePatientById(toPatient: Patient) {
        val patientId = toPatient.patientId

        getPatientBy(patientId).forEach { patientEntity ->
            val updatedEntity = patientEntity.copy(
                name = toPatient.name,
                bookmarkCount = toPatient.bookmarkCount,
                isBookmarked = toPatient.isBookmarked,
                photoUrl = toPatient.photoUrl
            )

            update(updatedEntity)
        }
    }
}
