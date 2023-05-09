package com.example.courtreservationapplicationjetpack.views.reservations

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.courtreservationapplicationjetpack.CourtTopAppBar
import com.example.courtreservationapplicationjetpack.R
import com.example.courtreservationapplicationjetpack.components.BottomBar
import com.example.courtreservationapplicationjetpack.views.courts.ReservationInputForm
import com.example.courtreservationapplicationjetpack.navigation.NavigationDestination
import com.example.courtreservationapplicationjetpack.ui.appViewModel.AppViewModelProvider
import kotlinx.coroutines.launch


object ReservationDetailsDestination : NavigationDestination {
    override val route = "reservation_details"
    const val reservationIdArg = "reservationId"
    val routeWithArgs = "$route/{$reservationIdArg}"
    override val titleRes = "Reservation Details"
    override val icon = Icons.Default.Star


}

@ExperimentalMaterial3Api
@Composable
fun ReservationDetails(
    navigateToEditReservation: (Int) -> Unit,
    navController: NavController,
    navigateBack: () -> Unit,

    modifier: Modifier = Modifier,
    viewModel: ReservationDetailsViewModel = viewModel(factory = AppViewModelProvider.Factory)

) {
    val uiState = viewModel.uiState.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    Scaffold(
        topBar = {
            CourtTopAppBar(canNavigateBack = true,
                navigateUp = navigateBack)
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navigateToEditReservation(uiState.value.reservationDetails.id!!) },
                modifier = Modifier.navigationBarsPadding()
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "edit",
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        },
        bottomBar = { BottomBar(navController = navController as NavHostController) }

    ) {
            innerPadding ->
        ReservationDetailsBody(
            navController = rememberNavController(),
            modifier = modifier.padding(innerPadding),
            reservationDetailsUiState = uiState.value,
            onDelete = {
                // Note: If the user rotates the screen very fast, the operation may get cancelled
                // and the item may not be deleted from the Database. This is because when config
                // change occurs, the Activity will be recreated and the rememberCoroutineScope will
                // be cancelled - since the scope is bound to composition.
                coroutineScope.launch {
                    viewModel.deleteReservation()
                    //navigateBack()
                }
            },
            //navigateToEditReservation = navigateToEditReservation
        )
    }

}

@Composable
private fun ReservationDetailsBody(
    navController: NavController = rememberNavController(),

    reservationDetailsUiState: ReservationDetailsUiState,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        var deleteConfirmationRequired by rememberSaveable { mutableStateOf(false) }
        ReservationInputForm(reservationDetails = reservationDetailsUiState.reservationDetails, enabled = false)

        OutlinedButton(
            onClick = { deleteConfirmationRequired = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("delete")
        }
        if (deleteConfirmationRequired) {
            DeleteConfirmationDialog(
                onDeleteConfirm = {
                    deleteConfirmationRequired = false
                    onDelete()
                },
                onDeleteCancel = { deleteConfirmationRequired = false }
            )
        }
    }
}

@Composable
private fun DeleteConfirmationDialog(
    onDeleteConfirm: () -> Unit,
    onDeleteCancel: () -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        onDismissRequest = { /* Do nothing */ },
        title = { Text("attention") },
        text = { Text("you want to delete?") },
        modifier = modifier.padding(16.dp),
        dismissButton = {
            TextButton(onClick = onDeleteCancel) {
                Text(text = stringResource(R.string.no))
            }
        },
        confirmButton = {
            TextButton(onClick = onDeleteConfirm) {
                Text(text = stringResource(R.string.yes))
            }
        }
    )
}



