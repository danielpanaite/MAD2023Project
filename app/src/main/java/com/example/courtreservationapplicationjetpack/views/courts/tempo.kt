//package com.example.courtreservationapplicationjetpack.views.courts
//
//
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.offset
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.rememberLazyListState
//import androidx.compose.foundation.text.KeyboardOptions
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.Star
//import androidx.compose.material3.AlertDialog
//import androidx.compose.material3.Button
//import androidx.compose.material3.Card
//import androidx.compose.material3.ExperimentalMaterial3Api
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.OutlinedButton
//import androidx.compose.material3.OutlinedTextField
//import androidx.compose.material3.Scaffold
//import androidx.compose.material3.Text
//import androidx.compose.material3.TextButton
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.collectAsState
//import androidx.compose.runtime.derivedStateOf
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.saveable.rememberSaveable
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.shadow
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.graphics.graphicsLayer
//import androidx.compose.ui.layout.ContentScale
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.res.stringResource
//import androidx.compose.ui.text.input.KeyboardType
//import androidx.compose.ui.unit.dp
//import androidx.lifecycle.viewmodel.compose.viewModel
//import androidx.navigation.NavController
//import androidx.navigation.NavHostController
//import coil.compose.AsyncImage
//import coil.request.ImageRequest
//import com.example.courtreservationapplicationjetpack.CourtTopAppBar
//import com.example.courtreservationapplicationjetpack.R
//import com.example.courtreservationapplicationjetpack.components.BottomBar
//import com.example.courtreservationapplicationjetpack.navigation.NavigationDestination
//import com.example.courtreservationapplicationjetpack.ui.appViewModel.AppViewModelProvider
//import com.example.courtreservationapplicationjetpack.views.reservations.MyReservationsViewModel
//import com.example.courtreservationapplicationjetpack.views.reservations.ReservationDetails
//import com.example.courtreservationapplicationjetpack.views.reservations.ReservationsUiState
//import kotlinx.coroutines.CoroutineScope
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.launch
//
//
//object CourtReservation : NavigationDestination {
//    override val route  = "court_reservation"
//    override val titleRes = "Court Reservation"
//    override val icon = Icons.Default.Star
//    const val courtArg = "courtArg"
//    val routeWithArgs = "$route/{$courtArg}"
//}
//
//@ExperimentalMaterial3Api
//@Composable
//fun CourtReservation(
//    modifier: Modifier = Modifier,
//    navigateBack: () -> Unit,
//    onNavigateUp: () -> Unit,
//    canNavigateBack: Boolean = true,
//    navController: NavController,
//    viewModel: CourtReservationViewModel = viewModel(factory = AppViewModelProvider.Factory)
//) {
//    Scaffold(
//        topBar = {
//            CourtTopAppBar(canNavigateBack = canNavigateBack, navigateUp = onNavigateUp, text = "Reservation Details")
//        },
//        bottomBar = { BottomBar(navController = navController as NavHostController) }
//    ) {
//            innerPadding ->
//        ReservationEntryBody(
//            reservationsUiState = viewModel.reservationsUiState,
//            onReservationValueChange = viewModel::updateUiState,
//            onSaveClick = {
//                CoroutineScope(Dispatchers.IO).launch {
//                    viewModel.saveReservation()
//                }
//                //navController.navigate(MainScreenDestination.route)
//            },
//            onDeleteClick = {
//                CoroutineScope(Dispatchers.IO).launch {
//                    viewModel.saveReservation()
//                }
//            },
//            modifier = modifier.padding(innerPadding),
//        )
//    }
//}
//
//
//
//
//@Composable
//fun ReservationEntryBody(
//    reservationsUiState: ReservationsUiState,
//    onReservationValueChange: (ReservationDetails) -> Unit,
//    onSaveClick: () -> Unit,
//    onDeleteClick: () -> Unit,
//    modifier: Modifier = Modifier,
//    myReservationViewModel: MyReservationsViewModel = viewModel(factory = AppViewModelProvider.Factory)
//){
//    val courtUiState by myReservationViewModel.reservationCourtsState.collectAsState()
//    if(reservationsUiState.reservationDetails.courtId.isNotBlank()){
//        myReservationViewModel.setCourts(listOf(reservationsUiState.reservationDetails.courtId.toInt()))
//    }
//    var deleteConfirmationRequired by rememberSaveable { mutableStateOf(false) }
//    val lazyListState = rememberLazyListState()
//    val firstItemTranslationY by remember {
//        derivedStateOf {
//            when {
//                lazyListState.layoutInfo.visibleItemsInfo.isNotEmpty() &&
//                        lazyListState.firstVisibleItemIndex == 0 ->
//                    lazyListState.firstVisibleItemScrollOffset * .7f
//                else -> 0f
//            }
//        }
//    }
//    println("AAAAAAAAAAAAAAA" + courtUiState)
//    println("BBBBBBBBBBBBBBB" + reservationsUiState)
//    LazyColumn (
//        modifier = modifier
//            .fillMaxWidth(),
//        verticalArrangement = Arrangement.spacedBy(16.dp),
//        state = lazyListState
//    ){
//        item {
//            Box{// Court image box
//                AsyncImage(
//                    model = ImageRequest.Builder(LocalContext.current)
//                        .data("https://www.parrocchiecurtatone.it/wp-content/uploads/2020/07/WhatsApp-Image-2020-07-23-at-17.53.36-1984x1200.jpeg")
//                        .crossfade(true)
//                        .build(),
//                    placeholder = painterResource(R.drawable.placeholder),
//                    contentDescription = "Court Image",
//                    contentScale = ContentScale.Crop,
//                    modifier = Modifier
//                        .height(200.dp)
//                        .graphicsLayer { translationY = firstItemTranslationY }
//                )
//                if(courtUiState.courtList.isNotEmpty()) {
//                    Text(
//                        text = courtUiState.courtList[0].name,
//                        style = MaterialTheme.typography.headlineMedium,
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .align(Alignment.BottomStart)
//                            .padding(bottom = 32.dp, start = 16.dp)
//                            .shadow(50.dp),
//                        color = Color.White,
//                    )
//                }
//            }
//            ReservationInputForm(
//                reservationDetails = reservationsUiState.reservationDetails,
//                onValueChange = onReservationValueChange
//            )
//        }
//        item {
//            Button(onClick = onSaveClick,
//                enabled = reservationsUiState.isEntryValid,
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(start = 16.dp, end = 16.dp))
//            {
//                Text(text = "Save")
//            }
//        }
//        item {
//            OutlinedButton(onClick = { deleteConfirmationRequired = true },
//                enabled = reservationsUiState.isEntryValid,
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(start = 16.dp, end = 16.dp))
//            {
//                Text(text = "Delete")
//            }
//        }
//    }
//
//    if (deleteConfirmationRequired) {
//        DeleteConfirmationDialog(
//            onDeleteConfirm = {
//                deleteConfirmationRequired = false
//                onDeleteClick()
//            },
//            onDeleteCancel = { deleteConfirmationRequired = false }
//        )
//    }
//}
//
//
//@Composable
//fun ReservationInputForm(
//    reservationDetails: ReservationDetails,
//    onValueChange: (ReservationDetails) -> Unit = {}
//){
//    Card( //text fields
//        modifier = Modifier
//            .fillMaxSize()
//            .offset(y = (-16).dp)
//    ){
//        OutlinedTextField(
//            value = reservationDetails.date ,
//            onValueChange = {onValueChange(reservationDetails.copy(date = it))},
//            label = {Text(text = "Date")},
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(16.dp),
//            singleLine = true,
//        )
//        OutlinedTextField(
//            value =reservationDetails.slot ,
//            onValueChange = {onValueChange(reservationDetails.copy(slot = it))},
//            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
//            label = {Text(text = "Time slot")},
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(16.dp),
//            singleLine = true
//        )
//        OutlinedTextField(
//            value = reservationDetails.additions ,
//            onValueChange = {onValueChange(reservationDetails.copy(additions = it))},
//            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
//            label = {Text(text = "Notes")},
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(16.dp),
//            singleLine = true
//        )
//        OutlinedTextField(
//            value =reservationDetails.people ,
//            onValueChange = {onValueChange(reservationDetails.copy(people = it))},
//            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
//            label = {Text(text = "People")},
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(16.dp),
//            singleLine = true
//        )
//    }
//}
//
//@Composable
//private fun DeleteConfirmationDialog(
//    onDeleteConfirm: () -> Unit,
//    onDeleteCancel: () -> Unit,
//    modifier: Modifier = Modifier
//) {
//    AlertDialog(
//        onDismissRequest = { /* Do nothing */ },
//        title = { Text("Warning") },
//        text = { Text("Do you want to delete your reservation?") },
//        modifier = modifier.padding(16.dp),
//        dismissButton = {
//            TextButton(onClick = onDeleteCancel) {
//                Text(text = stringResource(R.string.no))
//            }
//        },
//        confirmButton = {
//            TextButton(onClick = onDeleteConfirm) {
//                Text(text = stringResource(R.string.yes))
//            }
//        }
//    )
//}