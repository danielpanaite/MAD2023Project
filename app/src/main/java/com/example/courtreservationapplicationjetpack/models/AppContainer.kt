package com.example.courtreservationapplicationjetpack.models

import android.content.Context
import com.example.courtreservationapplicationjetpack.models.courts.CourtRepository
import com.example.courtreservationapplicationjetpack.models.courts.OfflineCourtRepository
import com.example.courtreservationapplicationjetpack.models.reservations.OfflineReservationRepository
import com.example.courtreservationapplicationjetpack.models.reservations.ReservationRepository

/**
 * App container for Dependency injection.
 */
interface AppContainer {
    val reservationRepository: ReservationRepository

    val courtRepository: CourtRepository
}

/**
 * [AppContainer] implementation that provides instance of [OfflineReservationRepository]
 */
class AppDataContainer(private val context: Context) : AppContainer {
    /**
     * Implementation for [ReservationRepository]
     */
    override val reservationRepository: ReservationRepository by lazy {
        OfflineReservationRepository(AppDatabase.getDatabase(context).reservationsDao())
    }
    override val courtRepository: CourtRepository by lazy {
        OfflineCourtRepository(AppDatabase.getDatabase(context).courtDao())
    }
}

//Pass in the ItemDao() instance to the OfflineReservationsRepository constructor.
//Instantiate the database instance by calling getDatabase() on
// the ReservationsDatabase class passing in the context
// call .reservationsDao() to create the instance of Dao.