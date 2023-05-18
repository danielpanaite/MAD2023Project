
package com.example.courtreservationapplicationjetpack.views.profile

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.courtreservationapplicationjetpack.models.courts.CourtRepository
import com.example.courtreservationapplicationjetpack.models.sport.Sport
import com.example.courtreservationapplicationjetpack.models.sport.SportRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.firstOrNull
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
    val allSportsUiState: StateFlow<AllSportsUiState> =
        courtRepository.getSports().map { AllSportsUiState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = AllSportsUiState()
            )


    val sportPreferencesUiState: StateFlow<SportsPreferencesUiState> =
        sportRepository.getSportUser(1)
            .filterNotNull()
            .map { SportsPreferencesUiState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = SportsPreferencesUiState()
            )

    suspend fun insertOrUpdateSports(sports: List<Sport>) {
        sportRepository.insertOrUpdateSports(sports)

    }

    /*
    suspend fun insertOrUpdateSportsWithLevels(sports: List<Sport>){
        sportRepository.insertOrUpdateSports(sports)
    }*/

    /*
    suspend fun insertOrUpdateSportsWithLevels(selectedSports: Set<String>, sportsWithLevels: Map<String, String>) {
        val currentUser = 1 // replace with the actual user ID
        val selectedSportsWithLevels = sportsWithLevels.filterKeys { selectedSports.contains(it) }
            .map { (sportName, masteryLevel) ->
                Sport(idUser = currentUser, sportName = sportName, masteryLevel = masteryLevel, achievements = null)
            }
        sportRepository.insertOrUpdateSports(selectedSportsWithLevels)
    }

     */



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

    /*
    private suspend fun deleteUncheckedSports(currentUser: Int, selectedSports: Set<String>) {
        // First, delete all sports that are not selected by the user
        val uncheckedSports = sportRepository.getSportUser(currentUser)
            .filter { !selectedSports.contains(it.sportName) }
        sportRepository.deleteSports(uncheckedSports)
    }

     */




    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}

/*

suspend fun updateProfile() {
    if (validateInput(profileUiState.userDetails)) {
        userRepository.update(profileUiState.userDetails.toUser())
    }
}

/**
 * Updates the [profileUiState] with the value provided in the argument. This method also triggers
 * a validation for input values.
 */
fun updateUiState(userDetails: UserDetails) {
    profileUiState =
        ProfileUiState(userDetails = userDetails, isEntryValid = validateInput(userDetails))
}

 */

data class AllSportsUiState(val sportsList: List<String> = listOf())


data class  SportsPreferencesUiState(val sportsList: List<Sport> = listOf())


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

/*

/**
 * Represents Ui State for an Item.
 */
data class ProfileUiState(
    val userDetails: UserDetails = UserDetails(),
    val isEntryValid: Boolean = false
)

data class UserDetails(
    val id: Int? = 0,
    val name: String = "",
    val nickname: String = "",
    val email: String = "",
    val address: String = "",
    val age: String = "",
    val phone: String = "",
    val sportId: String? = "",
    val imageUri: String? = ""
)




/**
 * Extension function to convert [ProfileUiState] to [User]. If the value of [ProfileUiState.name] is
 * not a valid [String], then the name will be set to "User1".
 */
fun UserDetails.toUser(): User = User(
    id = id,
    name = name,
    nickname = nickname,
    email = email,
    address = address,
    age = age.toIntOrNull()?:0,
    phone = phone,
    sportId = sportId?.toIntOrNull() ?:0,
    imageUri = imageUri // se non ci fosse bisognerebbe mettere qualla di default


)



/**
 * Extension function to convert [Reservation] to [ReservationsUiState]
 */
fun User.toProfileUiState(isEntryValid: Boolean = false): ProfileUiState = ProfileUiState(
    userDetails = this.toUserDetails(),
    isEntryValid = isEntryValid
)

/**
 * Extension function to convert [Item] to [ItemDetails]
 */
fun User.toUserDetails(): UserDetails = UserDetails(
    id = id,
    name = name,
    nickname = nickname,
    email = email,
    address = address,
    age = age.toString(),
    phone = phone,
    sportId = sportId.toString(),
    imageUri = imageUri
)
*/

