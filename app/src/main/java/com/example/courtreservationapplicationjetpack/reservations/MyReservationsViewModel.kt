package com.example.courtreservationapplicationjetpack.reservations


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.courtreservationapplicationjetpack.models.Reservations
import com.example.courtreservationapplicationjetpack.models.ReservationsRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

/**
 * View Model to retrieve all reservations in the Room database.
 */
class MyReservationsViewModel(reservationsRepository: ReservationsRepository) : ViewModel() {
    /**
     * Holds my reservations ui state. The list of reservations are retrieved from [ReservationsRepository] and mapped to
     * [ReservationsUiState]
     */
    val myReservationsUiState: StateFlow<MyReservationsUiState> =
        reservationsRepository.getAllReservationsStream().map { MyReservationsUiState(it) }
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
data class MyReservationsUiState(val reservationsList: List<Reservations> = listOf())
