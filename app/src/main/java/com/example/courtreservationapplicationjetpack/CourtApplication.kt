package com.example.courtreservationapplicationjetpack

import android.app.Application
import com.example.courtreservationapplicationjetpack.models.AppContainer
import com.example.courtreservationapplicationjetpack.models.AppDataContainer

class CourtApplication : Application() {

    /**
     * AppContainer instance used by the rest of classes to obtain dependencies
     */
    lateinit var container: AppContainer
    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}
