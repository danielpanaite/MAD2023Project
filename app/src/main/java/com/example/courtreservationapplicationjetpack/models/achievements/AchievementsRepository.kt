package com.example.courtreservationapplicationjetpack.models.achievements

import com.example.courtreservationapplicationjetpack.models.sport.Sport
import kotlinx.coroutines.flow.Flow


interface AchievementsRepository {

    suspend fun addAchievement(achievement: Achievements)
    fun getAchivementUser(idUser: Int): Flow<List<Achievements>>
    fun getAchievementsForSport(sportName: String, idUser: Int): Flow<Achievements>
    suspend fun updateAchievement(achievement: Achievements)
    suspend fun deleteAchievement(achievement: Achievements)
    suspend fun deleteAchievementId(id: Int)

}
