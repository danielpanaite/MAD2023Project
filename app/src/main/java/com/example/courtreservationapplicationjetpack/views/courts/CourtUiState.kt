package com.example.courtreservationapplicationjetpack.views.courts

import com.example.courtreservationapplicationjetpack.models.courts.Court


/**
 * Represents Ui State for an Item.
 */
data class CourtsUiState(
    val isEntryValid: Boolean = false
)

data class CourtDetails(
    val id: Int = 0,
    val name: String = "",
    val address: String = "",
    val center: String = "",
    val sport: String = "",
    val capacity: String = ""
)



/**
 * Extension function to convert [CourtUiState] to [Courts].
 */
fun CourtDetails.toCourt(): Court = Court(
    id = id,
    name = name,
    address = address,
    center = center,
    sport = sport,
    capacity = capacity.toIntOrNull() ?:0,
)



/**
 * Extension function to convert [Courts] to [CourtsUiState]
 */
fun Court.toCourtsUiState(isEntryValid: Boolean = false): CourtsUiState = CourtsUiState(
    //courtDetails = this.toCourtDetails(),
    isEntryValid = isEntryValid
)

/**
 * Extension function to convert [Item] to [ItemDetails]
 */
fun Court.toCourtDetails(): CourtDetails = CourtDetails(

    id = id,
    name = name,
    address = address,
    center = center,
    sport = sport,
    capacity = capacity.toString()
)

