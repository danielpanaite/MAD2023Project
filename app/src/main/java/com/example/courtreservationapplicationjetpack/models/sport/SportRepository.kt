package com.example.courtreservationapplicationjetpack.models.sport

import kotlinx.coroutines.flow.Flow

interface SportRepository {
    /**
     * Retrieve all the reservations from the the given data source.
     */

    suspend fun addSportInterest(sport: Sport)

    fun getSportUser(idUser: Int): Flow<List<Sport>>

    fun getSportByName(sportName: String, idUser: Int): Flow<Sport>


    suspend fun updateSport(sport: List<Sport>)

    suspend fun insertOrUpdateSports(sports: List<Sport>)

    suspend fun deleteSport(sports: List<Sport>)

    suspend fun deleteSportByName(sportName: String, userId: Int)

    suspend fun updateSportAchievements(sportName: String, idUser: Int, achievements: String?)

    fun getAchievements(idUser: Int): Flow<List<String>>

}
