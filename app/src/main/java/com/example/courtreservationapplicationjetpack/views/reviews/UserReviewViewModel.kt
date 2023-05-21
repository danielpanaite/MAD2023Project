package com.example.courtreservationapplicationjetpack.views.reviews

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.courtreservationapplicationjetpack.models.reviews.Review
import com.example.courtreservationapplicationjetpack.models.reviews.ReviewRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class ReviewViewModel(
    reviewRepository: ReviewRepository
) : ViewModel() {

    val myReviewsUiState: StateFlow<MyReviewsUiState> =
        reviewRepository.getAllUserReviews(1)
            .map { MyReviewsUiState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = MyReviewsUiState()
            )

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }

}

data class MyReviewsUiState(val reviewList: List<Review> = listOf())