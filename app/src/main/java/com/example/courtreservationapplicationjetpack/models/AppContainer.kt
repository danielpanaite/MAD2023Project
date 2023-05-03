package com.example.courtreservationapplicationjetpack.models

import android.content.Context
import com.example.courtreservationapplicationjetpack.models.courts.CourtRepository
import com.example.courtreservationapplicationjetpack.models.courts.OfflineCourtRepository

/**
 * App container for Dependency injection.
 */
interface AppContainer {
    val reservationsRepository: ReservationsRepository

    val courtRepository: CourtRepository
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
    override val courtRepository: CourtRepository by lazy {
        OfflineCourtRepository(ReservationsDatabase.getDatabase(context).courtDao())
    }
}

//Pass in the ItemDao() instance to the OfflineReservationsRepository constructor.
//Instantiate the database instance by calling getDatabase() on
// the ReservationsDatabase class passing in the context
// call .reservationsDao() to create the instance of Dao.