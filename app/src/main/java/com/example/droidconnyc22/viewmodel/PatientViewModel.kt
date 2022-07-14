package com.example.droidconnyc22.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.droidconnyc22.model.Patient
import com.example.droidconnyc22.model.PatientRepository
import com.example.droidconnyc22.model.TabsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PatientViewModel(
    private val patientRepository: PatientRepository,
    private val tabsRepository: TabsRepository
) : ViewModel() {

    private val _viewState = MutableStateFlow<PatientListUiState>(PatientListUiState.Loading())
    val viewState
        get() = _viewState.asStateFlow()

    init {
        viewModelScope.launch {
            val tabs = tabsRepository.getTabs()
            val selectedTabId = tabs[0].id

            withContext(Dispatchers.Main) {
                _viewState.update {
                    PatientListUiState.Loading(tabs = tabs, currentTabId = selectedTabId)
                }
            }

            onTabSelected(0)
        }
    }

    fun onTabSelected(tabIndex: Int) {
        val selectedTabId = viewState.value.tabs[tabIndex].id

        viewModelScope.launch {
            updateStateWithSelectedTab(selectedTabId)

            val result = patientRepository.fetchListFor(selectedTabId)
            result.onFailure { updateStateToFailure(it) }
            result.onSuccess { updateStateToLoaded(it, selectedTabId) }
        }
    }

    fun onUserTappedOnBookmark(ofPatient: Patient) {
        viewModelScope.launch {
            patientRepository
                .toggleBookmark(ofPatient, toBookmark = ofPatient.isBookmarked.not())
                .onSuccess {
                    updatePatientInList(it)
                }
                .onFailure {
                    //TODO Show a one time error
                }
        }
    }

    private fun updatePatientInList(patient: Patient) {
        val updatedList = _viewState.value.patientList.map {
            if (it.patientId == patient.patientId) {
                it.copy(patient.isBookmarked)
            } else {
                it
            }
        }

        _viewState.update {
            PatientListUiState.Loaded(
                updatedList,
                tabs = it.tabs,
                currentTabId = it.currentTabId!!
            )
        }
    }

    private suspend fun updateStateWithSelectedTab(tabId: String) = withContext(Dispatchers.Main) {
        _viewState.update {
            it.copy(tabId)
        }
    }

    private suspend fun updateStateToFailure(exception: Throwable) = withContext(Dispatchers.Main) {
        _viewState.update {
            PatientListUiState.Error(
                it.patientList,
                it.tabs,
                it.currentTabId,
                exception
            )
        }
    }

    private suspend fun updateStateToLoaded(patients: List<Patient>, tabId: String) =
        withContext(Dispatchers.Main) {
            _viewState.update {
                PatientListUiState.Loaded(
                    patients,
                    it.tabs,
                    tabId
                )
            }
        }
}