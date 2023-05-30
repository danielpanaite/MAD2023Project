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
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.courtreservationapplicationjetpack.components.BottomBar
import com.example.courtreservationapplicationjetpack.components.MonthCalendar
import com.example.courtreservationapplicationjetpack.navigation.NavigationDestination

object MyReservationsDestination : NavigationDestination {
    override val route = "my_reservation"
    override val titleRes = "My Reservations"
    override val icon = Icons.Default.Star
}

@ExperimentalMaterial3Api
@Composable
fun MyReservations(
    navigateToEditReservation: (String) -> Unit,
    navController: NavController,
    modifier: Modifier = Modifier
) {

    Scaffold(
        bottomBar = { BottomBar(navController = navController as NavHostController) }
    ) {
            innerPadding ->
        MyReservationsBody(
            modifier = modifier.padding(innerPadding),
            onReservationClick = navigateToEditReservation,
        )
    }

}

@Composable
fun MyReservationsBody(
    modifier: Modifier = Modifier,
    onReservationClick: (String) -> Unit,
    ){
    Column(
        modifier = modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        MonthCalendar(
            onReservationClick = { onReservationClick(it.id) }
        )
    }
}
