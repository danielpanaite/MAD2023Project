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
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch


/**
 * View Model to retrieve all reservations in the Room database.
 */
class AchievementsViewModel(
    private val sportRepository: SportRepository
) : ViewModel() {

    val favoritesSportsUi: StateFlow<FavoritesSportsUi> =
        sportRepository.getSportUser(1)
            .filterNotNull()
            .map { FavoritesSportsUi(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = FavoritesSportsUi()
            )

    val achievementsUi: StateFlow<AchievementsUi> =
        sportRepository.getAchievements(1)
            .filterNotNull()
            .map { AchievementsUi(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = AchievementsUi()

            )

    suspend fun updateSportAchievements(sportName: String, idUser: Int, achievements: String) {
        sportRepository.updateSportAchievements(sportName, idUser, achievements)

    }



    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}

data class  FavoritesSportsUi(val sportsList: List<Sport> = listOf())

data class  AchievementsUi(val achievementsList: List<String> = listOf())