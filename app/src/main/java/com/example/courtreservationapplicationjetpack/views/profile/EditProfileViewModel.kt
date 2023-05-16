package com.example.courtreservationapplicationjetpack.views.profile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.courtreservationapplicationjetpack.models.reservations.ReservationRepository
import com.example.courtreservationapplicationjetpack.models.user.UserRepository
import com.example.courtreservationapplicationjetpack.views.reservations.EditReservationDestination
import com.example.courtreservationapplicationjetpack.views.reservations.ReservationDetails
import com.example.courtreservationapplicationjetpack.views.reservations.ReservationsUiState
import com.example.courtreservationapplicationjetpack.views.reservations.toReservation
import com.example.courtreservationapplicationjetpack.views.reservations.toReservationsUiState
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch


/**
 * ViewModel to retrieve and update a reservation from the [UserRepository]'s data source.
 */
class EditProfileViewModel(
    savedStateHandle: SavedStateHandle,
    private val userRepository: UserRepository
) : ViewModel() {

    /**
     * Holds current item ui state
     */
    var profileUiState by mutableStateOf(ProfileUiState())
        private set

    private val profileId: Int = checkNotNull(savedStateHandle[EditProfileDestination.profileIdArg])

    init {
        viewModelScope.launch {
            profileUiState = userRepository.getUserById(profileId)
                .filterNotNull()
                .first()
                .toProfileUiState(true)
        }
    }

    /**
     * Update the reservation in the [UserRepository]'s data source
     */
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

    private fun validateInput(uiState: UserDetails = profileUiState.userDetails): Boolean {
        return with(uiState) {
            name.isNotBlank() && nickname.isNotBlank() && email.isNotBlank()
        }
    }
}

