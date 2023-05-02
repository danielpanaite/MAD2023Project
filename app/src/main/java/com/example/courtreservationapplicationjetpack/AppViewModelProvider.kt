package com.example.courtreservationapplicationjetpack

import android.app.Application
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
/*import com.example.courtreservationapplicationjetpack.routes.reservations.ReservationEditViewModel
import com.example.courtreservationapplicationjetpack.routes.reservations.ReservationEntryViewModel
import com.example.courtreservationapplicationjetpack.CourtApplication



/**
 * Provides Factory to create instance of ViewModel for the entire Court app
 */
object AppViewModelProvider {
    val Factory = viewModelFactory {
        // Initializer for ItemEditViewModel
        initializer {
            ReservationEditViewModel(
                this.createSavedStateHandle()
            )
        }
        // Initializer for ItemEntryViewModel
        initializer {
            ReservationEntryViewModel(courtApplication().container.reservationsRepository)
        }
        /*
                // Initializer for ItemDetailsViewModel
                initializer {
                    ItemDetailsViewModel(
                        this.createSavedStateHandle()
                    )
                }

                // Initializer for HomeViewModel
                initializer {
                    HomeViewModel()
                }*/
    }
}

/**
 * Extension function to queries for [Application] object and returns an instance of
 * [CourtApplication].
 */
fun CreationExtras.courtApplication(): CourtApplication =
    (this[AndroidViewModelFactory.APPLICATION_KEY] as CourtApplication)
*/