package com.example.courtreservationapplicationjetpack.views.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.courtreservationapplicationjetpack.models.courts.CourtRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn


/**
 * View Model to retrieve all reservations in the Room database.
 */
class SportPreferencesViewModel(courtRepository: CourtRepository) : ViewModel() {
    /**
     * Holds my sport ui state. The list of all sport are retrieved from [CourtRepository] and mapped to
     * [SportsUiState]
     */
    val allSportsUiState: StateFlow<AllSportsUiState> =
        courtRepository.getSports().map { AllSportsUiState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = AllSportsUiState()
            )

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}

/**
 * Ui State for HomeScreen
 */
data class AllSportsUiState(val sportsList: List<String> = listOf())