package com.example.droidconnyc22.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.droidconnyc22.R
import com.example.droidconnyc22.model.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class PatientViewModel(
    private val patientRepository: PatientRepository,
    private val tabsRepository: TabsRepository
) : ViewModel() {

    private var currentJob: Job? = null

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
        val selectedTab = viewState.value.tabs[tabIndex]

        updateToLoadingStateWithSelectedTab(selectedTab.id)
        fetchPatients(selectedTab, forceRefresh = false)
    }

    // Droidcon 4
    private fun fetchPatients(selectedTab: TabData, forceRefresh: Boolean) {
        currentJob?.cancel()
        currentJob = viewModelScope.launch {
            val result = patientRepository.fetchListFor(selectedTab.filter, forceRefresh)

            if (!isActive) return@launch

            result.onFailure {
                val previousList = when (_viewState.value.currentTabId) {
                    selectedTab.id -> _viewState.value.patientList
                    else -> emptyList()
                }
                updateStateToFailure(it, previousList, emptyStateOrNull(previousList, selectedTab))
            }
            result.onSuccess { patientsList ->
                updateStateToLoaded(patientsList, emptyStateOrNull(patientsList, selectedTab))
            }
        }
    }

    private fun emptyStateOrNull(patientList: List<Patient>?, forSelectedTab: TabData): EmptyState? {
        return if (patientList?.isEmpty() == true) {
            getEmptyState(forSelectedTab)
        } else null
    }

    private fun getEmptyState(forSelectedTab: TabData): EmptyState {
        return when (forSelectedTab.filter) {
            PatientFilter.All -> EmptyState(title = StringResource(R.string.empty_all))
            PatientFilter.Bookmarks -> EmptyState(
                title = StringResource(R.string.empty_bookmarks_title),
                description = StringResource(R.string.empty_bookmarks_description)
            )
            is PatientFilter.TypeFilter -> EmptyState(
                title = StringResource(
                    R.string.empty_type,
                    arrayOf(forSelectedTab.title)
                )
            )
        }
    }

    fun onUserTappedOnBookmark(ofPatient: Patient) {
        viewModelScope.launch {
            patientRepository
                .toggleBookmark(ofPatient, toBookmark = ofPatient.isBookmarked.not())
                .onSuccess {
                    val updatedList = updatePatientInList(it)
                    updateStateToLoaded(updatedList)
                }
                .onFailure {
                    updateStateToFailure(it, updatePatientInList(ofPatient))
                }
        }
    }

    private fun updatePatientInList(patient: Patient): List<Patient> {
        return _viewState.value.patientList.map {
            if (it.patientId == patient.patientId) {
                it.copy(patient.isBookmarked, patient.bookmarkCount)
            } else {
                it
            }
        }
    }

    private fun updateToLoadingStateWithSelectedTab(tabId: String) {
        _viewState.update {
            PatientListUiState.Loading(
                emptyList(),
                it.tabs,
                tabId
            )
        }
    }

    private suspend fun updateStateToFailure(
        exception: Throwable,
        patientList: List<Patient>? = null,
        emptyState: EmptyState? = null
    ) = withContext(Dispatchers.Main) {
        _viewState.update {
            PatientListUiState.Error(
                patientList ?: it.patientList,
                it.tabs,
                it.currentTabId,
                emptyState,
                exception
            )
        }
    }

    private suspend fun updateStateToLoaded(
        patients: List<Patient>,
        emptyState: EmptyState? = null
    ) =
        withContext(Dispatchers.Main) {
            _viewState.update {
                PatientListUiState.Loaded(
                    patients,
                    it.tabs,
                    it.currentTabId,
                    emptyState
                )
            }
        }

    fun refreshList() {
        val selectedTab = viewState.value.tabs.find { it.id == viewState.value.currentTabId }
        if (selectedTab != null) {
            fetchPatients(selectedTab, forceRefresh = true)
        }
    }

    fun loadMore() {
        _viewState.update {
            PatientListUiState.LoadingMore(
                it.patientList,
                it.tabs,
                it.currentTabId!!
            )
        }

        val selectedTab = viewState.value.tabs.find { it.id == viewState.value.currentTabId }
        if (selectedTab != null) {
            fetchPatients(selectedTab, forceRefresh = false)
        }
    }
}