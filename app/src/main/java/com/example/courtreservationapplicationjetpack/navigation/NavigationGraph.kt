package com.example.courtreservationapplicationjetpack.navigation


import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.courtreservationapplicationjetpack.home.MainScreen
import com.example.courtreservationapplicationjetpack.routes.EditProfile
import com.example.courtreservationapplicationjetpack.reservations.EditReservation
import com.example.courtreservationapplicationjetpack.routes.Profile
import com.example.courtreservationapplicationjetpack.reservations.MyReservations
import com.example.courtreservationapplicationjetpack.reservations.ReserveACourt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
){
    NavHost(
        navController = navController,
        startDestination = Screens.MainScreen.route,
        modifier = modifier
    ){
        composable(
            route = Screens.MainScreen.route
        ){
            MainScreen(
                navController = navController,
                navigateToReserveACourt = { navController.navigate(route = Screens.ReserveACourt.route) }
            )
        }

        composable(
            route = Screens.ReserveACourt.route
        ){
            ReserveACourt(
                navController = navController
                //navigateToMyReservations = { navController.navigate(Screens.MyReservations.route) }
            )
        }

        composable(
            route = Screens.EditProfile.route
        ){
            EditProfile()
        }
        composable(
            route = Screens.Profile.route
        ){
            Profile(navController = navController)
        }
        composable(
            route = Screens.MyReservations.route
        ){
            MyReservations(
                navController = navController,
                navigateToEditReservation = { navController.navigate(route = Screens.EditReservations.route) }
            )
        }
        composable(
            route = Screens.EditReservations.route
        ){
            EditReservation()
        }
    }

}