package com.example.courtreservationapplicationjetpack.views.reviews

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.courtreservationapplicationjetpack.models.courts.Court
import com.example.courtreservationapplicationjetpack.models.courts.CourtRepository
import com.example.courtreservationapplicationjetpack.models.reviews.Review
import com.example.courtreservationapplicationjetpack.models.reviews.ReviewRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEmpty
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ReviewCreateViewModel(
    savedStateHandle: SavedStateHandle,
    private val reviewRepository: ReviewRepository,
    courtRepository: CourtRepository
) : ViewModel() {

    //private val courtId: Int = checkNotNull(savedStateHandle[ReviewCreatePageDestination.courtIdArg])
    private val courtId: Int = 1
    data class ReviewUiState(
        val review: Review = Review(null, 0, 0, "", "", 0),
        val isEntryValid: Boolean = false
    )

    var reviewsUiState by mutableStateOf(ReviewUiState())
        private set

    init {
        viewModelScope.launch {
            reviewsUiState = reviewRepository.getReviewByUserAndCourt(1, courtId)
                .filterNotNull()
                .onEmpty { ReviewUiState().review }
                .first()
                .toReviewsUiState()
        }
    }

    val courtUiState: StateFlow<ReviewCourtState> =
        courtRepository.getCourtsWithId(listOf(courtId))
            .filterNotNull()
            .map { ReviewCourtState(it[0]) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = ReviewCourtState()
            )

    fun updateUiState(review: Review) {
        reviewsUiState = ReviewUiState(review = review, isEntryValid = validateInput(review))
    }

    suspend fun createReview() {
        if(validateInput(reviewsUiState.review)) {
            reviewRepository.insert(reviewsUiState.review)
        }
    }

    suspend fun deleteReview(){
        reviewRepository.delete(reviewsUiState.review)
    }

    private fun validateInput(uiState: Review = reviewsUiState.review): Boolean {
        return with(uiState) {
            (user != 0 && court != 0 && date.isNotBlank() && review.isNotBlank() && rating != 0)
        }
    }

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }

    private fun Review.toReviewsUiState(isEntryValid: Boolean = false): ReviewUiState = ReviewUiState(
        review = this,
        isEntryValid = isEntryValid
    )

}

data class ReviewCourtState(val court: Court? = null)