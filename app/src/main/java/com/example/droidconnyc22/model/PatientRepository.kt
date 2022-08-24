package com.example.droidconnyc22.model

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.example.droidconnyc22.model.db.PatientDbDataSource
import com.example.droidconnyc22.model.db.PatientEntity
import com.example.droidconnyc22.model.remote.PatientRemoteDataSource
import com.example.droidconnyc22.model.remote.PatientRemoteMediator
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.parameter.parametersOf

class PatientRepository(
    private val patientDbDataSource: PatientDbDataSource,
    private val patientRemoteDataSource: PatientRemoteDataSource,
) : KoinComponent {

    suspend fun toggleBookmark(forPatient: Patient, toBookmark: Boolean): Result<Patient> {
        patientDbDataSource.toggleBookmark(forPatient, toBookmark)

        val result = kotlin.runCatching {
            patientRemoteDataSource.toggleBookmark(forPatient, toBookmark)
        }

        result.onSuccess {
            patientDbDataSource.updatePatient(it)
        }
        result.onFailure {
            patientDbDataSource.toggleBookmark(forPatient, toBookmark.not())
        }

        return result
    }

    // Droidcon 4
    @OptIn(ExperimentalPagingApi::class)
    fun createPager(filter: PatientFilter): Pager<Int, PatientEntity> {

        val mediator by inject<PatientRemoteMediator> { parametersOf(filter) }

        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                initialLoadSize = PAGE_SIZE,
                prefetchDistance = PAGE_SIZE / 2
            ), remoteMediator = mediator
        ) {
            patientDbDataSource.getPatientListPaging(filter)
        }
    }

    private companion object {
        const val PAGE_SIZE = 3
    }
}