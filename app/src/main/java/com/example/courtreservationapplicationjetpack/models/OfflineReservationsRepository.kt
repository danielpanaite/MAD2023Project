package com.example.courtreservationapplicationjetpack.models


import kotlinx.coroutines.flow.Flow

class OfflineReservationsRepository(private val reservationsDao: ReservationsDao) : ReservationsRepository {
    override fun getAllReservationsStream(): Flow<List<Reservations>> = reservationsDao.getAllReservations()

    override fun getReservationStream(id: Int): Flow<Reservations?> = reservationsDao.getReservation(id)

    override suspend fun insertReservation(reservations: Reservations) = reservationsDao.insert(reservations)

    override suspend fun deleteReservation(reservations: Reservations) = reservationsDao.delete(reservations)

    override suspend fun updateReservation(reservations: Reservations) = reservationsDao.update(reservations)
}

