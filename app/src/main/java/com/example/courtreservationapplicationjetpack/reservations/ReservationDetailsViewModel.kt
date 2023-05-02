package com.example.courtreservationapplicationjetpack.reservations

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.courtreservationapplicationjetpack.models.ReservationsRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch




/**
 * ViewModel to retrieve, update and delete an item from the [ReservationsRepository]'s data source.
 */
/*
class DetailsReservationViewModel(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    /**
     * Holds current reservation ui state
     */
    var reservationsUiState by mutableStateOf(ReservationsUiState())
        private set

    //private val reservationId: Int = checkNotNull(savedStateHandle[ItemEditDestination.itemIdArg])

}
*/
class ReservationDetailsViewModel(
    savedStateHandle: SavedStateHandle,
    private val reservationsRepository: ReservationsRepository,
) : ViewModel() {

    private val reservationId: Int = checkNotNull(savedStateHandle[ReservationDetailsDestination.reservationIdArg])

    /**
     * Holds the reservations details ui state. The data is retrieved from [ReservationsRepository] and mapped to
     * the UI state.
     */
    val uiState: StateFlow<ReservationDetailsUiState> =
        reservationsRepository.getReservationStream(reservationId)
            .filterNotNull()
            .map {
                ReservationDetailsUiState(reservationDetails = it.toReservationDetails())
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = ReservationDetailsUiState()
            )



    /**
     * Deletes the reservation from the [ReservationsRepository]'s data source.
     */
    suspend fun deleteReservation() {
        reservationsRepository.deleteReservation(uiState.value.reservationDetails.toReservation())
    }

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}

/**
 * UI state for ReservationDetailsScreen
 */
data class ReservationDetailsUiState(
    val reservationDetails: ReservationDetails = ReservationDetails()
)