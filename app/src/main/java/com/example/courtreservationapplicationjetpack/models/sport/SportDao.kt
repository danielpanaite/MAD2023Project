package com.example.courtreservationapplicationjetpack.models.sport

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.courtreservationapplicationjetpack.models.user.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull

@Dao
interface SportDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addSportInterest(sport: Sport)

    @Query("SELECT * FROM sport WHERE idUser = :idUser")
    fun getSportUser(idUser: Int): Flow<List<Sport>>

    @Query("SELECT * FROM sport WHERE (sportName =:sportName AND idUser = :idUser)")
    fun getSportByName(sportName: String, idUser: Int): Flow<Sport>

    @Update
    suspend fun updateSport(sport: List<Sport>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateSports(sports: List<Sport>) {
        sports.forEach { sport ->
            val existingSport = getSportByName(sport.sportName, sport.idUser).firstOrNull()
            if (existingSport == null) {
                // If the sport doesn't exist, insert it
                addSportInterest(sport)
            } else {
                // If the sport exists, update its mastery level
                val updatedSport = existingSport.copy(masteryLevel = sport.masteryLevel)
                updateSport(listOf(updatedSport))
            }
        }
    }


}
