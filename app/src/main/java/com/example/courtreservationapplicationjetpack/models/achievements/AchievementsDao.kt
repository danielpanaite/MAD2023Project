package com.example.courtreservationapplicationjetpack.models.achievements

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.courtreservationapplicationjetpack.models.sport.Sport
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull

@Dao
interface AchievementsDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addAchievement(achievement: Achievements)

    @Query("SELECT * FROM achievements WHERE idUser = :idUser")
    fun getAchivementUser(idUser: Int): Flow<List<Achievements>>

    @Query("SELECT * FROM achievements WHERE (sportName =:sportName AND idUser = :idUser)")
    fun getAchievementsForSport(sportName: String, idUser: Int): Flow<Achievements>

    @Update
    suspend fun updateAchievement(achievement: Achievements)

    @Delete
    suspend fun deleteAchievement(achievement: Achievements)

    @Query("DELETE FROM achievements WHERE id =:id")
    suspend fun deleteAchievementId(id: Int)


}
