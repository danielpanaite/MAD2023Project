package com.example.courtreservationapplicationjetpack.views.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.courtreservationapplicationjetpack.models.achievements.Achievements
import com.example.courtreservationapplicationjetpack.models.achievements.AchievementsRepository
import com.example.courtreservationapplicationjetpack.models.reservations.Reservation
import com.example.courtreservationapplicationjetpack.models.sport.SportRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn


/**
 * View Model to retrieve all reservations in the Room database.
 */
class AchievementsViewModel(
    private val sportRepository: SportRepository,
    private val achievementRepository: AchievementsRepository
) : ViewModel() {


    val achievements: StateFlow<Achievements2> =
        achievementRepository.getAchivementUser(1)
            .filterNotNull()
            .map { Achievements2(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = Achievements2()
            )

    /*
    val achievementsUiState: StateFlow<AchievementsUiState> =

    achievementRepository.getAchivementUser(1).map {
        AchievementsUiState(achievementDetails = it.toAchievementsDetails()) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = AchievementsUiState()
            )
*/
    suspend fun addAchievement(sportName: String, idUser: Int, date: String, title: String, description:String){

        val newAchievement = AchievementDetails(
            id = null,
            sportName = sportName,
            idUser = 1,
            date = date,
            title = title,
            description = description
        )

        achievementRepository.addAchievement(newAchievement.toAchievements())

    }

    suspend fun deleteAchievement(id: Int?){


        if (id != null) {
            achievementRepository.deleteAchievementId(id)
        }

    }


    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}


data class  Achievements2(val achievementsList: List<Achievements> = listOf())




/**
 * Extension function to convert [Reservation] to [ReservationsUiState]
 */

/*

data class AchievementsUiState(
    val achievementDetails: AchievementDetails = AchievementDetails(),
    val isEntryValid: Boolean = false
)

 */

data class AchievementDetails(
    val id: Int?,
    val sportName: String,
    val idUser: Int,
    val date: String ,
    val title: String ,
    val description: String?
)

fun AchievementDetails.toAchievements(): Achievements = Achievements(
    id = id,
    sportName = sportName,
    idUser = idUser,
    date = date,
    title = title,
    description=description
)

/*
fun Achievements.toAchievementsUiState(isEntryValid: Boolean = false): AchievementsUiState = AchievementsUiState(
    achievementDetails = this.toAchievementsDetails(),
    isEntryValid = isEntryValid
)


fun Achievements.toAchievementsDetails(): AchievementDetails = AchievementDetails(
    id = id,
    sportName = sportName,
    idUser = idUser,
    date = date,
    title = title,
    description=description
)

*/



