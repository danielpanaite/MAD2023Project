package com.example.courtreservationapplicationjetpack.models


import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ReservationsDao {

    @Query("SELECT * from reservations ORDER BY id ASC")
    fun getAllReservations(): Flow<List<Reservations>>

    @Query("SELECT * from reservations WHERE id = :id")
    fun getReservation(id: Int): Flow<Reservations>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(reservation: Reservations)

    @Update
    suspend fun update(reservation: Reservations)

    @Delete
    suspend fun delete(reservation: Reservations)
}