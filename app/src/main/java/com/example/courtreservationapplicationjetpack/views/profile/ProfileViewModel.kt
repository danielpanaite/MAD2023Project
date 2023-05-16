package com.example.courtreservationapplicationjetpack.views.profile

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.courtreservationapplicationjetpack.models.reservations.Reservation
import com.example.courtreservationapplicationjetpack.models.reservations.ReservationRepository
import com.example.courtreservationapplicationjetpack.models.user.User
import com.example.courtreservationapplicationjetpack.models.user.UserRepository
import com.example.courtreservationapplicationjetpack.views.reservations.ReservationDetails
import com.example.courtreservationapplicationjetpack.views.reservations.ReservationsUiState
import com.example.courtreservationapplicationjetpack.views.reservations.toReservationDetails
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn


class ProfileViewModel(
    savedStateHandle: SavedStateHandle,
    private val userRepository: UserRepository,
) : ViewModel() {

    private val userId: Int = 1//checkNotNull(savedStateHandle[EditProfileDestination.userIdArg])

    /**
     * Holds the reservations details ui state. The data is retrieved from [ReservationRepository] and mapped to
     * the UI state.
     */
    val uiState: StateFlow<ProfileUiState> =
        userRepository.getUserById(userId)
            .filterNotNull()
            .map {
                ProfileUiState(userDetails = it.toUserDetails())
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = ProfileUiState()
            )



    /**
     * Deletes the reservation from the [ReservationRepository]'s data source.

    suspend fun deleteReservation() {
        reservationRepository.deleteReservation(uiState.value.reservationDetails.toReservation())
    }

    */
    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}



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


