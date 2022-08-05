package com.example.droidconnyc22.model.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PagingEntity(
    @PrimaryKey
    val filterId: String,
    val lastCursorId: String,
    val hasReachLimit: Boolean
)
