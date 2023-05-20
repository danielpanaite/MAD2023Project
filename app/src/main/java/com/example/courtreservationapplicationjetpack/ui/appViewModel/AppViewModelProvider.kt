package com.example.courtreservationapplicationjetpack.ui.appViewModel

import android.app.Application
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.courtreservationapplicationjetpack.CourtApplication
import com.example.courtreservationapplicationjetpack.views.courts.AllSportsViewModel
import com.example.courtreservationapplicationjetpack.views.courts.CourtReservationViewModel
import com.example.courtreservationapplicationjetpack.views.courts.CourtsAvailableViewModel
import com.example.courtreservationapplicationjetpack.views.profile.AchievementsViewModel
import com.example.courtreservationapplicationjetpack.views.profile.EditProfileViewModel
import com.example.courtreservationapplicationjetpack.views.profile.ProfileViewModel
import com.example.courtreservationapplicationjetpack.views.profile.SportPreferencesViewModel
import com.example.courtreservationapplicationjetpack.views.reservations.ReservationDetailsViewModel
import com.example.courtreservationapplicationjetpack.views.reservations.EditReservationViewModel

import com.example.courtreservationapplicationjetpack.views.reservations.MyReservationsViewModel

/**
 * Provides Factory to create instance of ViewModel for the entire Court app
 */
object AppViewModelProvider {
    val Factory = viewModelFactory {

        // Initializer for EditReservationViewModel
        initializer {
            EditReservationViewModel(
                this.createSavedStateHandle(),
                courtApplication().container.reservationRepository
            )
        }

        initializer {
            EditProfileViewModel(
                this.createSavedStateHandle(),
                courtApplication().container.userRepository
            )
        }
        // Initializer for ReservationDetails View Model
        initializer {
            ReservationDetailsViewModel(
                this.createSavedStateHandle(),
                courtApplication().container.reservationRepository
            )
        }
        initializer {
            ProfileViewModel(
                this.createSavedStateHandle(),
                courtApplication().container.userRepository
            )
        }

        // Initializer for  MyReservationViewModel
        initializer {
            MyReservationsViewModel(courtApplication().container.reservationRepository)
        }

        // Initializer for AllSportsViewModel
        initializer {
            AllSportsViewModel(courtApplication().container.courtRepository)
        }

        // Initializer for CourtAvailableViewModel
        initializer {
            CourtsAvailableViewModel(this.createSavedStateHandle(), courtApplication().container.courtRepository)
        }

        // Initializer for CourtReservationViewModel
        initializer {
            CourtReservationViewModel(courtApplication().container.reservationRepository)
        }

        initializer {
            SportPreferencesViewModel(courtApplication().container.courtRepository, courtApplication().container.sportRepository)
        }

        initializer {
            AchievementsViewModel(courtApplication().container.sportRepository, courtApplication().container.achievementsRepository)
        }



    }
}

/**
 * Extension function to queries for [Application] object and returns an instance of
 * [CourtApplication].
 */

fun CreationExtras.courtApplication(): CourtApplication =
    (this[AndroidViewModelFactory.APPLICATION_KEY] as CourtApplication)