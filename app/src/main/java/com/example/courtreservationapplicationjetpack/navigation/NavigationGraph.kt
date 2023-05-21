package com.example.courtreservationapplicationjetpack.navigation


import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.courtreservationapplicationjetpack.views.courts.AllSports
import com.example.courtreservationapplicationjetpack.views.courts.AllSportsDestination
import com.example.courtreservationapplicationjetpack.views.courts.CourtReservation
import com.example.courtreservationapplicationjetpack.views.courts.CourtsAvailable
import com.example.courtreservationapplicationjetpack.views.courts.CourtsAvailableDestination
import com.example.courtreservationapplicationjetpack.views.MainScreen
import com.example.courtreservationapplicationjetpack.views.MainScreenDestination
import com.example.courtreservationapplicationjetpack.views.reservations.ReservationDetails
import com.example.courtreservationapplicationjetpack.views.reservations.EditReservation
import com.example.courtreservationapplicationjetpack.views.reservations.EditReservationDestination
import com.example.courtreservationapplicationjetpack.views.profile.Profile
import com.example.courtreservationapplicationjetpack.views.profile.EditProfile
import com.example.courtreservationapplicationjetpack.views.profile.EditProfileDestination
import com.example.courtreservationapplicationjetpack.views.profile.ProfileDestination
import com.example.courtreservationapplicationjetpack.views.profile.SportPreferences
import com.example.courtreservationapplicationjetpack.views.profile.SportPreferencesDestination
import com.example.courtreservationapplicationjetpack.views.reservations.MyReservations
import com.example.courtreservationapplicationjetpack.views.reservations.MyReservationsDestination
import com.example.courtreservationapplicationjetpack.views.reservations.ReservationDetailsDestination
import com.example.courtreservationapplicationjetpack.views.reviews.CourtReviewPage
import com.example.courtreservationapplicationjetpack.views.reviews.CourtReviewPageDestination
import com.example.courtreservationapplicationjetpack.views.reviews.ReviewCreatePage
import com.example.courtreservationapplicationjetpack.views.reviews.ReviewCreatePageDestination
import com.example.courtreservationapplicationjetpack.views.reviews.ReviewMainPage
import com.example.courtreservationapplicationjetpack.views.reviews.ReviewMainPageDestination


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
){
    NavHost(
        navController = navController,
        startDestination = MainScreenDestination.route,
        modifier = modifier
    ){
        composable(
            route = MainScreenDestination.route

        ){
            MainScreen(
                navController = navController,
                //navigateToReserveACourt = { navController.navigate(ReserveACourtDestination.route) }
                navigateToAllSports = {navController.navigate(AllSportsDestination.route)},
                navigateToReviews = {navController.navigate(ReviewMainPageDestination.route)}
            )
        }

        /*
        composable(
            route = ReserveACourtDestination.route

        ){
            ReserveACourt(
                navigateBack = { navController.popBackStack() },
                onNavigateUp = { navController.navigateUp() },
                navController = navController
            )
        }

         */

        composable(
            route = EditProfileDestination.routeWithArgs,
            arguments = listOf(navArgument(EditProfileDestination.profileIdArg) {
                type = NavType.IntType
            })
        ){
            EditProfile(
                navController = navController,
                navigateBack = { navController.popBackStack() },
                onNavigateUp = { navController.navigateUp() }
            )
        }
        composable(
            route = ProfileDestination.route
        ){
            Profile(navController = navController,
                navigateToEditProfileDestination = { navController.navigate("${EditProfileDestination.route}/${it}") },
                navigateBack = { navController.navigateUp() },
                navigateToSportPreferencesDestination = {navController.navigate(SportPreferencesDestination.route)},
            )

        }


        composable(
            route = MyReservationsDestination.route
        ){
            MyReservations(
                navController = navController,
                navigateToEditReservation = { navController.navigate("${EditReservationDestination.route}/$it") },
            )
        }
        composable(
            route = ReservationDetailsDestination.routeWithArgs,
            arguments = listOf(navArgument(ReservationDetailsDestination.reservationIdArg) {
                type = NavType.IntType
            })
        ){
            ReservationDetails(
                navController = navController,
                navigateToEditReservation = { navController.navigate("${EditReservationDestination.route}/$it") },
                navigateBack = { navController.navigateUp() }
            )
        }
        composable(
            route = EditReservationDestination.routeWithArgs,
            arguments = listOf(navArgument(EditReservationDestination.reservationIdArg) {
                type = NavType.IntType
            })
        ){
            EditReservation(
                navController = navController,
                onNavigateUp = { navController.navigateUp() }
            )
        }
        //All courts list
        composable(
            route = AllSportsDestination.route

        ){
            AllSports(
                navController = navController,
                navigateToCourtsAvailable = { navController.navigate("${CourtsAvailableDestination.route}/${it}" ) },
                onNavigateUp = { navController.navigateUp() }
            )
        }
        //Review main page
        composable(
            route = ReviewMainPageDestination.route
        ){
            ReviewMainPage(
                navController = navController,
                onNavigateUp = { navController.navigateUp() },
                navigateToCreateReview = { navController.navigate("${ReviewCreatePageDestination.route}/$it") }
            )
        }

        composable(
            route = ReviewCreatePageDestination.routeWithArgs,
            arguments = listOf(navArgument(ReviewCreatePageDestination.courtIdArg) {
                type = NavType.IntType
            })
        ){
            ReviewCreatePage(
                navController = navController,
                onNavigateUp = { navController.navigateUp() }
            )
        }

        composable(
            route = CourtReviewPageDestination.routeWithArgs,
            arguments = listOf(navArgument(CourtReviewPageDestination.courtIdArg) {
                type = NavType.IntType
            })
        ){
            CourtReviewPage(
                navController = navController,
                onNavigateUp = { navController.navigateUp() }
            )
        }
        
        composable(
            route = CourtsAvailableDestination.routeWithArgs,
            arguments = listOf(navArgument(CourtsAvailableDestination.sportArg) {
                type = NavType.StringType
            })
        ){
            CourtsAvailable(
                navController = navController,
                navigateToCourtReservation = { navController.navigate("${CourtReservation.route}/${it}" ) },
            )
        }

        composable(
            route = CourtReservation.routeWithArgs,
            arguments = listOf(navArgument(CourtReservation.courtArg) {
                type = NavType.IntType
            })
        ){
            CourtReservation(
                navController = navController,
                onNavigateUp = { navController.navigateUp() }

            )
        }

        composable(
            route = SportPreferencesDestination.route

        ){
            SportPreferences(
                navController = navController,
                //navigateToCourtsAvailable = { navController.navigate("${CourtsAvailableDestination.route}/${it}" ) },
                onNavigateUp = { navController.navigateUp() }
            )
        }
    }

}