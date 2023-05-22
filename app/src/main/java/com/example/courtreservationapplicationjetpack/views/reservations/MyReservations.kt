package com.example.courtreservationapplicationjetpack.views.reservations

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.courtreservationapplicationjetpack.components.BottomBar
import com.example.courtreservationapplicationjetpack.components.MonthCalendar
import com.example.courtreservationapplicationjetpack.models.courts.Court
import com.example.courtreservationapplicationjetpack.models.reservations.Reservation
import com.example.courtreservationapplicationjetpack.navigation.NavigationDestination
import com.example.courtreservationapplicationjetpack.ui.appViewModel.AppViewModelProvider

object MyReservationsDestination : NavigationDestination {
    override val route = "my_reservation"
    override val titleRes = "My Reservations"
    override val icon = Icons.Default.Star
}

@ExperimentalMaterial3Api
@Composable
fun MyReservations(
    navigateToEditReservation: (Int) -> Unit,
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: MyReservationsViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val myReservationsUiState by viewModel.myReservationsUiState.collectAsState()
    val reservationCourtsState by viewModel.reservationCourtsState.collectAsState()

    viewModel.setCourts(myReservationsUiState.reservationList.map { it.courtId })

    Scaffold(
        bottomBar = { BottomBar(navController = navController as NavHostController) }
    ) {
            innerPadding ->
        MyReservationsBody(
            reservationList = myReservationsUiState.reservationList,
            courtList = reservationCourtsState.courtList,
            modifier = modifier.padding(innerPadding),
            onReservationClick = navigateToEditReservation,
        )
    }

}

@Composable
fun MyReservationsBody(
    modifier: Modifier = Modifier,
    reservationList: List<Reservation>,
    courtList: List<Court>,
    onReservationClick: (Int) -> Unit,
    ){
    Column(
        modifier = modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        if (reservationList.isEmpty() && courtList.isEmpty()) {
            MonthCalendar(
                reservations = emptyList(),
                courts = emptyList(),
                onReservationClick = { onReservationClick(it.id!!) }
            )
        } else {
            MonthCalendar(
                reservations = reservationList,
                courts = courtList,
                onReservationClick = { onReservationClick(it.id!!) }
            )
        }
    }
}
