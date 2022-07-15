package com.example.droidconnyc22.model.remote

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

interface PatientService {

    @GET("patients")
    suspend fun getAllPatients(): List<PatientRemote>

    @GET("patients/{type}")
    suspend fun getPatientsByType(@Path("type") forType: PatientType): List<PatientRemote>

    @GET("patients?bookmarked=true")
    suspend fun getBookmarkedPatients(): List<PatientRemote>

    @PUT("patients/{patientId}/bookmark")
    suspend fun setPatientBookmark(
        @Body bookmarkRequest: BookmarkRequest,
        @Path("patientId") patientId: String
    ): PatientRemote
}