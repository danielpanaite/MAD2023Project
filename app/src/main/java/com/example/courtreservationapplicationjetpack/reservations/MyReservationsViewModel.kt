package com.example.courtreservationapplicationjetpack.reservations


import androidx.lifecycle.ViewModel
import com.example.courtreservationapplicationjetpack.models.Reservations

/**
 * View Model to retrieve all items in the Room database.
 */
class MyReservationsViewModel : ViewModel() {

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}

/**
 * Ui State for HomeScreen
 */
data class MyReservationsUiState(val reservationsList: List<Reservations> = listOf())
