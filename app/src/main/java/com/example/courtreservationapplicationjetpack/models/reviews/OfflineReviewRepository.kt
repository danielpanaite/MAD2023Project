package com.example.courtreservationapplicationjetpack.models.reviews

import kotlinx.coroutines.flow.Flow

class OfflineReviewRepository(private val reviewDao: ReviewDao): ReviewRepository {

    override fun getAllUserReviews(user: Int): Flow<List<Review>> = reviewDao.getAllUserReviews(user)

    override fun getReviewByUserAndCourt(user: Int, court: Int): Flow<Review> = reviewDao.getReviewByUserAndCourt(user, court)

    override fun getAllCourtReviews(court: Int): Flow<List<Review>> = reviewDao.getAllCourtReviews(court)

    override suspend fun insert(review: Review) = reviewDao.insert(review)

    override suspend fun update(review: Review) = reviewDao.update(review)

    override suspend fun delete(review: Review) = reviewDao.delete(review)

}