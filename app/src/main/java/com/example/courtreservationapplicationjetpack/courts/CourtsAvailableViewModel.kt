package com.example.courtreservationapplicationjetpack.courts


/**
 * View Model to retrieve all courts in the Room database.
 */

/*
class MyCourtsViewModel(courtRepository: CourtRepository) : ViewModel() {
    /**
     * Holds my courts ui state. The list of courts are retrieved from [CourtsRepository] and mapped to
     * [CourtsUiState]
     */
    val myCourtsUiState: StateFlow<MyCourtsUiState> =
        courtRepository.getAllCourts().map { MyCoourtsUiState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = MyCourtsUiState()
            )

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}

/**
 * Ui State for HomeScreen
 */
data class MyCourtsUiState(val courtList: List<Court> = listOf())
*/