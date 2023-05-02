package com.example.courtreservationapplicationjetpack.navigation


import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.ui.graphics.vector.ImageVector



// first object HomeScreen and will inherit from the Screens sealed class
// each object represents one screen. route name inside the screen constructor we pass a varibale as string
/**
 * Interface to describe the navigation destinations for the app
 */
sealed class Screens(
    /**
     * Unique name to define the path for a composable
     */
    val route: String,

    val icon: ImageVector,
    val title: String

){
    object MainScreen : Screens(
        route = "main_screen",
        icon = Icons.Default.Home,
        title = "Home"
    )
    object Profile : Screens(
        route = "profile",
        icon = Icons.Default.Person,
        title = "Profile"
    )
    object MyReservations : Screens(
        route = "my_reservations",
        icon = Icons.Default.Star,
        title = "MyReservations"
    )
    object EditReservations : Screens(
        route = "edit_reservation",
        icon = Icons.Default.Star,
        title = "edit Reservations")
    object ReserveACourt : Screens(
        route = "reserve_court",
        icon = Icons.Default.Star,
        title = "reserve a court"
    )
    object EditProfile : Screens(
        route = "edit_profile",
        icon = Icons.Default.Edit,
        title = "edit profile"
    )
}