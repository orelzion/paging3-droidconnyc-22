package com.example.droidconnyc22

import android.app.Application
import androidx.room.Room
import com.example.droidconnyc22.model.PatientRepository
import com.example.droidconnyc22.model.TabsRepository
import com.example.droidconnyc22.model.db.PatientDB
import com.example.droidconnyc22.model.db.PatientDao
import com.example.droidconnyc22.model.db.PatientDbDataSource
import com.example.droidconnyc22.model.remote.PatientRemoteDataSource
import com.example.droidconnyc22.viewmodel.PatientViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val patientDbModule = module {

    fun provideDB(application: Application): PatientDB {
        return Room
            .databaseBuilder(application, PatientDB::class.java, "patient")
            .build()
    }

    fun providePatientDao(patientDB: PatientDB): PatientDao {
        return patientDB.patientDao
    }

    // Provide DB
    singleOf(::provideDB)
    factoryOf(::providePatientDao)

    // Provide data source
    singleOf(::PatientDbDataSource)
    singleOf(::PatientRemoteDataSource)
    singleOf(::PatientRepository)

    // Provide tabs repository
    singleOf(::TabsRepository)

    // Provide view model
    viewModelOf(::PatientViewModel)
}