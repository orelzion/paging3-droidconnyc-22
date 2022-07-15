package com.example.droidconnyc22.model

import com.example.droidconnyc22.model.remote.PatientType

sealed class PatientFilter {
    abstract val filterId: String

    data class TypeFilter(val type: PatientType): PatientFilter() {
        override val filterId: String
            get() = type.name
    }

    object Bookmarks: PatientFilter() {
        override val filterId: String
            get() = "bookmarked"
    }

    object All: PatientFilter() {
        override val filterId: String
            get() = "all"
    }
}
