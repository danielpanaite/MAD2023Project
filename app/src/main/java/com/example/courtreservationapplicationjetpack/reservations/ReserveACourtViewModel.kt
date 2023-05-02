package com.example.courtreservationapplicationjetpack.reservations


import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.courtreservationapplicationjetpack.models.ReservationsRepository

/**
 * View Model to validate and insert items in the Room database.
 */
class ReserveACourtViewModel(private val reservationsRepository: ReservationsRepository) : ViewModel() {

    /**
     * Holds current reservation ui state
     */
    var reservationsUiState by mutableStateOf(ReservationsUiState())
        private set

    /**
     * Updates the [reservationsUiState] with the value provided in the argument. This method also triggers
     * a validation for input values.
     */
    fun updateUiState(newReservationUiState: ReservationsUiState) {
        reservationsUiState = newReservationUiState.copy( actionEnabled = newReservationUiState.isValid())
    }

    suspend fun saveReservation() {
        if (reservationsUiState.isValid()) {
            reservationsRepository.insertReservation(reservationsUiState.toReservations())
        }
    }
}