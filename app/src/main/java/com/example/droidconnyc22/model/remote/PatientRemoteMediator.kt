package com.example.droidconnyc22.model.remote

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.example.droidconnyc22.model.PatientFilter
import com.example.droidconnyc22.model.db.PatientDao
import com.example.droidconnyc22.model.db.PatientEntity
import com.example.droidconnyc22.model.toPatientEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import timber.log.Timber

@OptIn(ExperimentalPagingApi::class)
class PatientRemoteMediator(
    private val filter: PatientFilter,
    private val patientService: PatientService,
    private val patientDao: PatientDao
) : RemoteMediator<Int, PatientEntity>() {

    /**
     * If we don't have anything loaded to this filter, launch a refresh load
     * Otherwise skip initial launch and load from the saved Room cache
     */
    override suspend fun initialize() = withContext(Dispatchers.IO) {
        if (patientDao.getFilterSize(filter.filterId) == 0) {
            InitializeAction.LAUNCH_INITIAL_REFRESH
        } else {
            InitializeAction.SKIP_INITIAL_REFRESH
        }
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, PatientEntity>
    ): MediatorResult {

        Timber.d("loadType: ${loadType.name}")
        Timber.d("last item: ${state.lastItemOrNull()}")

        // Droidcon 8
        val lastCursorId: String? = when (loadType) {
            LoadType.REFRESH -> null
            LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
            LoadType.APPEND -> {
                state.lastItemOrNull()?.patientId ?: return MediatorResult.Success(endOfPaginationReached = false)
            }
        }

        return try {
            val reachedEnd = withContext(Dispatchers.IO) {
                loadMore(
                    filter,
                    lastCursorId,
                    pageSize = state.config.pageSize
                )
            }

            MediatorResult.Success(endOfPaginationReached = reachedEnd.value)
        } catch (e: Exception) {
            MediatorResult.Error(e)
        }
    }

    private suspend fun loadMore(
        filter: PatientFilter,
        lastCursorId: String?,
        pageSize: Int
    ): ReachedEnd {
        val patients = when (filter) {
            PatientFilter.All -> fetchAll(pageSize, lastCursorId)
            PatientFilter.Bookmarks -> fetchBookmarks(pageSize, lastCursorId)
            is PatientFilter.TypeFilter -> fetchFiltered(filter, pageSize, lastCursorId)
        }

        // To increase the effect
        delay(3000)

        // Remove previous results if requested
        if (lastCursorId == null) {
            patientDao.removeAllFor(filter.filterId)
        }

        // Save Locally
        patientDao.createOrUpdate(
            patients.map {
                it.toPatientEntity(filter.filterId)
            }
        )

        return ReachedEnd(patients.size < pageSize)
    }

    private suspend fun fetchFiltered(
        filter: PatientFilter.TypeFilter,
        pageSize: Int,
        lastCursorId: String?
    ) = patientService.getPatientsByType(
        filter.type,
        limit = pageSize,
        lastPatientId = lastCursorId
    )

    private suspend fun fetchBookmarks(
        pageSize: Int,
        lastCursorId: String?
    ) = patientService.getBookmarkedPatients(
        limit = pageSize,
        lastPatientId = lastCursorId
    )

    private suspend fun fetchAll(
        pageSize: Int,
        lastCursorId: String?
    ) = patientService.getAllPatients(
        limit = pageSize,
        lastPatientId = lastCursorId
    )

}

@JvmInline
value class ReachedEnd(val value: Boolean)