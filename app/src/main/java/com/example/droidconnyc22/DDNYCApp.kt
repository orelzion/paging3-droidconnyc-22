package com.example.droidconnyc22

import android.app.Application
import androidx.core.content.edit
import androidx.preference.PreferenceManager
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin
import java.util.*

class DDNYCApp : Application() {

    companion object {
        lateinit var INSTANCE: DDNYCApp
            private set
    }

    override fun onCreate() {
        super.onCreate()

        INSTANCE = this

        startKoin {
            androidLogger()
            androidContext(this@DDNYCApp)
            modules(patientDbModule)
        }
    }

    fun getDeviceId(): String {
        val deviceIdKey = "deviceID"
        val savedDeviceId = PreferenceManager.getDefaultSharedPreferences(this)
            .getString(deviceIdKey, null)

        if (savedDeviceId != null) return savedDeviceId

        val deviceId = UUID.randomUUID().toString()
        PreferenceManager.getDefaultSharedPreferences(this)
            .edit {
                putString(deviceIdKey, deviceId)
            }

        return deviceId
    }
}