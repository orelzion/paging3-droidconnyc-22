package com.example.droidconnyc22.viewmodel

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.example.droidconnyc22.model.Patient
import com.example.droidconnyc22.model.TabData

sealed class PatientListUiState {
    abstract val patientList: List<Patient>
    abstract val tabs: List<TabData>
    abstract val currentTabId: String?
    abstract val emptyState: EmptyState?
    abstract fun copy(withCurrentTabId: String): PatientListUiState

    data class Loading(
        override val patientList: List<Patient> = emptyList(),
        override val tabs: List<TabData> = emptyList(),
        override val currentTabId: String? = null,
        override val emptyState: EmptyState? = null
    ) : PatientListUiState() {
        override fun copy(withCurrentTabId: String) = copy(currentTabId = withCurrentTabId)
    }

    data class Loaded(
        override val patientList: List<Patient>,
        override val tabs: List<TabData>,
        override val currentTabId: String?,
        override val emptyState: EmptyState?
    ) : PatientListUiState() {
        override fun copy(withCurrentTabId: String) = copy(currentTabId = withCurrentTabId)
    }

    data class Error(
        override val patientList: List<Patient> = emptyList(),
        override val tabs: List<TabData> = emptyList(),
        override val currentTabId: String? = null,
        override val emptyState: EmptyState?,
        val error: Throwable
    ) : PatientListUiState() {
        override fun copy(withCurrentTabId: String) = copy(currentTabId = withCurrentTabId)
    }
}

data class EmptyState(
    @DrawableRes val icon: Int? = null,
    @StringRes val title: StringResource,
    @StringRes val description: StringResource? = null
)

fun PatientListUiState.isLoading() = this is PatientListUiState.Loading
