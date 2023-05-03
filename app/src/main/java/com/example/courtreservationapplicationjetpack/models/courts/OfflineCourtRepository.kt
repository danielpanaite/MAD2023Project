package com.example.courtreservationapplicationjetpack.models.courts


import com.example.courtreservationapplicationjetpack.models.Reservations
import com.example.courtreservationapplicationjetpack.models.courts.CourtDao
import com.example.courtreservationapplicationjetpack.models.courts.CourtRepository
import kotlinx.coroutines.flow.Flow

class OfflineCourtRepository(private val courtDao: CourtDao) :
    CourtRepository {
    override fun getAllCourts(): Flow<List<Court>> = courtDao.getAllCourts()

    override fun getSports(): Flow<List<Court>> = courtDao.getSports()

    override  fun getCourtsSport(sport: String) = courtDao.getCourtsSport(sport)


}

