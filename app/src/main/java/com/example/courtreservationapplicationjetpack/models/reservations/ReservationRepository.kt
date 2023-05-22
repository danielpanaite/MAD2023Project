package com.example.courtreservationapplicationjetpack.models.reservations



import kotlinx.coroutines.flow.Flow

/**
 * Repository that provides insert, update, delete, and retrieve of [Reservation] from a given data source.
 */
interface ReservationRepository {
    /**
     * Retrieve all the reservations from the the given data source.
     */
    fun getAllReservationsStream(): Flow<List<Reservation>>

    /**
     * Retrieve an reservation from the given data source that matches with the [id].
     */
    fun getReservationStream(id: Int): Flow<Reservation?>

    fun getCourtReservations(court: Int, date: String): Flow<List<Reservation>>

    /**
     * Insert reservation in the data source
     */
    suspend fun insertReservation(reservation: Reservation)

    /**
     * Delete reservation from the data source
     */
    suspend fun deleteReservation(reservation: Reservation)

    /**
     * Update reservation in the data source
     */
    suspend fun updateReservation(reservation: Reservation)
}
