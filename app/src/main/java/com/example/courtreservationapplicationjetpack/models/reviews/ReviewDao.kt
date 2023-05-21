package com.example.courtreservationapplicationjetpack.models.reviews

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ReviewDao {

    @Query("SELECT * from review WHERE user = :user")
    fun getAllUserReviews(user: Int): Flow<List<Review>>

    @Query("SELECT * from review WHERE user = :user AND court = :court")
    fun getReviewByUserAndCourt(user: Int, court: Int): Flow<Review>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(review: Review)

    @Update
    suspend fun update(review: Review)

    @Delete
    suspend fun delete(review: Review)
}