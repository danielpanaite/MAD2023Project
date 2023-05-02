package com.example.courtreservationapplicationjetpack.reservations

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.courtreservationapplicationjetpack.models.ReservationsRepository

/**
 * ViewModel to retrieve and update an item from the [ReservationsRepository]'s data source.
 */
class EditReservationViewModel(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    /**
     * Holds current reservation ui state
     */
    var reservationsUiState by mutableStateOf(ReservationsUiState())
        private set

    //private val reservationId: Int = checkNotNull(savedStateHandle[ItemEditDestination.itemIdArg])

}