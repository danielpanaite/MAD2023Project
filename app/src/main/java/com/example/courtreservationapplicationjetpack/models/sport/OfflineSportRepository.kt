package com.example.courtreservationapplicationjetpack.models.sport

import com.example.courtreservationapplicationjetpack.models.user.User
import com.example.courtreservationapplicationjetpack.models.user.UserDao
import com.example.courtreservationapplicationjetpack.models.user.UserRepository
import kotlinx.coroutines.flow.Flow

class OfflineSportRepository (private val sportDao: SportDao) :
    SportRepository {

    override suspend fun addSportInterest(sport: Sport)= sportDao.addSportInterest(sport)

    override fun getSportUser(idUser: Int): Flow<List<Sport>> = sportDao.getSportUser(idUser)

    override fun getSportByName(sportName: String, idUser: Int): Flow<Sport> = sportDao.getSportByName(sportName, idUser)

    override suspend fun updateSport(sport: List<Sport>) = sportDao.updateSport(sport)

    override suspend fun insertOrUpdateSports(sports: List<Sport>) = sportDao.insertOrUpdateSports(sports)


}