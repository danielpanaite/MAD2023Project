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
import androidx.compose.material.icons.filled.Place
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
import com.example.courtreservationapplicationjetpack.ui.appViewModel.AppViewModelProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

object AllSportsDestination : NavigationDestination {
    override val route  = "all_sports"
    override val titleRes = "All sports"
    override val icon = Icons.Default.Place

}



@ExperimentalMaterial3Api
@Composable
fun AllSports(
    navController: NavController,
    modifier: Modifier = Modifier,
    navigateToCourtsAvailable: (String) -> Unit,
    //cercare di riprendere uesta stringa per prendere tutti i campi di quello sport
    onNavigateUp: () -> Unit,

    viewModel: AllSportsViewModel = viewModel(factory = AppViewModelProvider.Factory)



) {
    val allSportsUiState by viewModel.allSportsUiState.collectAsState()
    //val myReservationsUiState by viewModel.myReservationsUiState.collectAsState()
    Scaffold(
        topBar = {
            CourtTopAppBar(canNavigateBack = false)
        },
        bottomBar = { BottomBar(navController = navController as NavHostController) }

    ) {
            innerPadding ->
        SportsBody(
            sportsList = allSportsUiState.sportsList,
            modifier = modifier.padding(innerPadding),
            onSportClick = navigateToCourtsAvailable,
        )
    }

}
@Composable
fun SportsBody(
    sportsList: List<String>,
    modifier: Modifier = Modifier,
    onSportClick: (String) -> Unit,

    ){

        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            //ReservationsListHeader()
            //Divider()
            if (sportsList.isEmpty()) {
                Text(
                    text = "no sports in the db",
                    style = MaterialTheme.typography.bodySmall //trovare subtitle
                )
            } else {
                SportsList(
                    sportsList = sportsList,
                    onSportClick = onSportClick
                )
            }
        }
    }

    @Composable
    private fun SportsList(
        sportsList: List<String>,
        modifier: Modifier = Modifier,
        onSportClick: (String) -> Unit,
    ) {
        LazyColumn(modifier = modifier, verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(items = sportsList, //key = { it.id }
            ) { sport ->
                SportItem(sport = sport,
                    //navigateToDetailsReservation = navigateToDetailsReservation
                    onSportClick = onSportClick
                )
                Divider()
            }
        }
    }


    @Composable
    private fun SportItem(
        sport: String,
        modifier: Modifier = Modifier,
        onSportClick: (String) -> Unit,

        ) {
        Row(modifier = modifier
            .fillMaxWidth()
            .clickable {
                onSportClick(sport)
            }
            .padding(vertical = 16.dp)
        ) {
            Text(text = sport,  modifier = Modifier.weight(0.7f), fontWeight = FontWeight.Bold)


        }
    }



