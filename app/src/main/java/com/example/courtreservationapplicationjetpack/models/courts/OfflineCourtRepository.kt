package com.example.courtreservationapplicationjetpack.models.courts


import kotlinx.coroutines.flow.Flow

class OfflineCourtRepository(private val courtDao: CourtDao) :
    CourtRepository {
    override fun getAllCourts(): Flow<List<Court>> = courtDao.getAllCourts()

    override fun getSports(): Flow<List<String>> = courtDao.getSports()

    override  fun getCourtsSport(sport: String) = courtDao.getCourtsSport(sport)

    override fun getCourt(id: Int): Flow<Court> = courtDao.getCourt(id)
}

