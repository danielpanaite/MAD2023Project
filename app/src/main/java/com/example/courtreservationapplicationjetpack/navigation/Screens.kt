package com.example.courtreservationapplicationjetpack.navigation


import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.ui.graphics.vector.ImageVector


/*
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
    val title: String,
    val itemIdArg: String?,
    val routeWithArgs: String?

){
    object MainScreen : Screens(
        route = "main_screen",
        icon = Icons.Default.Home,
        title = "Home",
        itemIdArg = null,
        routeWithArgs = null
    )
    object Profile : Screens(
        route = "profile",
        icon = Icons.Default.Person,
        title = "Profile",
        itemIdArg = null,
        routeWithArgs = null
    )
    object MyReservations : Screens(
        route = "my_reservations",
        icon = Icons.Default.Star,
        title = "MyReservations",
        itemIdArg = null,
        routeWithArgs = null
    )
    object ReservationDetails : Screens(
        route = "reservation_details",
        icon = Icons.Default.Star,
        title = "details Reservations",
        itemIdArg = "itemId",
        routeWithArgs = null
        //routeWithArgs = "$route/{$itemIdArg}"
    )
    object ReserveACourt : Screens(
        route = "reserve_court",
        icon = Icons.Default.Star,
        title = "reserve a court",
                itemIdArg = null,
        routeWithArgs = null
    )
    object EditProfile : Screens(
        route = "edit_profile",
        icon = Icons.Default.Edit,
        title = "edit profile",
        itemIdArg = null,
        routeWithArgs = null
    )
    object EditReservation : Screens(
        route = "edit_reservation",
        icon = Icons.Default.Edit,
        title = "edit reservation",
        itemIdArg = null,
        routeWithArgs = null
    )
}

*/
/**
 * Interface to describe the navigation destinations for the app
 */
interface NavigationDestination {
    /**
     * Unique name to define the path for a composable
     */
    val route: String

    /*
     * String resource id to that contains title to be displayed for the bottom nav .
     */
    val titleRes: String
    val icon: ImageVector
}