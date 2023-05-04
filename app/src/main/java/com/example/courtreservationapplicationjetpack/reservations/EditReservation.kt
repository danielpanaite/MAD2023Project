package com.example.courtreservationapplicationjetpack.reservations

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.courtreservationapplicationjetpack.CourtTopAppBar
import com.example.courtreservationapplicationjetpack.components.BottomBar
import com.example.courtreservationapplicationjetpack.navigation.NavigationDestination
import com.example.courtreservationapplicationjetpack.ui.appViewModel.AppViewModelProvider
import kotlinx.coroutines.launch
import com.example.courtreservationapplicationjetpack.courts.ReservationEntryBody


object EditReservationDestination : NavigationDestination {
    override val route = "edit_reservation"
    const val reservationIdArg = "reservationId"
    val routeWithArgs = "$route/{$reservationIdArg}"
    override val titleRes = "Edit"
    override val icon = Icons.Default.Edit

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditReservation(
    navController: NavController,

    navigateBack: () -> Unit,
    onNavigateUp: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: EditReservationViewModel = viewModel(factory = AppViewModelProvider.Factory)

){
    val coroutineScope = rememberCoroutineScope()
    Scaffold(
        topBar = {
            CourtTopAppBar(canNavigateBack = true,
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
                    viewModel.updateReservation()
                    //navigateToMyReservations()
                }
            },
            modifier = modifier.padding(innerPadding),
        )
    }

}
