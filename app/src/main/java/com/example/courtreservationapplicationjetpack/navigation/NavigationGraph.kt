package com.example.courtreservationapplicationjetpack.navigation


import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.courtreservationapplicationjetpack.home.MainScreen
import com.example.courtreservationapplicationjetpack.home.MainScreenDestination
import com.example.courtreservationapplicationjetpack.routes.EditProfile
import com.example.courtreservationapplicationjetpack.reservations.ReservationDetails
import com.example.courtreservationapplicationjetpack.reservations.EditReservation
import com.example.courtreservationapplicationjetpack.reservations.EditReservationDestination
import com.example.courtreservationapplicationjetpack.routes.Profile
import com.example.courtreservationapplicationjetpack.reservations.MyReservations
import com.example.courtreservationapplicationjetpack.reservations.MyReservationsDestination
import com.example.courtreservationapplicationjetpack.reservations.ReservationDetailsDestination
import com.example.courtreservationapplicationjetpack.reservations.ReserveACourt
import com.example.courtreservationapplicationjetpack.reservations.ReserveACourtDestination
import com.example.courtreservationapplicationjetpack.routes.EditProfileDestination
import com.example.courtreservationapplicationjetpack.routes.ProfileDestination

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
){
    NavHost(
        navController = navController,
        //startDestination = Screens.MainScreen.route,
        startDestination = MainScreenDestination.route,
        modifier = modifier
    ){
        composable(
            //route = Screens.MainScreen.route
            route = MainScreenDestination.route

        ){
            MainScreen(
                navController = navController,
                //navigateToReserveACourt = { navController.navigate(route = Screens.ReserveACourt.route) }
                navigateToReserveACourt = { navController.navigate(ReserveACourtDestination.route) }

            )
        }

        composable(
            //route = Screens.ReserveACourt.route
            route = ReserveACourtDestination.route

        ){
            ReserveACourt(
                navigateBack = { navController.popBackStack() },
                onNavigateUp = { navController.navigateUp() },
                navController = navController
                //navigateToMyReservations = { navController.navigate(Screens.MyReservations.route) }
            )
        }

        composable(
            route = EditProfileDestination.route
        ){
            EditProfile()
        }
        composable(
            route = ProfileDestination.route
        ){
            Profile(navController = navController, navigateToEditProfileDestination = { navController.navigate(EditProfileDestination.route) })
        }
        composable(
            //route = Screens.MyReservations.route
            route = MyReservationsDestination.route
        ){
            MyReservations(
                navController = navController,
                navigateToReservationDetailsDestination = { navController.navigate("${ReservationDetailsDestination.route}/${it}") }

            )
        }
        composable(
            //route = Screens.ReservationDetails.route
            route = ReservationDetailsDestination.routeWithArgs,
            arguments = listOf(navArgument(ReservationDetailsDestination.reservationIdArg) {
                type = NavType.IntType
            })
        ){
            ReservationDetails(
                navController = navController,
                //navigateToEditReservation = { navController.navigate(route = ("${Screens.EditReservation.route}/$it")) }
                //navigateToEditReservation = { navController.navigate(route = ("${Screens.EditReservation.route}/$it")) }
                navigateToEditReservation = { navController.navigate("${EditReservationDestination.route}/$it") },
                navigateBack = { navController.navigateUp() }
            )
        }
        composable(
            //route = Screens.EditReservation.route
            route = EditReservationDestination.routeWithArgs,
            arguments = listOf(navArgument(EditReservationDestination.reservationIdArg) {
                type = NavType.IntType
            })


        ){
            EditReservation(
                navController = navController,
                navigateBack = { navController.popBackStack() },
                onNavigateUp = { navController.navigateUp() }
            )
        }
    }

}