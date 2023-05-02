package com.example.courtreservationapplicationjetpack.reservations

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.example.courtreservationapplicationjetpack.models.ReservationsRepository
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first


/**
 * ViewModel to retrieve and update a reservation from the [ReservationsRepository]'s data source.
 */
class EditReservationViewModel(
    savedStateHandle: SavedStateHandle,
    private val reservationsRepository: ReservationsRepository
) : ViewModel() {

    /**
     * Holds current item ui state
     */
    var reservationsUiState by mutableStateOf(ReservationsUiState())
        private set

    private val reservationId: Int = checkNotNull(savedStateHandle[EditReservationDestination.reservationIdArg])

    init {
        viewModelScope.launch {
            reservationsUiState = reservationsRepository.getReservationStream(reservationId)
                .filterNotNull()
                .first()
                .toReservationsUiState(true)
        }
    }

    /**
     * Update the reservation in the [ReservationsRepository]'s data source
     */
    suspend fun updateReservation() {
        if (validateInput(reservationsUiState.reservationDetails)) {
            reservationsRepository.updateReservation(reservationsUiState.reservationDetails.toReservation())
        }
    }

    /**
     * Updates the [reservationUiState] with the value provided in the argument. This method also triggers
     * a validation for input values.
     */
    fun updateUiState(reservationDetails: ReservationDetails) {
        reservationsUiState =
            ReservationsUiState(reservationDetails = reservationDetails, isEntryValid = validateInput(reservationDetails))
    }

    private fun validateInput(uiState: ReservationDetails = reservationsUiState.reservationDetails): Boolean {
        return with(uiState) {
            user.isNotBlank() && courtId.isNotBlank() && date.isNotBlank() && slot.isNotBlank()
        }
    }
}
