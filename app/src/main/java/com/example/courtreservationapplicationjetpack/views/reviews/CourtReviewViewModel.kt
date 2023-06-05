package com.example.courtreservationapplicationjetpack.views.reviews

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.courtreservationapplicationjetpack.models.reviews.Review
import com.example.courtreservationapplicationjetpack.models.reviews.ReviewRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class CourtReviewViewModel(
    savedStateHandle: SavedStateHandle,
    reviewRepository: ReviewRepository
): ViewModel() {

    //private val courtId: Int = checkNotNull(savedStateHandle[ReviewCreatePageDestination.courtIdArg])
    private val courtId: Int = 1
    val courtReviewsState: StateFlow<CourtReviewsState> =
        reviewRepository.getAllCourtReviews(courtId)
            .filterNotNull()
            .map { CourtReviewsState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = CourtReviewsState()
            )

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }

}

data class CourtReviewsState(val reviewList: List<Review> = listOf())