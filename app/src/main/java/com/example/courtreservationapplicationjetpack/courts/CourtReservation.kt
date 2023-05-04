package com.example.courtreservationapplicationjetpack.courts


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview

import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.courtreservationapplicationjetpack.ui.appViewModel.AppViewModelProvider
import com.example.courtreservationapplicationjetpack.CourtTopAppBar
import com.example.courtreservationapplicationjetpack.components.BottomBar
import com.example.courtreservationapplicationjetpack.navigation.NavigationDestination
import com.example.courtreservationapplicationjetpack.reservations.ReservationDetails
import com.example.courtreservationapplicationjetpack.reservations.ReservationsUiState
import kotlinx.coroutines.launch



object CourtReservation : NavigationDestination {
        override val route  = "court_reservation"
        override val titleRes = "Court Reservation"
        override val icon = Icons.Default.Star
        const val courtArg = "courtArg"
        val routeWithArgs = "$route/{$courtArg}"

    }




@ExperimentalMaterial3Api
@Composable
fun CourtReservation(
    navigateBack: () -> Unit,
    onNavigateUp: () -> Unit,
    canNavigateBack: Boolean = true,
    //navigateToMyReservations: () -> Unit,
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: CourtReservationViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val coroutineScope = rememberCoroutineScope()
    Scaffold(
        topBar = {
            CourtTopAppBar(canNavigateBack = canNavigateBack,
                navigateUp = onNavigateUp)
        },
        bottomBar = { BottomBar(navController = navController as NavHostController) }

    ) {



            innerPadding ->
        ReservationEntryBody(
            reservationsUiState = viewModel.reservationsUiState,
            onReservationValueChange = viewModel::updateUiState,
            onSaveClick = {
                coroutineScope.launch {
                    viewModel.saveReservation()
                    //navigateToMyReservations()
                }
            },
            modifier = modifier.padding(innerPadding),
        )
    }

}




@Composable
fun ReservationEntryBody(
    reservationsUiState: ReservationsUiState,
    onReservationValueChange: (ReservationDetails) -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier
){

    Column (
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(32.dp)
    ){
        ReservationInputForm(reservationDetails = reservationsUiState.reservationDetails, onValueChange = onReservationValueChange)
        Button(onClick = onSaveClick,
            enabled = reservationsUiState.isEntryValid,
            modifier = Modifier.fillMaxWidth())
        {
            Text(text = "save")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReservationInputForm(
    reservationDetails: ReservationDetails,
    modifier: Modifier = Modifier,
    onValueChange: (ReservationDetails) -> Unit ={},
    enabled: Boolean = true
){
    Column(modifier=modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(16.dp))
    {
        //user id gia impostato da noi e anche court id perchè è quello che sceglie prima da eliminare questi primi due

        OutlinedTextField(
            value =reservationDetails.user ,
            onValueChange = {onValueChange(reservationDetails.copy(user = it))},
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            label = {Text(text = "userId")},
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )
        OutlinedTextField(
            value =reservationDetails.courtId ,
            onValueChange = {onValueChange(reservationDetails.copy(courtId = it))},
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            label = {Text(text = "courtId")},
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )


        OutlinedTextField(
            value =reservationDetails.date ,
            onValueChange = {onValueChange(reservationDetails.copy(date = it))},
            label = {Text(text = "date")},
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )
        OutlinedTextField(
            value =reservationDetails.slot ,
            onValueChange = {onValueChange(reservationDetails.copy(slot = it))},
            label = {Text(text = "id")},
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )
        OutlinedTextField(
            value =reservationDetails.additions ,
            onValueChange = {onValueChange(reservationDetails.copy(additions = it))},
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            label = {Text(text = "additions")},
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )
        OutlinedTextField(
            value =reservationDetails.people ,
            onValueChange = {onValueChange(reservationDetails.copy(people = it))},
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            label = {Text(text = "people")},
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )
    }


}



@ExperimentalMaterial3Api
@Preview(showBackground = true)
@Composable
private fun ItemEntryScreenPreview() {
    ReservationEntryBody(
        reservationsUiState = ReservationsUiState(
            ReservationDetails(
                id = 1,
                user = "1",
                courtId = "2",
                date = "20-12-2023",
                slot = "11.30-12.30",
                additions = "",
                people = "2"
            )

        ),
        onReservationValueChange = {},
        onSaveClick = {}
    )

}
