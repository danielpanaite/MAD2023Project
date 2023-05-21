package com.example.courtreservationapplicationjetpack.views.reservations

import android.widget.Toast
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.courtreservationapplicationjetpack.CourtTopAppBar
import com.example.courtreservationapplicationjetpack.components.BottomBar
import com.example.courtreservationapplicationjetpack.navigation.NavigationDestination
import com.example.courtreservationapplicationjetpack.ui.appViewModel.AppViewModelProvider
import com.example.courtreservationapplicationjetpack.views.courts.ReservationEntryBody
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


object EditReservationDestination : NavigationDestination {
    override val route = "edit_reservation"
    const val reservationIdArg = "reservationId"
    val routeWithArgs = "$route/{$reservationIdArg}"
    override val titleRes = "Edit"
    override val icon = Icons.Default.Edit

}

@Composable
fun EditReservation(
    navController: NavController,
    onNavigateUp: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: EditReservationViewModel = viewModel(factory = AppViewModelProvider.Factory)
){
    val toastUpdate = Toast.makeText(LocalContext.current, "Reservation updated!", Toast.LENGTH_SHORT)
    val toastDelete = Toast.makeText(LocalContext.current, "Reservation deleted!", Toast.LENGTH_SHORT)
    val coroutineScope = rememberCoroutineScope()
    Scaffold(
        topBar = { CourtTopAppBar(canNavigateBack = true, navigateUp = onNavigateUp, text = "Reservation details") },
        bottomBar = { BottomBar(navController = navController as NavHostController) }
    ) {

            innerPadding ->
        ReservationEntryBody(
            reservationsUiState = viewModel.reservationsUiState,
            onReservationValueChange = viewModel::updateUiState,
            onSaveClick = {
                coroutineScope.launch {
                    viewModel.updateReservation()
                }
                toastUpdate.show()
                navController.navigate(MyReservationsDestination.route)
            },
            onDeleteClick = {
                CoroutineScope(Dispatchers.IO).launch {
                    viewModel.deleteReservation()
                }
                toastDelete.show()
                navController.navigate(MyReservationsDestination.route)
            },
            modifier = modifier.padding(innerPadding),
        )
    }

}
