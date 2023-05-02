package com.example.courtreservationapplicationjetpack.reservations


import com.example.courtreservationapplicationjetpack.models.Reservations
import java.util.Date

/*
Created a database. To save the app's transient data
and to also access the database to update the ViewModels.
 Thr ViewModels interact with the database via the DAO and
 provide data to the UI. All database operations need to
 be run away from the main UI thread; you do so with coroutines
 and viewModelScope.
 */

/**
 * Represents Ui State for a Reservation.
 */

/*
data class ReservationsUiState(
    val id: Int = 0,
    val user: String = "",
    val courtId: String = "",
    val date: String = "",
    val slot: String = "",
    val additions: String = "",
    val people: String = "",
    val actionEnabled: Boolean = false

)

/**
 * Extension function to convert [ReservationsUiState] to [Reservations]. If the value of [ReservationsUiState.additions] is
 * not a valid [String], then the addittions will be set to empty. Similarly if the value of
 * [peopleUiState] is not a valid [Int], then the quantity will be set to 2
 */
fun ReservationsUiState.toReservations(): Reservations = Reservations(
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
fun Reservations.toReservationsUiState(actionEnabled: Boolean = false): ReservationsUiState = ReservationsUiState(
    id = id,
    user = user.toString(),
    courtId = courtId.toString(),
    date = date,
    slot = slot,
    additions=additions,
    people = people.toString(),
    actionEnabled = actionEnabled
)

fun ReservationsUiState.isValid() : Boolean {
    return user.isNotBlank() && courtId.isNotBlank() && date.isNotBlank() && slot.isNotBlank()
}


*/