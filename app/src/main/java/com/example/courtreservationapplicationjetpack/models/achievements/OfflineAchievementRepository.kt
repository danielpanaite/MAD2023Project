package com.example.courtreservationapplicationjetpack.models.achievements



import com.example.courtreservationapplicationjetpack.models.sport.Sport
import com.example.courtreservationapplicationjetpack.models.sport.SportDao
import com.example.courtreservationapplicationjetpack.models.sport.SportRepository
import com.example.courtreservationapplicationjetpack.models.user.User
import com.example.courtreservationapplicationjetpack.models.user.UserDao
import com.example.courtreservationapplicationjetpack.models.user.UserRepository
import kotlinx.coroutines.flow.Flow

class OfflineAchievementRepository (private val achievementsDao: AchievementsDao) :
    AchievementsRepository {

    override suspend fun addAchievement(achievement: Achievements) = achievementsDao.addAchievement(achievement)
    override fun getAchivementUser(idUser: Int): Flow<List<Achievements>> = achievementsDao.getAchivementUser(idUser)
    override fun getAchievementsForSport(sportName: String, idUser: Int): Flow<Achievements> = achievementsDao.getAchievementsForSport(sportName, idUser)
    override suspend fun updateAchievement(achievement: Achievements)  = achievementsDao.updateAchievement(achievement)
    override suspend fun deleteAchievement(achievement: Achievements) = achievementsDao.deleteAchievement(achievement)
   override  suspend fun deleteAchievementId(id: Int)= achievementsDao.deleteAchievementId(id)



}