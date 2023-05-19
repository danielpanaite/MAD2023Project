package com.example.courtreservationapplicationjetpack.views.courts

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.courtreservationapplicationjetpack.models.courts.Court
import com.example.courtreservationapplicationjetpack.models.courts.CourtRepository
import com.example.courtreservationapplicationjetpack.models.reservations.ReservationRepository
import com.example.courtreservationapplicationjetpack.views.reservations.ReservationDetails
import com.example.courtreservationapplicationjetpack.views.reservations.ReservationsUiState
import com.example.courtreservationapplicationjetpack.views.reservations.toReservation
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

/**
 * View Model to validate and insert items in the Room database.
 */
class CourtReservationViewModel(
    savedStateHandle: SavedStateHandle,
    private val reservationRepository: ReservationRepository,
    private val courtRepository: CourtRepository
) : ViewModel() {

    private val reservationId: Int = checkNotNull(savedStateHandle[CourtReservation.courtArg])

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }

    /**
     * Holds current reservation ui state
     */
    var reservationsUiState by mutableStateOf(ReservationsUiState())
        private set

    val uiState: StateFlow<CourtReservationUiState> =
        courtRepository.getCourtsWithId(listOf(reservationId))
            .filterNotNull()
            .map {
                CourtReservationUiState(courtDetails = it[0])
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = CourtReservationUiState()
            )

    data class CourtReservationUiState(
        val courtDetails: Court = Court(0,"","","","",0)
    )

    /**
     * Updates the [reservationsUiState] with the value provided in the argument. This method also triggers
     * a validation for input values.
     */
    fun updateUiState(reservationDetails: ReservationDetails) {
        reservationsUiState = ReservationsUiState( reservationDetails = reservationDetails, isEntryValid = validateInput(reservationDetails))
    }

    suspend fun saveReservation() {
        if (validateInput()) {
            reservationRepository.insertReservation(reservationsUiState.reservationDetails.toReservation())
        }
    }

    private fun validateInput(uiState: ReservationDetails = reservationsUiState.reservationDetails): Boolean {
        return with(uiState) {
            user.isNotBlank() && courtId.isNotBlank() && date.isNotBlank() && slot.isNotBlank()
        }
    }

}



/*



/**
 * Represents Ui State for an Item.
 */
data class ReservationsUiState(
    val reservationDetails: ReservationDetails = ReservationDetails(),
    val isEntryValid: Boolean = false
)

data class ReservationDetails(
    val id: Int = 0,
    val user: String = "",
    val courtId: String = "",
    val date: String = "",
    val slot: String = "",
    val additions: String = "",
    val people: String = ""
)



/**
 * Extension function to convert [ReservationsUiState] to [Reservations]. If the value of [ReservationsUiState.additions] is
 * not a valid [String], then the addittions will be set to empty. Similarly if the value of
 * [peopleUiState] is not a valid [Int], then the quantity will be set to 2
 */
fun ReservationDetails.toReservation(): Reservations = Reservations(
    id = id,
    user = user.toIntOrNull() ?: 0,
    courtId = courtId.toIntOrNull() ?:0,
    date = date,
    slot = slot,
    additions=additions,
    people = people.toIntOrNull() ?:2
)



/**
 * Extension function to convert [Reservations] to [ReservationsUiState]
 */
fun Reservations.toReservationsUiState(isEntryValid: Boolean = false): ReservationsUiState = ReservationsUiState(
    reservationDetails = this.toReservationDetails(),
    isEntryValid = isEntryValid
)

/**
 * Extension function to convert [Item] to [ItemDetails]
 */
fun Reservations.toReservationDetails(): ReservationDetails = ReservationDetails(

    id = id,
    user = user.toString(),
    courtId = courtId.toString(),
    date = date,
    slot = slot,
    additions=additions,
    people = people.toString(),
)

*/