package com.example.droidconnyc22

import android.app.Application
import androidx.room.Room
import com.example.droidconnyc22.model.PatientRepository
import com.example.droidconnyc22.model.TabsRepository
import com.example.droidconnyc22.model.db.PagingDao
import com.example.droidconnyc22.model.db.PatientDB
import com.example.droidconnyc22.model.db.PatientDao
import com.example.droidconnyc22.model.db.PatientDbDataSource
import com.example.droidconnyc22.model.remote.PatientRemoteDataSource
import com.example.droidconnyc22.model.remote.PatientRemoteMediator
import com.example.droidconnyc22.model.remote.PatientService
import com.example.droidconnyc22.viewmodel.PatientViewModel
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import retrofit2.Retrofit
import timber.log.Timber
import java.util.concurrent.Executors

@OptIn(ExperimentalSerializationApi::class)
val patientDbModule = module {

    fun provideOkHttp(interceptor: Interceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .addInterceptor(interceptor)
            .build()
    }

    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        val contentType = "application/json".toMediaType()
        return Retrofit.Builder()
            .baseUrl("http://192.168.1.81:3000/")
            .addConverterFactory(Json.asConverterFactory(contentType))
            .client(okHttpClient)
            .build()
    }

    fun provideDB(application: Application): PatientDB {
        return Room
            .databaseBuilder(application, PatientDB::class.java, "patient")
            .setQueryCallback({ sqlQuery, bindArgs ->
                Timber.d("SqlQuery: $sqlQuery\nbindArgs: $bindArgs")
            }, Executors.newSingleThreadExecutor())
            .build()
    }

    fun providePatientDao(patientDB: PatientDB): PatientDao = patientDB.patientDao
    fun providePagingDao(patientDB: PatientDB): PagingDao = patientDB.pagingDao

    single {
        Interceptor { chain ->
            chain.proceed(chain.request().newBuilder().apply {
                header("device_id", DDNYCApp.INSTANCE.getDeviceId())
            }.build())
        }
    }

    // Provide DB
    singleOf(::provideDB)
    factoryOf(::providePatientDao)
    factoryOf(::providePagingDao)

    // Provide network
    singleOf(::provideOkHttp)
    singleOf(::provideRetrofit)
    single { get<Retrofit>().create(PatientService::class.java) }

    // Provide data source
    singleOf(::PatientDbDataSource)
    singleOf(::PatientRemoteDataSource)
    singleOf(::PatientRepository)
    factory { params ->
        PatientRemoteMediator(
            filter = params.get(),
            patientService = get(),
            patientDao = get(),
            pagingDao = get()
        )
    }

    // Provide tabs repository
    singleOf(::TabsRepository)

    // Provide view model
    viewModelOf(::PatientViewModel)
}