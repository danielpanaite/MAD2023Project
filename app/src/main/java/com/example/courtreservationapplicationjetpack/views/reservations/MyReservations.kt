package com.example.courtreservationapplicationjetpack.views.reservations

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.courtreservationapplicationjetpack.CourtTopAppBar
import com.example.courtreservationapplicationjetpack.R
import com.example.courtreservationapplicationjetpack.components.BottomBar
import com.example.courtreservationapplicationjetpack.models.reservations.Reservation
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.courtreservationapplicationjetpack.components.MonthCalendar
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
    navigateToReservationDetailsDestination: (Int) -> Unit,
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: MyReservationsViewModel = viewModel(factory = AppViewModelProvider.Factory)


) {
    val myReservationsUiState by viewModel.myReservationsUiState.collectAsState()
    Scaffold(
        topBar = {
            CourtTopAppBar(canNavigateBack = false)
        },
        bottomBar = { BottomBar(navController = navController as NavHostController) }

    ) {
            innerPadding ->
        MyReservationsBody(
            reservationList = myReservationsUiState.reservationList,
            navController = rememberNavController(),
            modifier = modifier.padding(innerPadding),
            onReservationClick = navigateToReservationDetailsDestination,
        )
    }

}



@Composable
fun MyReservationsBody(
    modifier: Modifier = Modifier,
    reservationList: List<Reservation>,
    navController: NavController = rememberNavController(),
    //navigateToDetailsReservation: () -> Unit
    onReservationClick: (Int) -> Unit,


    ){
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        if (reservationList.isEmpty()) {
            Text(
                text = stringResource(R.string.no_reservations_description),
                style = MaterialTheme.typography.bodySmall
            )
        } else {
            MonthCalendar(reservations = reservationList)
//            ReservationsList(reservationList = reservationList,
//                //navigateToDetailsReservation = navigateToDetailsReservation
//                onReservationClick = { onReservationClick(it.id!!) }
        }
    }
}

@Composable
private fun ReservationsList(
    reservationList: List<Reservation>,
    //navigateToDetailsReservation: () -> Unit,
    modifier: Modifier = Modifier,
    onReservationClick: (Reservation) -> Unit,
) {
    LazyColumn(modifier = modifier, verticalArrangement = Arrangement.spacedBy(8.dp)) {
        items(items = reservationList, key = { it.id!! }
        ) { reservation ->
            ReservationItem(reservation = reservation,
                //navigateToDetailsReservation = navigateToDetailsReservation
                onReservationClick = onReservationClick
            )
            Divider()
        }
    }
}

@Composable
private fun ReservationItem(
    reservation: Reservation,
    //navigateToDetailsReservation: () -> Unit,
    modifier: Modifier = Modifier,
    onReservationClick: (Reservation) -> Unit,

    ) {
    Row(modifier = modifier
        .fillMaxWidth()
        .clickable {
            //navigateToDetailsReservation()
            onReservationClick(reservation)
        }
        .padding(vertical = 16.dp)
    ) {
        Text(text = reservation.id.toString(),  modifier = Modifier.weight(0.7f), fontWeight = FontWeight.Bold)
        Text(text = reservation.user.toString(), modifier = Modifier.weight(0.7f))
        Text(text = reservation.courtId.toString(), modifier = Modifier.weight(0.7f))
        Text(text = reservation.date, modifier = Modifier.weight(0.7f))
        Text(text = reservation.slot, modifier = Modifier.weight(0.7f))
        Text(text = reservation.additions, modifier = Modifier.weight(0.7f))
        Text(text = reservation.people.toString(), modifier = Modifier.weight(0.7f))

    }
}

@Preview(showBackground = true)
@Composable
fun MyReservationsPreview() {
        ReservationsList(
            listOf(
                Reservation(1, 1, 1, "10-12-2023", "11.30-12.30", "", 3),
                Reservation(2, 1, 2, "11-03-2023", "11.30-12.30", "", 4),
                Reservation(3, 1, 3, "15-04-2023", "11.30-12.30", "", 6)
            ),
            //navigateToDetailsReservation = {}
            onReservationClick = {  }
        )
}
