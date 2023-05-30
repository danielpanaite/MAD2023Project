package com.example.courtreservationapplicationjetpack.views.reservations

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.chargemap.compose.numberpicker.NumberPicker
import com.example.courtreservationapplicationjetpack.CourtTopAppBar
import com.example.courtreservationapplicationjetpack.R
import com.example.courtreservationapplicationjetpack.firestore.Court
import com.example.courtreservationapplicationjetpack.firestore.CourtViewModel
import com.example.courtreservationapplicationjetpack.firestore.Reservation
import com.example.courtreservationapplicationjetpack.firestore.ReservationViewModel
import com.example.courtreservationapplicationjetpack.firestore.toDate
import com.example.courtreservationapplicationjetpack.firestore.toTime
import com.example.courtreservationapplicationjetpack.models.sport.SportDrawables
import com.example.courtreservationapplicationjetpack.navigation.NavigationDestination
import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.Locale


object EditReservationDestination : NavigationDestination {
    override val route = "edit_reservation"
    const val reservationIdArg = "reservationIdArg"
    val routeWithArgs = "$route/{$reservationIdArg}"
    override val titleRes = "Edit"
    override val icon = Icons.Default.Edit
}

@Composable
fun EditReservation(
    navController: NavController,
    onNavigateUp: () -> Unit,
    reservationArg: String?,
    viewModel: ReservationViewModel = viewModel()
){
    var launchOnce by rememberSaveable { mutableStateOf(true) }
    if(launchOnce){
        viewModel.getReservationById(reservationArg!!)
        launchOnce = false
    }
    val toastUpdate = Toast.makeText(LocalContext.current, "Reservation updated!", Toast.LENGTH_SHORT)
    val toastDelete = Toast.makeText(LocalContext.current, "Reservation deleted!", Toast.LENGTH_SHORT)
    var deleteConfirmationRequired by rememberSaveable { mutableStateOf(false) }
    val reservationDetails by remember { mutableStateOf(viewModel.reservation) } //reservation to be edited
    if (deleteConfirmationRequired) {
        DeleteConfirmationDialog(
            onDeleteConfirm = {
                deleteConfirmationRequired = false
                viewModel.deleteReservation()
                toastDelete.show()
                navController.popBackStack()
            },
            onDeleteCancel = { deleteConfirmationRequired = false }
        )
    }
    Scaffold(
        topBar = { CourtTopAppBar(canNavigateBack = true, navigateUp = onNavigateUp, text = "Reservation details") },
        bottomBar = {
            Column(modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
            ){
                Button(
                    onClick = {
                        viewModel.updateReservation()
                        toastUpdate.show()
                        navController.popBackStack()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Text(text = "Save")
                }
                OutlinedButton(
                    onClick = { deleteConfirmationRequired = true },
                    modifier = Modifier
                        .fillMaxWidth()
                ){
                    Text(text = "Delete")
                }
            }
        }
    ) {
            innerPadding ->
        if(reservationDetails.value.court != "")
            EditReservationForm(
                modifier = Modifier.padding(innerPadding),
                reservation = reservationDetails
            )
    }

}

@Composable
fun EditReservationForm(
    modifier: Modifier = Modifier,
    reservation: MutableState<Reservation>,
    courtViewModel: CourtViewModel = viewModel()
) {
    courtViewModel.getCourtById(reservation.value.court) //get court info to be displayed
    val court = courtViewModel.court.value
    val lazyListState = rememberLazyListState()
    val firstItemTranslationY by remember {
        derivedStateOf {
            when {
                lazyListState.layoutInfo.visibleItemsInfo.isNotEmpty() &&
                        lazyListState.firstVisibleItemIndex == 0 ->
                    lazyListState.firstVisibleItemScrollOffset * .7f
                else -> 0f
            }
        }
    }
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        state = lazyListState
    ) {
        item {
            Box(modifier = Modifier.fillMaxWidth()) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data("https://www.parrocchiecurtatone.it/wp-content/uploads/2020/07/WhatsApp-Image-2020-07-23-at-17.53.36-1984x1200.jpeg")
                        .crossfade(true)
                        .build(),
                    placeholder = painterResource(R.drawable.placeholder),
                    contentDescription = "Court Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .height(200.dp)
                        .graphicsLayer { translationY = firstItemTranslationY }
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            brush = Brush.verticalGradient(
                                0f to Color.Transparent,
                                0.0001f to Color.Transparent,
                                1f to Color.Black
                            )
                        )
                )
            }
        }

        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(y = (-90).dp)
                    .background(
                        color = MaterialTheme.colorScheme.surface,
                        shape = MaterialTheme.shapes.large.copy(
                            topStart = CornerSize(20.dp),
                            topEnd = CornerSize(20.dp)
                        )
                    )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(3f)) {
                            Text(
                                text = court.name,
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = court.address,
                                style = MaterialTheme.typography.labelSmall,
                                color = Color.Gray,
                                textAlign = TextAlign.Start
                            )
                        }
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .align(Alignment.CenterVertically)
                        ) {
                            if(court.id != "")
                                Image(
                                    painter = painterResource(SportDrawables.getDrawable(court.sport)!!),
                                    contentDescription = "Sport icon",
                                    colorFilter = ColorFilter.tint(Color.Black),
                                    modifier = Modifier
                                        .size(29.dp)
                                        .align(Alignment.Center)
                                )
                        }
                    }

                    Divider(
                        color = Color.LightGray,
                        thickness = 1.dp,
                        modifier = Modifier
                            .padding(top = 8.dp)
                            .height(1.dp)
                    )

                    CalendarScreen(court, reservation)
                }
            }
        }
    }
}

@Composable
fun CalendarScreen(
    court: Court,
    reservation: MutableState<Reservation>,
    viewModel: ReservationViewModel = viewModel()
) {
    val format = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    viewModel.getCourtReservations(court.id, Timestamp(format.parse(reservation.value.toDate())!!))
    Column {
        Row(
            modifier = Modifier.padding(horizontal = 0.dp, vertical = 8.dp)
        ) {
            Text(
                text = "Timeslots available for ${reservation.value.toDate()}",
                style = MaterialTheme.typography.labelMedium,
                color = Color.Gray
            )
        }

//        TextGrid(
//            mutableListOf("08:00", "09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00")
//                .filter { !courtReservations.any{ r -> (r.slot == it) && (r.id != reservationsUiState.reservationDetails.id)} },
//            reservationsUiState,
//            onReservationValueChange)
        if(viewModel.courtres.value.isNotEmpty()){
            TextGrid(mutableListOf("08:00", "09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00")
                .filter { !viewModel.courtres.value.any { r -> (r.toTime() == it) && (r.id != reservation.value.id) } }
                , reservation)
        }
        //TextGrid(mutableListOf("08:00", "09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00"), reservation)
        Row(
            modifier = Modifier
                .padding(horizontal = 0.dp)
        ) {
            var pickerValue by remember { mutableStateOf(1) }
            pickerValue = reservation.value.people

            Text(
                text = "Number of people: $pickerValue",
                style = MaterialTheme.typography.labelMedium,
                color = Color.Gray,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(4f)
                    .align(Alignment.CenterVertically)
            )
            if(court.id != "")
                NumberPicker(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .align(Alignment.CenterVertically),
                    value = pickerValue,
                    range = 1..court.capacity,
                    onValueChange = {
                        pickerValue = it
                        reservation.value = reservation.value.copy(people = it)
                    },
                    dividersColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f),
                )
        }
    }
    Column(modifier = Modifier.padding(top = 0.dp)) {
        Text(
            text = "Notes",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 0.dp)
        )

        TextField(
            value = reservation.value.notes,
            onValueChange = { reservation.value = reservation.value.copy(notes = it) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 0.dp, vertical = 8.dp)
                .background(MaterialTheme.colorScheme.surface),
            textStyle = MaterialTheme.typography.bodyMedium,
            placeholder = { Text(text = "Additional notes here") },
            maxLines = 3,
            singleLine = false,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                disabledContainerColor = MaterialTheme.colorScheme.surface,
                focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                unfocusedIndicatorColor = Color.Gray,
            ),
        )
    }
}


@Composable
fun TextGrid(
    textList: List<String>,
    reservation: MutableState<Reservation>
) {
    Log.d("EDIT", textList.toString())
    val rows = (textList.size + 4) / 5 //Calculate the number of rows necessary
    val selectedButtonIndex = remember { mutableStateOf(1) }
    selectedButtonIndex.value = textList.indexOfFirst { reservation.value.toTime() == it }
    Column {
        repeat(rows) { rowIndex ->
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 0.dp)) {
                for (columnIndex in 0 until 5) {
                    val index = rowIndex * 5 + columnIndex
                    if (index < textList.size) {
                        val isSelected = index == selectedButtonIndex.value

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(50.dp)
                                .padding(4.dp)
                                .clip(
                                    if (isSelected) MaterialTheme.shapes.small else MaterialTheme.shapes.small
                                )
                                .background(
                                    if (isSelected) MaterialTheme.colorScheme.primary.copy(
                                        alpha = 0.7f
                                    ) else Color.Transparent
                                )
                                .border(
                                    BorderStroke(1.dp, if (isSelected) Color.Black else Color.Gray),
                                    shape = MaterialTheme.shapes.small
                                )
                                .clickable {
                                    selectedButtonIndex.value = index
                                    val oldDate = reservation.value.toDate()
                                    val newDate = oldDate + " " + textList[selectedButtonIndex.value]
                                    val format = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
                                    reservation.value = reservation.value.copy(date = Timestamp(format.parse(newDate)!!))
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = textList[index],
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isSelected) Color.Black else Color.Black
                                ),
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                            )
                        }
                    } else {
                        Spacer(
                            modifier = Modifier
                                .weight(1f)
                                .aspectRatio(1f)
                                .padding(2.dp)
                        )
                    }
                }
            }
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
        title = { Text("Warning") },
        text = { Text("Do you want to delete your reservation?") },
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
