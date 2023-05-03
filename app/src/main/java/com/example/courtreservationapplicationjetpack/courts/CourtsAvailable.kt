package com.example.courtreservationapplicationjetpack.courts

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.courtreservationapplicationjetpack.CourtTopAppBar
import com.example.courtreservationapplicationjetpack.components.BottomBar
import com.example.courtreservationapplicationjetpack.navigation.NavigationDestination

object CourtsAvailableDestination : NavigationDestination {
    override val route  = "my_courts"
    override val titleRes = "My Courts"
    override val icon = Icons.Default.Star
    const val sportChoosen = "reservationId"

}



@ExperimentalMaterial3Api
@Composable
fun CourtsAvailable(

    navController: NavController,
    modifier: Modifier = Modifier,
    //viewModel: MyCourtsViewModel = viewModel(factory = AppViewModelProvider.Factory)


) {
    //val myReservationsUiState by viewModel.myReservationsUiState.collectAsState()
    Scaffold(
        topBar = {
            CourtTopAppBar(canNavigateBack = false)
        },
        bottomBar = { BottomBar(navController = navController as NavHostController) }

    ) {
            innerPadding ->
        CourtsBody(
            //reservationList = myReservationsUiState.reservationsList,
            //navController = rememberNavController(),
            //modifier = modifier.padding(innerPadding),
            //onReservationClick = navigateToReservationDetailsDestination,
        )
    }

}
@Composable
fun CourtsBody(){

}

