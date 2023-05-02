package com.example.courtreservationapplicationjetpack.models

import android.content.Context

/**
 * App container for Dependency injection.
 */
interface AppContainer {
    val reservationsRepository: ReservationsRepository
}

/**
 * [AppContainer] implementation that provides instance of [OfflineReservationsRepository]
 */
class AppDataContainer(private val context: Context) : AppContainer {
    /**
     * Implementation for [ReservationsRepository]
     */
    override val reservationsRepository: ReservationsRepository by lazy {
        OfflineReservationsRepository(ReservationsDatabase.getDatabase(context).reservationsDao())
    }
}

//Pass in the ItemDao() instance to the OfflineReservationsRepository constructor.
//Instantiate the database instance by calling getDatabase() on
// the ReservationsDatabase class passing in the context
// call .reservationsDao() to create the instance of Dao.