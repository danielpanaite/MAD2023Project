package com.example.courtreservationapplicationjetpack.courts

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.courtreservationapplicationjetpack.CourtTopAppBar
import com.example.courtreservationapplicationjetpack.components.BottomBar
import com.example.courtreservationapplicationjetpack.navigation.NavigationDestination
import com.example.courtreservationapplicationjetpack.reservations.ReservationDetailsDestination
import com.example.courtreservationapplicationjetpack.ui.appViewModel.AppViewModelProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.courtreservationapplicationjetpack.models.courts.Court

object CourtsAvailableDestination : NavigationDestination {
    override val route  = "my_courts"
    override val titleRes = "My Courts"
    override val icon = Icons.Default.Star
    const val sportArg = "sportArg"
    val routeWithArgs = "$route/{$sportArg}"

}



@ExperimentalMaterial3Api
@Composable
fun CourtsAvailable(

    navController: NavController,
    modifier: Modifier = Modifier,
    navigateToCourtReservation: (Int) -> Unit,

    viewModel: CourtsAvailableViewModel = viewModel(factory = AppViewModelProvider.Factory)


) {
    val courtsAvailableUiState by viewModel.courtsAvailableUiState.collectAsState()
    Scaffold(
        topBar = {
            CourtTopAppBar(canNavigateBack = false)
        },
        bottomBar = { BottomBar(navController = navController as NavHostController) }

    ) {
            innerPadding ->
        CourtsBody(
            courtList = courtsAvailableUiState.courtsAvailableList,
            modifier = modifier.padding(innerPadding),
            onCourtClick = navigateToCourtReservation,
        )
    }

}

@Composable
fun CourtsBody(
    courtList: List<Court>,
    modifier: Modifier = Modifier,
    onCourtClick: (Int) -> Unit,

    ){

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        //ReservationsListHeader()
        //Divider()
        if (courtList.isEmpty()) {
            Text(
                text = "no court for this sport in the db",
                style = MaterialTheme.typography.bodySmall //trovare subtitle
            )
        } else {
            CourtList(
                courtList = courtList,
                onCourtClick = { onCourtClick(it.id) }
            )
        }
    }
}

@Composable
private fun CourtList(
    courtList: List<Court>,
    modifier: Modifier = Modifier,
    onCourtClick: (Court) -> Unit,
) {
    LazyColumn(modifier = modifier, verticalArrangement = Arrangement.spacedBy(8.dp)) {
        items(items = courtList, //key = { it.id }
        ) { court ->
            CourtItem(court = court,
                //navigateToDetailsReservation = navigateToDetailsReservation
                onCourtClick = onCourtClick
            )
            Divider()
        }
    }
}


@Composable
private fun CourtItem(
    court: Court,
    modifier: Modifier = Modifier,
    onCourtClick: (Court) -> Unit,

    ) {
    Row(modifier = modifier
        .fillMaxWidth()
        .clickable {
            onCourtClick(court)
        }
        .padding(vertical = 16.dp)
    ) {
        Text(text = court.name,  modifier = Modifier.weight(0.7f), fontWeight = FontWeight.Bold)


    }
}





