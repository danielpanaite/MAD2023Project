package com.example.courtreservationapplicationjetpack.models

import android.content.Context
import com.example.courtreservationapplicationjetpack.models.achievements.AchievementsRepository
import com.example.courtreservationapplicationjetpack.models.achievements.OfflineAchievementRepository
import com.example.courtreservationapplicationjetpack.models.courts.CourtRepository
import com.example.courtreservationapplicationjetpack.models.courts.OfflineCourtRepository
import com.example.courtreservationapplicationjetpack.models.reservations.OfflineReservationRepository
import com.example.courtreservationapplicationjetpack.models.reservations.ReservationRepository
import com.example.courtreservationapplicationjetpack.models.reviews.OfflineReviewRepository
import com.example.courtreservationapplicationjetpack.models.reviews.ReviewRepository
import com.example.courtreservationapplicationjetpack.models.sport.OfflineSportRepository
import com.example.courtreservationapplicationjetpack.models.sport.SportRepository
import com.example.courtreservationapplicationjetpack.models.user.OfflineUserRepository
import com.example.courtreservationapplicationjetpack.models.user.UserRepository

/**
 * App container for Dependency injection.
 */
interface AppContainer {
    val reservationRepository: ReservationRepository

    val courtRepository: CourtRepository

    val userRepository: UserRepository

    val sportRepository: SportRepository

    val reviewRepository: ReviewRepository

    val achievementsRepository: AchievementsRepository
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

    override val userRepository: UserRepository by lazy {
        OfflineUserRepository(AppDatabase.getDatabase(context).userDao())
    }

    override val sportRepository: SportRepository by lazy {
        OfflineSportRepository(AppDatabase.getDatabase(context).sportDao())
    }

    override val reviewRepository: ReviewRepository by lazy {
        OfflineReviewRepository(AppDatabase.getDatabase(context).reviewDao())
    }

    override val achievementsRepository: AchievementsRepository by lazy {
        OfflineAchievementRepository(AppDatabase.getDatabase(context).achievementsDao())
    }
}

//Pass in the ItemDao() instance to the OfflineReservationsRepository constructor.
//Instantiate the database instance by calling getDatabase() on
// the ReservationsDatabase class passing in the context
// call .reservationsDao() to create the instance of Dao.