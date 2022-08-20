package com.example.droidconnyc22.model.remote

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.example.droidconnyc22.model.PatientFilter
import com.example.droidconnyc22.model.db.PagingDao
import com.example.droidconnyc22.model.db.PagingEntity
import com.example.droidconnyc22.model.db.PatientDao
import com.example.droidconnyc22.model.db.PatientEntity
import com.example.droidconnyc22.model.toPatientEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@OptIn(ExperimentalPagingApi::class)
class PatientRemoteMediator(
    private val filter: PatientFilter,
    private val patientService: PatientService,
    private val patientDao: PatientDao,
    private val pagingDao: PagingDao
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
        val pagingProperties: PagingEntity? = when (loadType) {
            LoadType.REFRESH -> null
            LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
            LoadType.APPEND -> {
                val properties = withContext(Dispatchers.IO) {
                    pagingDao.getFor(filter.filterId).firstOrNull()
                }

                if (properties?.hasReachLimit == true) {
                    return MediatorResult.Success(endOfPaginationReached = true)
                } else properties
            }
        }

        return try {
            val reachedEnd = withContext(Dispatchers.IO) {
                loadMore(
                    filter,
                    pagingProperties,
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
        pagingProperties: PagingEntity?,
        pageSize: Int
    ): ReachedEnd {
        val patients = when (filter) {
            PatientFilter.All -> fetchAll(pageSize, pagingProperties)
            PatientFilter.Bookmarks -> fetchBookmarks(pageSize, pagingProperties)
            is PatientFilter.TypeFilter -> fetchFiltered(filter, pageSize, pagingProperties)
        }

        // Remove previous results if requested
        if (pagingProperties == null) {
            pagingDao.clear(filter.filterId)
            patientDao.removeAllFor(filter.filterId)
        }

        // Save Locally
        patientDao.createOrUpdate(
            patients.map {
                it.toPatientEntity(filter.filterId)
            }
        )

        val reachedEnd = ReachedEnd(patients.size < pageSize)

        // Update paging properties
        pagingDao.createOrUpdate(
            PagingEntity(
                filterId = filter.filterId,
                lastCursorId = patients.last().patientId,
                hasReachLimit = reachedEnd.value
            )
        )

        return reachedEnd
    }

    private suspend fun fetchFiltered(
        filter: PatientFilter.TypeFilter,
        pageSize: Int,
        pagingProperties: PagingEntity?
    ) = patientService.getPatientsByType(
        filter.type,
        limit = pageSize,
        lastPatientId = pagingProperties?.lastCursorId
    )

    private suspend fun fetchBookmarks(
        pageSize: Int,
        pagingProperties: PagingEntity?
    ) = patientService.getBookmarkedPatients(
        limit = pageSize,
        lastPatientId = pagingProperties?.lastCursorId
    )

    private suspend fun fetchAll(
        pageSize: Int,
        pagingProperties: PagingEntity?
    ) = patientService.getAllPatients(
        limit = pageSize,
        lastPatientId = pagingProperties?.lastCursorId
    )

}

@JvmInline
value class ReachedEnd(val value: Boolean)