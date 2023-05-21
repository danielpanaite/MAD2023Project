package com.example.courtreservationapplicationjetpack.models.courts

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CourtDao {

   /* @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addCourt(court: Court)*/

    @Query("SELECT * FROM court ORDER BY id DESC")
    fun getAllCourts(): Flow<List<Court>>

    /*@Query("SELECT name FROM court")
    fun getCourtName(): String*/

    //prende tutti gli sport disponibili
    @Query("SELECT DISTINCT sport from court")
    fun getSports(): Flow<List<String>>

    //prendi tutti i campi di quello sport
    @Query("SELECT * from court WHERE sport = :sport")
    fun getCourtsSport(sport: String): Flow<List<Court>>

    @Query("SELECT * from court WHERE id = :id")
    fun getCourt(id: Int): Flow<Court>
    /*
    @Query("DELETE FROM court_table")
    suspend fun  deleteAll()
    */
}