package com.example.courtreservationapplicationjetpack

import android.app.Application
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.courtreservationapplicationjetpack.CourtApplication
import com.example.courtreservationapplicationjetpack.reservations.EditReservationViewModel

import com.example.courtreservationapplicationjetpack.reservations.ReserveACourtViewModel
import com.example.courtreservationapplicationjetpack.reservations.MyReservationsViewModel


/**
 * Provides Factory to create instance of ViewModel for the entire Court app
 */
object AppViewModelProvider {
    val Factory = viewModelFactory {
        // Initializer for ReservationEdit View Model
        initializer {
            EditReservationViewModel(
                this.createSavedStateHandle()
            )
        }
        // Initializer for RserveCourtViewModel
        initializer {
            ReserveACourtViewModel(courtApplication().container.reservationsRepository)
        }
        /*
                // Initializer for ItemDetailsViewModel
                initializer {
                    ItemDetailsViewModel(
                        this.createSavedStateHandle()
                    )
                }
*/
                // Initializer for HomeViewModel MyReservationViewModel
                initializer {
                    MyReservationsViewModel()
                }
    }
}

/**
 * Extension function to queries for [Application] object and returns an instance of
 * [CourtApplication].
 */

fun CreationExtras.courtApplication(): CourtApplication =
    (this[AndroidViewModelFactory.APPLICATION_KEY] as CourtApplication)