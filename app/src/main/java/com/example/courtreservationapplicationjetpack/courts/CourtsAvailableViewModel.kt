package com.example.courtreservationapplicationjetpack.courts

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.courtreservationapplicationjetpack.models.courts.Court
import com.example.courtreservationapplicationjetpack.models.courts.CourtRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn


/**
 * View Model to retrieve all courts of that sport in the Room database.
 */

/**
* View Model to retrieve all reservations in the Room database.
*/
class CourtsAvailableViewModel(savedStateHandle: SavedStateHandle,
                               private val courtRepository: CourtRepository,) : ViewModel() {

    /**
     * Holds my court ui state. The list of all court are retrieved from [CourtRepository] and mapped to
     * [courtsAvailableUiState]
     */
    //riprendere la stringa da passare a getCourtsSport
    private val sport: String = checkNotNull(savedStateHandle[CourtsAvailableDestination.sportArg])

    val courtsAvailableUiState: StateFlow<CourtsAvailableUiState> =
        courtRepository.getCourtsSport(sport).map { CourtsAvailableUiState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = CourtsAvailableUiState()
            )

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }


}

/**
 * Ui State for HomeScreen
 */
data class CourtsAvailableUiState(val courtsAvailableList: List<Court> = listOf())
