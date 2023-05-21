package com.example.courtreservationapplicationjetpack.views.courts

import android.annotation.SuppressLint
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.courtreservationapplicationjetpack.CourtTopAppBar
import com.example.courtreservationapplicationjetpack.components.BottomBar
import com.example.courtreservationapplicationjetpack.navigation.NavigationDestination
import com.example.courtreservationapplicationjetpack.views.reservations.ReservationDetailsDestination
import com.example.courtreservationapplicationjetpack.ui.appViewModel.AppViewModelProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.chargemap.compose.numberpicker.NumberPicker
import com.example.courtreservationapplicationjetpack.R
import com.example.courtreservationapplicationjetpack.models.courts.Court
import com.maxkeppeker.sheets.core.models.base.UseCaseState
import com.maxkeppeker.sheets.core.models.base.rememberUseCaseState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

object CourtsAvailableDestination : NavigationDestination {
    override val route  = "my_courts"
    override val titleRes = "My Courts"
    override val icon = Icons.Default.Star
    private const val courtID = "courtID"
    val routeWithArgs = "$route/{$courtID}"
}



@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@ExperimentalMaterial3Api
@Composable
fun CourtsAvailable(
    courtID: String,
    navController: NavController,
    modifier: Modifier = Modifier,
    navigateToCourtReservation: (Int) -> Unit,

    viewModel: CourtsAvailableViewModel = viewModel(factory = AppViewModelProvider.Factory)


) {
    val courtsAvailableUiState by viewModel.courtsAvailableUiState.collectAsState()
    Scaffold(
        topBar = {
            //CourtTopAppBar(canNavigateBack = false)
        },
        //bottomBar = { BottomBar(navController = navController as NavHostController) }
        bottomBar = {
            Row(modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
                .padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Button(
                    onClick = {},
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp)
                ) {
                    Text(text = "Save")
                }
            }

        }

    ) {
        _ ->
        Ciao(courtID = courtID, viewModel = viewModel)
//        CourtsBody(
//            courtList = courtsAvailableUiState.courtsAvailableList,
//            modifier = modifier.padding(innerPadding),
//            onCourtClick = navigateToCourtReservation,
//        )
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

//--------------------------------------------------------------------------------
@Composable
fun Ciao(courtID: String, viewModel: CourtsAvailableViewModel) {
    val courtState = remember { mutableStateOf<Court?>(null) }

    LaunchedEffect(Unit) {
        viewModel.getCourt(courtID.toInt()).collect { courtValue ->
            courtState.value = courtValue
        }
    }

    val court = courtState.value

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
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .clip(
                RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp)
            ),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        state = lazyListState
    ) {
        item {
            Box(modifier = Modifier.fillMaxWidth()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .background(Color.Red)
                        .align(Alignment.TopCenter)
                )
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data("https://www.parrocchiecurtatone.it/wp-content/uploads/2020/07/WhatsApp-Image-2020-07-23-at-17.53.36-1984x1200.jpeg")
                        .crossfade(true)
                        .build(),
                    placeholder = painterResource(R.drawable.placeholder),
                    contentDescription = "Court Image",
                    contentScale = ContentScale.Crop,
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
                        .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
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
                                text = "${court?.name}",
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "${court?.address}}",
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
                            Image(
                                painter = painterResource(R.drawable.ic_calcio5),
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
                            .shadow(elevation = 4.dp, shape = MaterialTheme.shapes.medium)
                    )

                    CalendarScreen()
                }
            }
        }
    }
}
@Composable
fun CalendarScreen() {
    val scrollState = rememberScrollState()
    val startDate = LocalDate.now()
    val selectedDate = remember { mutableStateOf(LocalDate.now()) }

    Column {
        Row(
            modifier = Modifier
                .horizontalScroll(scrollState)
                .padding(horizontal = 0.dp, vertical = 8.dp)
        ) {
            val daysToShow = 100
            repeat(daysToShow) { index ->
                val currentDate = startDate.plusDays(index.toLong())
                val dayOfWeek =
                    currentDate.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault())
                val dayOfMonth = currentDate.dayOfMonth
                val month = currentDate.month.getDisplayName(TextStyle.SHORT, Locale.getDefault())

                DayButton(
                    dayOfWeek = dayOfWeek,
                    dayOfMonth = dayOfMonth,
                    month = month,
                    isSelected = selectedDate.value == currentDate,
                    onDaySelected = { selectedDate.value = currentDate }
                )
            }
        }
        Row(
            modifier = Modifier.padding(horizontal = 0.dp, vertical = 8.dp)
        ) {
            Text(
                text = "Timeslot disponibili per il giorno ${
                    selectedDate.value.format(
                        DateTimeFormatter.ofPattern("dd/MM/yyyy")
                    )
                }",
                style = MaterialTheme.typography.labelMedium,
                color = Color.Gray
            )
        }

        TextGrid(listOf("10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00"))
        Row(
            modifier = Modifier
                .padding(horizontal = 0.dp, vertical = 8.dp)
            //.padding(8.dp)
        ) {
            var pickerValue by remember { mutableStateOf(1) }

            Text(
                text = "Numero di persone coinvolte: $pickerValue",
                style = MaterialTheme.typography.labelMedium,
                color = Color.Gray,
                modifier = Modifier
                    .weight(1f)
                    .align(Alignment.CenterVertically)
            )

            NumberPicker(
                modifier = Modifier
                    .width(120.dp)
                    .align(Alignment.CenterVertically),
                value = pickerValue,
                range = 0..10,
                onValueChange = {
                    pickerValue = it
                },
                dividersColor = Color.Black.copy(alpha = 0.7f),
            )
        }
    }
}


@Composable
fun TextGrid(textList: List<String>) {
    val rows = (textList.size + 4) / 5 // Calcola il numero di righe necessarie per visualizzare tutti gli elementi
    val selectedButtonIndex = remember { mutableStateOf(-1) }

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
                                .background(if (isSelected) Color.Black else Color.Transparent)
                                .border(
                                    BorderStroke(1.dp, if (isSelected) Color.Black else Color.Gray),
                                    shape = MaterialTheme.shapes.small
                                )
                                .clickable { selectedButtonIndex.value = index },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = textList[index],
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isSelected) Color.White else Color.Black
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
fun DayButton(
    dayOfWeek: String,
    dayOfMonth: Int,
    month: String,
    isSelected: Boolean,
    onDaySelected: () -> Unit
) {
    val textColor = animateTextColor(isSelected)
    val circleColor = animateCircleColor(isSelected)

    Column(
        modifier = Modifier
            .padding(horizontal = 2.dp)
            .height(90.dp)
            .width(50.dp)
            .background(Color.Transparent, RoundedCornerShape(8.dp))
            .clickable(onClick = onDaySelected),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = dayOfWeek.uppercase(Locale.getDefault()),
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold,
            color = circleColor
        )
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(if (isSelected) Color.Black else Color.Transparent, shape = CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = dayOfMonth.toString(),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = textColor
            )
        }
        Text(
            text = month,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold,
            color = circleColor
        )
    }
}
@Composable
private fun animateTextColor(isSelected: Boolean): Color {
    return animateColorAsState(
        targetValue = if (isSelected) Color.White else Color.Gray,
        animationSpec = tween(durationMillis = 200) // Specify the desired duration
    ).value
}

@Composable
private fun animateCircleColor(isSelected: Boolean): Color {
    return animateColorAsState(
        targetValue = if (isSelected) Color.Black else Color.Gray,
        animationSpec = tween(durationMillis = 200) // Specify the desired duration
    ).value
}
//--------------------------------------------------------------------------------