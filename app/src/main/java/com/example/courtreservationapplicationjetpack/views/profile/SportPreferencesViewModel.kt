
package com.example.courtreservationapplicationjetpack.views.profile


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.courtreservationapplicationjetpack.models.courts.CourtRepository
import com.example.courtreservationapplicationjetpack.models.sport.Sport
import com.example.courtreservationapplicationjetpack.models.sport.SportRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * View Model to retrieve all reservations in the Room database.
 */
class SportPreferencesViewModel(
    courtRepository: CourtRepository,
    private val sportRepository: SportRepository
) : ViewModel() {
    /**
     * Holds my sport ui state. The list of all sport are retrieved from [CourtRepository] and mapped to
     * [SportsUiState]
     */

    val sportPreferencesUiState: StateFlow<SportsPreferencesUiState> by lazy {
        flow {
            // Emit the loading state initially
            emit(SportsPreferencesUiState(isLoading = true))

            val sports = sportRepository.getSportUser(1)
                .filterNotNull()
                .firstOrNull() ?: emptyList() // Get the first list of sports or an empty list if null

            // Emit the data with isLoading set to false
            emit(SportsPreferencesUiState(sports, isLoading = false))
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
            initialValue = SportsPreferencesUiState(isLoading = true) // Set initial isLoading state to true
        )
    }











    val allSportsUiState: StateFlow<AllSportsUiState> =
        courtRepository.getSports().map { AllSportsUiState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = AllSportsUiState()
            )



    suspend fun insertOrUpdateSports(sports: List<Sport>) {
        sportRepository.insertOrUpdateSports(sports)

    }

    fun updateSportMastery(sportName: String, masteryLevel: String?) {
        val currentUser = 1 // replace with the actual user ID
        viewModelScope.launch {
            if (masteryLevel == null) { // Remove the sport from the database
                sportRepository.deleteSportByName(sportName, currentUser)
            } else {
                val existingSport = sportRepository.getSportByName(sportName, currentUser).firstOrNull()
                if (existingSport == null) {
                    // If the sport doesn't exist, insert it
                    sportRepository.insertOrUpdateSports(listOf(Sport(idUser = currentUser, sportName = sportName, masteryLevel = masteryLevel, achievements = null)))
                } else {
                    // If the sport exists, update its mastery level
                    val updatedSport = existingSport.copy(masteryLevel = masteryLevel)
                    sportRepository.updateSport(listOf(updatedSport))
                }
            }
        }
    }

    fun deleteSports(currentUser: Int, uncheckedSports: List<String>){
        viewModelScope.launch {
            uncheckedSports.forEach { sport ->
                sportRepository.deleteSportByName(sport, 1)

            }
        }
    }

   

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}

data class AllSportsUiState(val sportsList: List<String> = listOf())


data class SportsPreferencesUiState(
    val sportsList: List<Sport> = listOf(),
    val isLoading: Boolean = false // Add the isLoading property
)

data class SportPreferencesDetails(
    val idUser: Int = 1,
    val sportName: String ="",
    val masteryLevel :String? = "",
    val achievements: String? = ""


)

fun SportPreferencesDetails.toSport(): Sport = Sport(

    idUser = idUser,
    sportName = sportName,
    masteryLevel = masteryLevel,
    achievements = achievements,
)

fun Sport.toSportPreferencesUiState(): SportsPreferencesUiState = SportsPreferencesUiState(
    //sportPreferencesDetails = this.toSportPreferencesDetails(),
)

/**
 * Extension function to convert [Item] to [ItemDetails]
 */
fun Sport.toSportPreferencesDetails(): SportPreferencesDetails = SportPreferencesDetails(
    idUser = idUser,
    sportName = sportName,
    masteryLevel = masteryLevel,
    achievements = achievements,
)
