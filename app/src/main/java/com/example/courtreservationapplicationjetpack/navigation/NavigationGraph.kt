package com.example.courtreservationapplicationjetpack.navigation


import android.content.Context
import android.util.Log
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.courtreservationapplicationjetpack.Screen
import com.example.courtreservationapplicationjetpack.SplashScreen
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
import com.example.courtreservationapplicationjetpack.views.notifications.FindFriends
import com.example.courtreservationapplicationjetpack.views.notifications.FindFriendsDestination
import com.example.courtreservationapplicationjetpack.views.notifications.NotificationScreenDestination
import com.example.courtreservationapplicationjetpack.views.notifications.Notifications
import com.example.courtreservationapplicationjetpack.views.profile.Achievements
import com.example.courtreservationapplicationjetpack.views.profile.AchievementsDestination
import com.example.courtreservationapplicationjetpack.views.profile.EditProfile
import com.example.courtreservationapplicationjetpack.views.profile.EditProfileDestination
import com.example.courtreservationapplicationjetpack.views.profile.Friends
import com.example.courtreservationapplicationjetpack.views.profile.FriendsDestination
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
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable


@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun NavigationGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    context: Context,
    googleAuthUiClient: GoogleAuthUiClient
){
    AnimatedNavHost(
        navController = navController,
        startDestination = Screen.Splash.route,
        modifier = modifier
    ){
        composable(
            route = Screen.Splash.route
        ) {
            SplashScreen(navController = navController)
        }

        composable(
            route = MainScreenDestination.route
        ){
            MainScreen(
                navController = navController,
                navigateToAllSports = {navController.navigate(AllSportsDestination.route)},
                navigateToReviews = {navController.navigate(ReviewMainPageDestination.route)},
                navigateToFindFriends = {navController.navigate(FindFriendsDestination.route)},
                googleAuthUiClient = googleAuthUiClient
            )
        }

        composable(
            route = NotificationScreenDestination.route
        ){
            Notifications(
                navController = navController,
                googleAuthUiClient = googleAuthUiClient
            )
        }

        composable(
            route = FriendsDestination.route
        ){
            Friends(
                navController = navController,
                googleAuthUiClient = googleAuthUiClient,
                navigateBack = { navController.navigateUp() }
            )
        }

        composable(
            route = FindFriendsDestination.route
        ){
            FindFriends(
                navController = navController,
                googleAuthUiClient = googleAuthUiClient,
                navigateBack = { navController.navigateUp() }
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
                googleAuthUiClient = googleAuthUiClient
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
                reservationArg = it.arguments?.getString("reservationIdArg"),
                googleAuthUiClient = googleAuthUiClient
            )
        }
        composable(
            route = AllSportsDestination.route

        ){
            AllSports(
                navController = navController,
                onNavigateUp = { navController.navigateUp() }
            )
        }

        composable(
            route = ReviewCreatePageDestination.routeWithArgs,
            arguments = listOf(
                navArgument("courtIdArg") {
                    type = NavType.StringType
                },
                navArgument("reservationIdArg") {
                    type = NavType.StringType
                },
            )
        ){
                backStackEntry ->
            val courtIdArg = backStackEntry.arguments?.getString("courtIdArg").orEmpty()
            val reservationIdArg = backStackEntry.arguments?.getString("reservationIdArg").orEmpty()

            Log.d("courtIdArg", courtIdArg)
            Log.d("reservationIdArg", reservationIdArg)
            ReviewCreatePage(
                navController = navController,
                onNavigateUp = { navController.navigateUp() },
                courtIdArg = courtIdArg,
                reservationIdArg = reservationIdArg,
                googleAuthUiClient = googleAuthUiClient
            )
        }

        composable(
            route = ReviewMainPageDestination.route
        ){
            ReviewMainPage(
                navController = navController,
                onNavigateUp = { navController.navigateUp() },
                navigateToCreateReview = { navController.navigate("${ReviewCreatePageDestination.route}/$it") },
                googleAuthUiClient = googleAuthUiClient
            )
        }


        composable(
            route = CourtReviewPageDestination.routeWithArgs,
            arguments = listOf(navArgument(CourtReviewPageDestination.courtIdArg) {
                type = NavType.StringType
            })
        ){
            it.arguments?.getString("courtIdArg")?.let { it1 ->
                CourtReviewPage(
                    navController = navController,
                    navigateBack = { navController.popBackStack() },
                    onNavigateUp = { navController.navigateUp() },
                    courtIdArg = it1,
                )
            }
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
                    defaultValue = null // Default value for optional argument
                    nullable = true // Optional
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
                navigateToCourtReservation = { navController.navigate("${route}/${it}") },
                googleAuthUiClient = googleAuthUiClient
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
                navigateToProfileDestination = {navController.navigate(ProfileDestination.route)},
                googleAuthUiClient = googleAuthUiClient,



                )
        }

        composable(
            route = NewAchievementsDestination.route

        ){
            NewAchievements(
                navController = navController,
                onNavigateUp = { navController.navigateUp() },
                navigateToAchievementsDestination = {navController.navigate(AchievementsDestination.route)},
                googleAuthUiClient = googleAuthUiClient,
                )
        }

        composable(
            route = SignInDestination.route

        ){
            SignIn(
                navController = navController,
                context = context,
                googleAuthUiClient = googleAuthUiClient
            )
        }

        composable(
            route = ProfileGoogleDestination.route

        ){
            ProfileGoogle(
                navController = navController,
                context = context,
                googleAuthUiClient = googleAuthUiClient
            )
        }
    }
}