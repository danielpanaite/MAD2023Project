package com.example.courtreservationapplicationjetpack.views.reservations


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.courtreservationapplicationjetpack.models.reservations.Reservation
import com.example.courtreservationapplicationjetpack.models.reservations.ReservationRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

/**
 * View Model to retrieve all reservations in the Room database.
 */
class MyReservationsViewModel(reservationRepository: ReservationRepository) : ViewModel() {
    /**
     * Holds my reservations ui state. The list of reservations are retrieved from [ReservationRepository] and mapped to
     * [ReservationsUiState]
     */
    val myReservationsUiState: StateFlow<MyReservationsUiState> =
        reservationRepository.getAllReservationsStream().map { MyReservationsUiState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = MyReservationsUiState()
            )

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}

/**
 * Ui State for HomeScreen
 */
data class MyReservationsUiState(val reservationList: List<Reservation> = listOf())