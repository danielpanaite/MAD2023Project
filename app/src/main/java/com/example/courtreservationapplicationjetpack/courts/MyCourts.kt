package com.example.courtreservationapplicationjetpack.courts

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.courtreservationapplicationjetpack.CourtTopAppBar
import com.example.courtreservationapplicationjetpack.components.BottomBar
import com.example.courtreservationapplicationjetpack.navigation.NavigationDestination
import com.example.courtreservationapplicationjetpack.reservations.MyReservationsBody
import com.example.courtreservationapplicationjetpack.reservations.MyReservationsViewModel
import com.example.courtreservationapplicationjetpack.ui.appViewModel.AppViewModelProvider

object MyCourts : NavigationDestination {
    override val route  = "my_courts"
    override val titleRes = "My Courts"
    override val icon = Icons.Default.Star


}

@ExperimentalMaterial3Api
@Composable
fun MyCourts(
    navigateToReserveACourt: (Int) -> Unit,
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
        MyCourtsBody(
            //reservationList = myReservationsUiState.reservationsList,
            //navController = rememberNavController(),
            //modifier = modifier.padding(innerPadding),
            //onReservationClick = navigateToReservationDetailsDestination,
        )
    }

}
@Composable
fun MyCourtsBody(){

}