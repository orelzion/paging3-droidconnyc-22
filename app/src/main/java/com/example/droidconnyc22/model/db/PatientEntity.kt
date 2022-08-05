package com.example.droidconnyc22.model.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.example.droidconnyc22.model.Patient
import kotlinx.datetime.Instant
import kotlinx.datetime.toInstant

@Entity()
data class PatientEntity(
    @PrimaryKey(autoGenerate = true)
    val genId: Int,
    override val patientId: String,
    override val name: String,
    override val bookmarkCount: Int,
    override val isBookmarked: Boolean,
    override val photoUrl: String?,
    val filterId: String,
    override val updatedAt: Instant,
) : Patient {
    override fun copy(_isBookmarked: Boolean, _bookmarkCount: Int): Patient =
        this.copy(isBookmarked = _isBookmarked, bookmarkCount = _bookmarkCount)
}

class Convertors {

    @TypeConverter
    fun fromString(string: String): Instant {
        return string.toInstant()
    }

    @TypeConverter
    fun toString(instant: Instant): String {
        return instant.toString()
    }
}
