package com.example.courtreservationapplicationjetpack.models.sport

import com.example.courtreservationapplicationjetpack.models.user.User
import kotlinx.coroutines.flow.Flow

interface SportRepository {
    /**
     * Retrieve all the reservations from the the given data source.
     */

    suspend fun addSportInterest(sport: Sport)

    fun getSportUser(idUser: Int): Flow<List<Sport>>

    fun getSportByName(sportName: String, idUser: Int): Flow<Sport>


    suspend fun updateSport(sport: Sport)
}