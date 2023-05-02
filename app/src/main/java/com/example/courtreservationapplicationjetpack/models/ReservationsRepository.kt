package com.example.courtreservationapplicationjetpack.models



import kotlinx.coroutines.flow.Flow

/**
 * Repository that provides insert, update, delete, and retrieve of [Reservations] from a given data source.
 */
interface ReservationsRepository {
    /**
     * Retrieve all the reservations from the the given data source.
     */
    fun getAllReservationsStream(): Flow<List<Reservations>>

    /**
     * Retrieve an reservation from the given data source that matches with the [id].
     */
    fun getReservationStream(id: Int): Flow<Reservations?>

    /**
     * Insert reservation in the data source
     */
    suspend fun insertReservation(reservations: Reservations)

    /**
     * Delete reservation from the data source
     */
    suspend fun deleteReservation(reservations: Reservations)

    /**
     * Update reservation in the data source
     */
    suspend fun updateReservation(reservations: Reservations)
}
