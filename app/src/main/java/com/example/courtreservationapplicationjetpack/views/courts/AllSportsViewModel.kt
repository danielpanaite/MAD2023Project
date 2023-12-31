package com.example.courtreservationapplicationjetpack.views.courts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.courtreservationapplicationjetpack.firestore.CourtViewModel
import com.example.courtreservationapplicationjetpack.models.courts.CourtRepository
import com.example.courtreservationapplicationjetpack.models.reservations.ReservationRepository
import com.google.firebase.Timestamp
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.ZoneId
import java.util.Date
import java.util.Locale
import java.util.TimeZone

//retrieve all sports in the Room database as a StateFlow observable API for UI state
//with the Room Court data changes, the UI updates automatically


/**
 * View Model to retrieve all reservations in the Room database.
 */
class AllSportsViewModel(
courtRepository: CourtRepository,
private val reservationRepository: ReservationRepository
) : ViewModel() {
    /**
     * Holds my sport ui state. The list of all sport are retrieved from [CourtRepository] and mapped to
     * [SportsUiState]
     */
    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    init {
        loadStuff()
    }

    fun loadStuff(pickedSport: String? = null, courtViewModel: CourtViewModel? = null){
        viewModelScope.launch {
            _isLoading.value = true
            delay(3000L)
            // do stuff
            println(pickedSport)
//            if (courtViewModel != null) {
//                if (pickedSport != null) {
//                    courtViewModel.getCourtsBySport("tennis")
//                }
//            }
            _isLoading.value = false
        }
    }

    val allSportsUiState: StateFlow<AllSportsUiState> =
        courtRepository.getSports().map { AllSportsUiState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = AllSportsUiState()
            )

    fun getSlot(date:String, courtId: Int): Flow<List<String>> {
        return reservationRepository.getSlot(date, courtId)
    }


    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}

/**
 * Ui State for HomeScreen
 */
data class AllSportsUiState(val sportsList: List<String> = listOf())
