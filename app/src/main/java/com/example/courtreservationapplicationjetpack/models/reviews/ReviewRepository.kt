package com.example.courtreservationapplicationjetpack.models.reviews

import kotlinx.coroutines.flow.Flow

interface ReviewRepository {

    fun getAllUserReviews(user: Int): Flow<List<Review>>

    fun getReviewByUserAndCourt(user: Int, court: Int): Flow<Review>

    suspend fun insert(review: Review)

    suspend fun update(review: Review)

    suspend fun delete(review: Review)

}