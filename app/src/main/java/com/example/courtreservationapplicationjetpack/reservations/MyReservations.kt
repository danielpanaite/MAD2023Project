package com.example.courtreservationapplicationjetpack.reservations

import androidx.annotation.StringRes
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
import androidx.compose.ui.Alignment
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
import com.example.courtreservationapplicationjetpack.models.Reservations
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
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
            reservationList = myReservationsUiState.reservationsList,
            navController = rememberNavController(),
            modifier = modifier.padding(innerPadding),
            onReservationClick = navigateToReservationDetailsDestination,
        )
    }

}



@Composable
fun MyReservationsBody(
    reservationList: List<Reservations>,
    navController: NavController = rememberNavController(),
    modifier: Modifier = Modifier,
    //navigateToDetailsReservation: () -> Unit
    onReservationClick: (Int) -> Unit,


){
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        ReservationsListHeader()
        Divider()
        if (reservationList.isEmpty()) {
            Text(
                text = stringResource(R.string.no_reservations_description),
                style = MaterialTheme.typography.bodySmall //trovare subtitle
            )
        } else {
            ReservationsList(reservationList = reservationList,
                //navigateToDetailsReservation = navigateToDetailsReservation
                onReservationClick = { onReservationClick(it.id) }

        )
        }
    }
}

@Composable
private fun ReservationsList(
    reservationList: List<Reservations>,
    //navigateToDetailsReservation: () -> Unit,
    modifier: Modifier = Modifier,
    onReservationClick: (Reservations) -> Unit,
) {
    LazyColumn(modifier = modifier, verticalArrangement = Arrangement.spacedBy(8.dp)) {
        items(items = reservationList, key = { it.id }
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
private fun ReservationsListHeader(modifier: Modifier = Modifier) {
    Row(modifier = modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        headerList.forEach {
            Text(
                text = stringResource(it.headerStringId),
                modifier = Modifier.weight(it.weight),
                style = MaterialTheme.typography.bodyMedium //h6
            )
        }
    }
}

@Composable
private fun ReservationItem(
    reservation: Reservations,
    //navigateToDetailsReservation: () -> Unit,
    modifier: Modifier = Modifier,
            onReservationClick: (Reservations) -> Unit,

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

private data class ReservationsHeader(@StringRes val headerStringId: Int, val weight: Float)

private val headerList = listOf(
    ReservationsHeader(headerStringId = R.string.reservation, weight = 0.7f),
    ReservationsHeader(headerStringId = R.string.user_id, weight = 0.7f),
    ReservationsHeader(headerStringId = R.string.court_id, weight = 0.7f),
    ReservationsHeader(headerStringId = R.string.date, weight = 0.7f),
    ReservationsHeader(headerStringId = R.string.slot, weight = 0.7f),
    ReservationsHeader(headerStringId = R.string.additions, weight = 0.7f),
    ReservationsHeader(headerStringId = R.string.people, weight = 0.7f),
)



@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun MyReservationsPreview() {
        ReservationsList(
            listOf(
                Reservations(1, 1, 1, "10-12-2023", "11.30-12.30", "", 3),
                Reservations(2, 1, 2, "11-03-2023", "11.30-12.30", "", 4),
                Reservations(3, 1, 3, "15-04-2023", "11.30-12.30", "", 6)
            ),
            //navigateToDetailsReservation = {}
            onReservationClick = {  }
        )
}
