package com.example.droidconnyc22.model.remote

import retrofit2.http.*

interface PatientService {

    @GET("patients")
    suspend fun getAllPatients(
        @Query("limit") limit: Int,
        @Query("lastCursor") lastPatientId: String? = null
    ): List<PatientRemote>

    @GET("patients/{type}")
    suspend fun getPatientsByType(
        @Path("type") forType: PatientType,
        @Query("limit") limit: Int,
        @Query("lastCursor") lastPatientId: String? = null
    ): List<PatientRemote>

    @GET("patients?bookmarked=true")
    suspend fun getBookmarkedPatients(
        @Query("limit") limit: Int,
        @Query("lastCursor") lastPatientId: String? = null
    ): List<PatientRemote>

    @PUT("patients/{patientId}/bookmark")
    suspend fun setPatientBookmark(
        @Body bookmarkRequest: BookmarkRequest,
        @Path("patientId") patientId: String
    ): PatientRemote
}