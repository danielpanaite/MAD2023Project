package com.example.courtreservationapplicationjetpack.models.reservations


import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

/**
 * room DAOs provide methods used by the app to retriece, update insert, and delete data in the database
 * DAO pattern used to separate the persistence layer from the rest of the application by providing an abstract interface.
 * This isolation follows the single-responsability principle (" A module should be responsible to one, and only one
 * actor")
 * Functionality of DAO is to hide all the complexities involved in performing database operations
 * in the underlying persistence layer, separate from the rest of the application.
 * This lets as change the data layer independently of the code that uses the data.
 * DAO is a custom interface that provides methods for wuerying/retriving... the database.
 * Room generates an implementation of this class at compile time.
 */
@Dao
interface ReservationDao {

    @Query("SELECT * from reservation ORDER BY id ASC")
    fun getAllReservations(): Flow<List<Reservation>>

    /**
     * query select all columns from the reservations, where the id matches the :id argument.
     * Reccommended to use Flow in the persistence layer. With Flow as the return type, we receive
     * notification whenever the data in the database changes. The Room keps this flow updated,
     * which means we only need to explicity get the data once.
     * Because of the Flow return type, Room also runs the wury on the background thread
     */
    @Query("SELECT * from reservation WHERE id = :id")
    fun getReservation(id: Int): Flow<Reservation>

    @Query("SELECT * from reservation WHERE courtId = :court AND date = :date")
    fun getCourtReservations(court: Int, date: String): Flow<List<Reservation>>

    /**
     * mark the function with the suspend keyword to let it run on a separate thread.
     * Room doesn't allow database access on the main thread
     * when inserting items in the database, conflicts can happen. For now we only insert the entity
     * from one place, the reserve a courtscreen so there shouldn't be any conflicts, and so set
     * the conflict strategy to ignore
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(reservation: Reservation)

    @Update
    suspend fun update(reservation: Reservation)

    @Delete
    suspend fun delete(reservation: Reservation)

    @Query("SELECT slot FROM reservation WHERE date =:date AND courtId = :courtId")
    fun getSlot(date: String, courtId: Int): Flow<List<String>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addReservation(reservation: Reservation)


}