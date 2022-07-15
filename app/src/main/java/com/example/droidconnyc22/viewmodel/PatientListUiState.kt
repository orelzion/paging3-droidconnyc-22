package com.example.droidconnyc22.viewmodel

import com.example.droidconnyc22.model.Patient
import com.example.droidconnyc22.model.TabData

sealed class PatientListUiState {
    abstract val patientList: List<Patient>
    abstract val tabs: List<TabData>
    abstract val currentTabId: String?
    abstract fun copy(withCurrentTabId: String): PatientListUiState

    data class Loading(
        override val patientList: List<Patient> = emptyList(),
        override val tabs: List<TabData> = emptyList(),
        override val currentTabId: String? = null
    ) : PatientListUiState() {
        override fun copy(withCurrentTabId: String) = copy(currentTabId = withCurrentTabId)
    }

    data class Loaded(
        override val patientList: List<Patient>,
        override val tabs: List<TabData>,
        override val currentTabId: String?
    ) : PatientListUiState() {
        override fun copy(withCurrentTabId: String) = copy(currentTabId = withCurrentTabId)
    }

    data class Error(
        override val patientList: List<Patient> = emptyList(),
        override val tabs: List<TabData> = emptyList(),
        override val currentTabId: String? = null,
        val error: Throwable
    ) : PatientListUiState() {
        override fun copy(withCurrentTabId: String) = copy(currentTabId = withCurrentTabId)
    }

    data class Empty(
        override val patientList: List<Patient> = emptyList(),
        override val tabs: List<TabData>,
        override val currentTabId: String
    ) : PatientListUiState() {
        override fun copy(withCurrentTabId: String) = copy(currentTabId = withCurrentTabId)
    }
}
