package com.example.courtreservationapplicationjetpack.models.reservations


import kotlinx.coroutines.flow.Flow

class OfflineReservationRepository(private val reservationDao: ReservationDao) :
    ReservationRepository {
    override fun getAllReservationsStream(): Flow<List<Reservation>> = reservationDao.getAllReservations()

    override fun getReservationStream(id: Int): Flow<Reservation?> = reservationDao.getReservation(id)

    override suspend fun insertReservation(reservation: Reservation) = reservationDao.insert(reservation)

    override suspend fun deleteReservation(reservation: Reservation) = reservationDao.delete(reservation)

    override suspend fun updateReservation(reservation: Reservation) = reservationDao.update(reservation)
    override fun getSlot(date: String, courtId: Int): Flow<List<String>> = reservationDao.getSlot(date, courtId)
}

