package com.example.courtreservationapplicationjetpack.reservations


import com.example.courtreservationapplicationjetpack.models.reservations.Reservation

/**
 * View Model to validate and insert items in the Room database.
 */
/*
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
    fun updateUiState(reservationDetails: ReservationDetails) {
        reservationsUiState = ReservationsUiState( reservationDetails = reservationDetails, isEntryValid = validateInput(reservationDetails))
    }


    suspend fun saveReservation() {
        if (validateInput()) {
            reservationsRepository.insertReservation(reservationsUiState.reservationDetails.toReservation())
        }
    }



    private fun validateInput(uiState: ReservationDetails = reservationsUiState.reservationDetails): Boolean {
        return with(uiState) {
            user.isNotBlank() && courtId.isNotBlank() && date.isNotBlank() && slot.isNotBlank()
        }
    }

}
*/



/**
 * Represents Ui State for an Item.
 */
data class ReservationsUiState(
    val reservationDetails: ReservationDetails = ReservationDetails(),
    val isEntryValid: Boolean = false
)

data class ReservationDetails(
    val id: Int? = 0,
    val user: String = "",
    val courtId: String = "",
    val date: String = "",
    val slot: String = "",
    val additions: String = "",
    val people: String = ""
)



/**
 * Extension function to convert [ReservationsUiState] to [Reservation]. If the value of [ReservationsUiState.additions] is
 * not a valid [String], then the addittions will be set to empty. Similarly if the value of
 * [peopleUiState] is not a valid [Int], then the quantity will be set to 2
 */
fun ReservationDetails.toReservation(): Reservation = Reservation(
    id = id,
    user = user.toIntOrNull() ?: 0,
    courtId = courtId.toIntOrNull() ?:0,
    date = date,
    slot = slot,
    additions=additions,
    people = people.toIntOrNull() ?:2
)



/**
 * Extension function to convert [Reservation] to [ReservationsUiState]
 */
fun Reservation.toReservationsUiState(isEntryValid: Boolean = false): ReservationsUiState = ReservationsUiState(
    reservationDetails = this.toReservationDetails(),
    isEntryValid = isEntryValid
)

/**
 * Extension function to convert [Item] to [ItemDetails]
 */
fun Reservation.toReservationDetails(): ReservationDetails = ReservationDetails(
    id = id,
    user = user.toString(),
    courtId = courtId.toString(),
    date = date,
    slot = slot,
    additions=additions,
    people = people.toString(),
)



