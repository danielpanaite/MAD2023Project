package com.example.courtreservationapplicationjetpack.ui.appViewModel

import android.app.Application
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.courtreservationapplicationjetpack.CourtApplication
import com.example.courtreservationapplicationjetpack.courts.AllSportsViewModel
import com.example.courtreservationapplicationjetpack.reservations.ReservationDetailsViewModel
import com.example.courtreservationapplicationjetpack.reservations.EditReservationViewModel

import com.example.courtreservationapplicationjetpack.reservations.ReserveACourtViewModel
import com.example.courtreservationapplicationjetpack.reservations.MyReservationsViewModel

/**
 * Provides Factory to create instance of ViewModel for the entire Court app
 */
object AppViewModelProvider {
    val Factory = viewModelFactory {

        // Initializer for EditReservationViewModel
        initializer {
            EditReservationViewModel(
                this.createSavedStateHandle(),
                courtApplication().container.reservationsRepository
            )
        }
        // Initializer for ReservationDetails View Model
        initializer {
            ReservationDetailsViewModel(
                this.createSavedStateHandle(),
                courtApplication().container.reservationsRepository
            )
        }
        // Initializer for RserveCourtViewModel
        initializer {
            ReserveACourtViewModel(courtApplication().container.reservationsRepository)
        }



                // Initializer for  MyReservationViewModel
                initializer {
                    MyReservationsViewModel(courtApplication().container.reservationsRepository)
                }

        // Initializer for AllSportsViewModel
        initializer {
            AllSportsViewModel(courtApplication().container.courtRepository)
        }



    }
}

/**
 * Extension function to queries for [Application] object and returns an instance of
 * [CourtApplication].
 */

fun CreationExtras.courtApplication(): CourtApplication =
    (this[AndroidViewModelFactory.APPLICATION_KEY] as CourtApplication)