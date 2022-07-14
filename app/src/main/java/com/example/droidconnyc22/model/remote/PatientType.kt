package com.example.droidconnyc22.model.remote

enum class PatientType {
    LVO,
    ICH;

    companion object {
        fun valueOfOrNull(name: String): PatientType? {
            return values().firstOrNull { it.name == name }
        }
    }
}