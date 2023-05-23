package com.example.courtreservationapplicationjetpack.views.courts

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.courtreservationapplicationjetpack.models.courts.Court
import com.example.courtreservationapplicationjetpack.models.courts.CourtRepository
import com.example.courtreservationapplicationjetpack.models.reservations.ReservationRepository
import com.example.courtreservationapplicationjetpack.views.reservations.ReservationDetails
import com.example.courtreservationapplicationjetpack.views.reservations.toReservation
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

/**
 * View Model to retrieve all courts of a specific sport from the Room database.
 */
class CourtsAvailableViewModel(
    savedStateHandle: SavedStateHandle,
    private val courtRepository: CourtRepository,
    private val reservationRepository: ReservationRepository
) : ViewModel() {

    private val sport: MutableStateFlow<String?> = MutableStateFlow(null)

    val courtsAvailableUiState: StateFlow<CourtsAvailableUiState> = sport
        .filterNotNull()
        .flatMapLatest { selectedSport ->
            courtRepository.getCourtsSport(selectedSport)
        }
        .map { CourtsAvailableUiState(it) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
            initialValue = CourtsAvailableUiState()
        )

    fun setSport(sport: String) {
        this.sport.value = sport
    }

    fun getCourt(id: Int) = courtRepository.getCourt(id)

    suspend fun addReservation(id: Int?, user: String, courtId: String, date: String, slot: String, additions: String, people: String){
        val newReservation = ReservationDetails(id = id, user = user, courtId = courtId, date = date, slot = slot, additions = additions, people = people)
        reservationRepository.addReservation(newReservation.toReservation())
    }

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}

/**
 * Ui State for CourtAvailabilityScreen
 */
data class CourtsAvailableUiState(val courtsAvailableList: List<Court> = emptyList())
