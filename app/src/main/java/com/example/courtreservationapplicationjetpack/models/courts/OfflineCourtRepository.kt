package com.example.courtreservationapplicationjetpack.models.courts


import kotlinx.coroutines.flow.Flow

class OfflineCourtRepository(private val courtDao: CourtDao) :
    CourtRepository {
    override fun getAllCourts(): Flow<List<Court>> = courtDao.getAllCourts()

    override fun getCourtsWithId(courts: List<Int>): Flow<List<Court>> = courtDao.getCourtsWithId(courts)

    override fun getSports(): Flow<List<String>> = courtDao.getSports()

    override  fun getCourtsSport(sport: String) = courtDao.getCourtsSport(sport)


}

