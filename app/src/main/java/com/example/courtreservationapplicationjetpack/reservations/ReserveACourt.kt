package com.example.courtreservationapplicationjetpack.reservations

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.navigation.compose.rememberNavController
import com.example.courtreservationapplicationjetpack.AppViewModelProvider
import com.example.courtreservationapplicationjetpack.CourtTopAppBar
import com.example.courtreservationapplicationjetpack.components.BottomBar
import com.example.courtreservationapplicationjetpack.navigation.Screens
//import com.example.courtreservationapplicationjetpack.routes.MyReservationsBody
import kotlinx.coroutines.launch




@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@ExperimentalMaterial3Api
@Composable
fun ReserveACourt(
    //navigateToMyReservations: () -> Unit,
    navController: NavController,
    modifier: Modifier = Modifier,
    //viewModel: ReserveACourtViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val coroutineScope = rememberCoroutineScope()
    Scaffold(
        topBar = {
            CourtTopAppBar(canNavigateBack = false)
        },
        bottomBar = { BottomBar(navController = navController as NavHostController) }

    ) {

    }}
        /*
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
    */



/*
@Composable
fun ReservationEntryBody(
    reservationsUiState: ReservationsUiState,
    onReservationValueChange: (ReservationsUiState) -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier
){

    Column (
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(32.dp)
    ){
        ReservationInputForm(reservationsUiState = reservationsUiState, onValueChange = onReservationValueChange)
        Button(onClick = onSaveClick,
        enabled = reservationsUiState.actionEnabled,
        modifier = Modifier.fillMaxWidth())
        {
            Text(text = "save")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReservationInputForm(
    reservationsUiState: ReservationsUiState,
    modifier: Modifier = Modifier,
    onValueChange: (ReservationsUiState) -> Unit ={},
    enabled: Boolean = true
){
    Column(modifier=modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(16.dp))
    {
        OutlinedTextField(
            value =reservationsUiState.user ,
            onValueChange = {onValueChange(reservationsUiState.copy(user = it))},
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            label = {Text(text = "id")},
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )
        OutlinedTextField(
            value =reservationsUiState.courtId ,
            onValueChange = {onValueChange(reservationsUiState.copy(courtId = it))},
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            label = {Text(text = "courtId")},
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )
        OutlinedTextField(
            value =reservationsUiState.date ,
            onValueChange = {onValueChange(reservationsUiState.copy(date = it))},
            label = {Text(text = "date")},
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )
        OutlinedTextField(
            value =reservationsUiState.slot ,
            onValueChange = {onValueChange(reservationsUiState.copy(slot = it))},
            label = {Text(text = "id")},
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )
        OutlinedTextField(
            value =reservationsUiState.additions ,
            onValueChange = {onValueChange(reservationsUiState.copy(additions = it))},
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            label = {Text(text = "additions")},
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )
        OutlinedTextField(
            value =reservationsUiState.people ,
            onValueChange = {onValueChange(reservationsUiState.copy(people = it))},
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
                id = 1,
                user = "1",
                courtId = "2",
                date = "20-12-2023",
                slot = "11.30-12.30",
                additions = "",
                people = "2"
            ),
            onReservationValueChange = {},
            onSaveClick = {}
        )

}
*/