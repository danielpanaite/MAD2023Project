package com.example.courtreservationapplicationjetpack.navigation


import android.content.Context
import android.util.Log
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.courtreservationapplicationjetpack.signIn.GoogleAuthUiClient
import com.example.courtreservationapplicationjetpack.signIn.ProfileGoogle
import com.example.courtreservationapplicationjetpack.signIn.ProfileGoogleDestination
import com.example.courtreservationapplicationjetpack.signIn.SignIn
import com.example.courtreservationapplicationjetpack.signIn.SignInDestination
import com.example.courtreservationapplicationjetpack.views.MainScreen
import com.example.courtreservationapplicationjetpack.views.MainScreenDestination
import com.example.courtreservationapplicationjetpack.views.courts.AllSports
import com.example.courtreservationapplicationjetpack.views.courts.AllSportsDestination
import com.example.courtreservationapplicationjetpack.views.courts.CourtsAvailable
import com.example.courtreservationapplicationjetpack.views.courts.CourtsAvailableDestination
import com.example.courtreservationapplicationjetpack.views.profile.Achievements
import com.example.courtreservationapplicationjetpack.views.profile.AchievementsDestination
import com.example.courtreservationapplicationjetpack.views.profile.EditProfile
import com.example.courtreservationapplicationjetpack.views.profile.EditProfileDestination
import com.example.courtreservationapplicationjetpack.views.profile.NewAchievements
import com.example.courtreservationapplicationjetpack.views.profile.NewAchievementsDestination
import com.example.courtreservationapplicationjetpack.views.profile.Profile
import com.example.courtreservationapplicationjetpack.views.profile.ProfileDestination
import com.example.courtreservationapplicationjetpack.views.profile.SportPreferences
import com.example.courtreservationapplicationjetpack.views.profile.SportPreferencesDestination
import com.example.courtreservationapplicationjetpack.views.reservations.EditReservation
import com.example.courtreservationapplicationjetpack.views.reservations.EditReservationDestination
import com.example.courtreservationapplicationjetpack.views.reservations.MyReservations
import com.example.courtreservationapplicationjetpack.views.reservations.MyReservationsDestination
import com.example.courtreservationapplicationjetpack.views.reservations.ReservationDetails
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
    modifier: Modifier = Modifier,
    context: Context,
    googleAuthUiClient: GoogleAuthUiClient
){
    NavHost(
        navController = navController,
        startDestination = SignInDestination.route,
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

        composable(
            route = EditProfileDestination.routeWithArgs,
            arguments = listOf(navArgument(EditProfileDestination.profileArg) {
                type = NavType.StringType
            })
        ){
            EditProfile(
                navController = navController,
                navigateBack = { navController.popBackStack() },
                onNavigateUp = { navController.navigateUp() },
                navigateToProfileDestination = {navController.navigate(ProfileDestination.route)},
                context = context,
                googleAuthUiClient = googleAuthUiClient,
                profileArg = it.arguments?.getString("profileArg")

            )
        }
        composable(
            route = ProfileDestination.route
        ){
            Profile(navController = navController,
                navigateToEditProfileDestination = { navController.navigate("${EditProfileDestination.route}/${it}") },
                navigateBack = { navController.navigateUp() },
                navigateToSportPreferencesDestination = {navController.navigate(SportPreferencesDestination.route)},
                navigateToAchievementsDestination = {navController.navigate(AchievementsDestination.route)},
                context = context,
                googleAuthUiClient = googleAuthUiClient

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
                type = NavType.StringType
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
                type = NavType.StringType
            })
        ){
            EditReservation(
                navController = navController,
                onNavigateUp = { navController.navigateUp() },
                reservationArg = it.arguments?.getString("reservationIdArg")
            )
        }
        composable(
            route = AllSportsDestination.route

        ){
            AllSports(
                navController = navController,
//                navigateToCourtsAvailable = { navController.navigate("${CourtsAvailableDestination.route}/${it}" ) },
                onNavigateUp = { navController.navigateUp() }
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
            route = ReviewMainPageDestination.route
        ){
            ReviewMainPage(
                navController = navController,
                onNavigateUp = { navController.navigateUp() },
                navigateToCreateReview = { navController.navigate("${ReviewCreatePageDestination.route}/$it") }
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
            arguments = listOf(
                navArgument("courtID") {
                    type = NavType.StringType
                },
                navArgument("dateArg") {
                    type = NavType.StringType
                },
                navArgument("hourOptArg") {
                    type = NavType.StringType
                    defaultValue = null // Valore predefinito per l'argomento opzionale
                    nullable = true // L'argomento è opzionale
                }
            )
        ) { backStackEntry ->
            val courtID = backStackEntry.arguments?.getString("courtID").orEmpty()
            val dateArg = backStackEntry.arguments?.getString("dateArg").orEmpty()
            val hourOptArg = backStackEntry.arguments?.getString("hourOptArg") ?: ""

            Log.d("CourtID", courtID)
            Log.d("Date", dateArg)
            Log.d("HourOptArg", hourOptArg)

            CourtsAvailable(
                courtID = courtID,
                pickedDate = dateArg,
                hourOptArg = hourOptArg,
                navController = navController,
                navigateToCourtReservation = { navController.navigate("${route}/${it}") }
            )
        }

        composable(
            route = SportPreferencesDestination.route

        ){
            SportPreferences(
                navController = navController,
                onNavigateUp = { navController.navigateUp() },
                navigateToProfileDestination = {navController.navigate(ProfileDestination.route)},
                googleAuthUiClient = googleAuthUiClient

            )
        }

        composable(
            route = AchievementsDestination.route

        ){
            Achievements(
                navController = navController,
                navigateToNewAchievementsDestination = {navController.navigate(NewAchievementsDestination.route)},

                onNavigateUp = { navController.navigateUp() },
                navigateBackAction = { navController.popBackStack() },
                navigateToProfileDestination = {navController.navigate(ProfileDestination.route)}


            )
        }

        composable(
            route = NewAchievementsDestination.route

        ){
            NewAchievements(
                navController = navController,
                onNavigateUp = { navController.navigateUp() },
                navigateToAchievementsDestination = {navController.navigate(AchievementsDestination.route)},



                )
        }

        composable(
            route = SignInDestination.route

        ){
            SignIn(
                navController = navController,
                context = context,
                googleAuthUiClient = googleAuthUiClient
                //navigateToReserveACourt = { navController.navigate(ReserveACourtDestination.route) }
            )
        }

        composable(
            route = ProfileGoogleDestination.route

        ){
            ProfileGoogle(
                navController = navController,
                context = context,
                googleAuthUiClient = googleAuthUiClient
                //navigateToReserveACourt = { navController.navigate(ReserveACourtDestination.route) }
            )
        }
    }
}