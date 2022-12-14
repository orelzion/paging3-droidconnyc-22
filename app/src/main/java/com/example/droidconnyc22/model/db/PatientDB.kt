package com.example.droidconnyc22.model.db

import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import com.example.droidconnyc22.model.Patient

@Database(entities = [PatientEntity::class, PagingEntity::class], version = 1, exportSchema = false)
@TypeConverters(Convertors::class)
abstract class PatientDB : RoomDatabase() {
    abstract val patientDao: PatientDao
    abstract val pagingDao: PagingDao
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

@Dao
interface PagingDao {

    @Insert(onConflict = REPLACE)
    fun createOrUpdate(pagingEntity: PagingEntity)

    @Query("SELECT * FROM pagingentity WHERE :filterId = filterId")
    fun getFor(filterId: String): List<PagingEntity>

    @Query("DELETE FROM pagingentity WHERE :filterId = filterId")
    fun clear(filterId: String)
}