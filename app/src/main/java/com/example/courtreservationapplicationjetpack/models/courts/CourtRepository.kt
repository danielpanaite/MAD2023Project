package com.example.courtreservationapplicationjetpack.models.courts

import com.example.courtreservationapplicationjetpack.models.Reservations
import kotlinx.coroutines.flow.Flow


/**
 * Repository that provides insert, update, delete, and retrieve of [Reservations] from a given data source.
 */
interface CourtRepository {
    /**
     * Retrieve all the courts from the the given data source.
     */
    fun getAllCourts(): Flow<List<Court>>

    /**
     * Retrieve the sports from the given data source .
     */
    fun getSports(): Flow<List<String>>

    /**
     * Retrieve all the courts of that specified sport
     */
    fun getCourtsSport(sport: String):  Flow<List<Court>>


}
