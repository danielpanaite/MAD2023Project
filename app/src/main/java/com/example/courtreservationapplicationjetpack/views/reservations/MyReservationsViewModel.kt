package com.example.courtreservationapplicationjetpack.views.reservations


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.courtreservationapplicationjetpack.models.courts.Court
import com.example.courtreservationapplicationjetpack.models.courts.CourtRepository
import com.example.courtreservationapplicationjetpack.models.reservations.Reservation
import com.example.courtreservationapplicationjetpack.models.reservations.ReservationRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.time.LocalDate
import java.time.format.DateTimeFormatter

/**
 * View Model to retrieve all reservations in the Room database.
 */
class MyReservationsViewModel(reservationRepository: ReservationRepository, courtRepository: CourtRepository) : ViewModel() {

    private val courts: MutableStateFlow<List<Int>?> = MutableStateFlow(null)
    private val court: MutableStateFlow<Int?> = MutableStateFlow(null)
    private val date: MutableStateFlow<String?> = MutableStateFlow(null)

    val dateFormatter: DateTimeFormatter =
        DateTimeFormatter.ofPattern("dd/MM/yyyy")
    /**
     * Holds my reservations ui state. The list of reservations are retrieved from [ReservationRepository] and mapped to
     * [ReservationsUiState]
     */
    val myReservationsUiState: StateFlow<MyReservationsUiState> =
        reservationRepository.getAllReservationsStream().map { MyReservationsUiState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = MyReservationsUiState()
            )

    val reservationCourtsState: StateFlow<ReservationCourtsState> =
        courts.filterNotNull().flatMapLatest { c -> courtRepository.getCourtsWithId(c) }.map { ReservationCourtsState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = ReservationCourtsState()
            )

    val courtReservationsState: StateFlow<CourtReservationsState> =
        court.filterNotNull().flatMapLatest { c -> reservationRepository.getCourtReservations(c, date.value.toString()) }.map { CourtReservationsState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = CourtReservationsState()
            )


    fun setCourts(courts: List<Int>) {
        this.courts.value = courts
    }

    fun setCourt(court: Int) {
        this.court.value = court
    }

    fun setDate(date: String){
        this.date.value = date
    }

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}

/**
 * Ui State for HomeScreen
 */
data class MyReservationsUiState(val reservationList: List<Reservation> = listOf())

data class ReservationCourtsState(val courtList: List<Court> = listOf())

data class CourtReservationsState(val reservationList: List<Reservation> = listOf())
