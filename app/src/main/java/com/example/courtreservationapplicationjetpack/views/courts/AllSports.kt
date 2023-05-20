package com.example.courtreservationapplicationjetpack.views.courts

import OptionSample3
import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.animation.animateColor
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Transition
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.shape.ZeroCornerSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.courtreservationapplicationjetpack.components.BottomBar
import com.example.courtreservationapplicationjetpack.navigation.NavigationDestination
import com.example.courtreservationapplicationjetpack.ui.appViewModel.AppViewModelProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.chargemap.compose.numberpicker.NumberPicker
import com.example.courtreservationapplicationjetpack.R
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.maxkeppeker.sheets.core.models.base.rememberUseCaseState
import com.maxkeppeler.sheets.calendar.CalendarDialog
import com.maxkeppeler.sheets.calendar.models.CalendarConfig
import com.maxkeppeler.sheets.calendar.models.CalendarSelection
import com.maxkeppeler.sheets.calendar.models.CalendarStyle
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.runBlocking
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

object AllSportsDestination : NavigationDestination {
    override val route  = "all_sports"
    override val titleRes = "All sports"
    override val icon = Icons.Default.Place

}



@ExperimentalMaterial3Api
@Composable
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
fun AllSports(
    navController: NavController,
    modifier: Modifier = Modifier,
    navigateToCourtsAvailable: (String) -> Unit,
    //cercare di riprendere uesta stringa per prendere tutti i campi di quello sport
    onNavigateUp: () -> Unit,

    viewModel: AllSportsViewModel = viewModel(factory = AppViewModelProvider.Factory),
    courtsViewModel: CourtsAvailableViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val allSportsUiState by viewModel.allSportsUiState.collectAsState()


    Scaffold(
        topBar = {
            // CourtTopAppBar(canNavigateBack = false)
        },
        bottomBar = { BottomBar(navController = navController as NavHostController) }
    ) {
    Ciao()
    //PrenotaCampo(sportsList = allSportsUiState.sportsList, courtsViewModel = courtsViewModel, viewModel = viewModel)
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrenotaCampo(sportsList: List<String>, courtsViewModel: CourtsAvailableViewModel, viewModel: AllSportsViewModel) {
    val (pickedDate, setPickedDate) = remember { mutableStateOf(LocalDate.now()) }
    val (pickedSport, setPickedSport) = remember { mutableStateOf("calcio") }
    val calendarState = rememberUseCaseState()
    val now = LocalDate.now() // ottiene la data odierna
    val future = now.plusYears(5) // aggiunge 5 anni alla data odierna

    val dateRange = now..future //creo closedRange per boundary
    val optionState = rememberUseCaseState()
    val courtsAvailableUiState by courtsViewModel.courtsAvailableUiState.collectAsState()

    OptionSample3(sportList = sportsList, optionState = optionState,pickedSport = pickedSport, setPickedSport = setPickedSport){}
    // Imposta lo sport su pickedSport
    courtsViewModel.setSport(pickedSport)


    CalendarDialog(
        state = calendarState,
        config = CalendarConfig(
            monthSelection = true,
            yearSelection = true,
            style = CalendarStyle.MONTH,
            boundary = dateRange
        ),
        selection = CalendarSelection.Date { date ->
            setPickedDate(date)
        }
    )


    Column() {
        Row() {
            TopAppBar(
                title = {
                    Text(
                        text = "Prenotazioni",
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                backgroundColor = Color.White,
                elevation = 150.dp
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Color.White)
                .padding(horizontal = 16.dp)
        ) {
            Box(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Bottone 1
                    OutlinedButton(
                        onClick = {optionState.show()},
                        modifier = Modifier.weight(2f),
                        border = BorderStroke(1.dp, Color.Black),
                        shape = RoundedCornerShape(25),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Black, containerColor = Color(
                            0xFFF8F1FF
                        )
                        )) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Image(
                                painter = painterResource(R.drawable.ic_calcio5),
                                contentDescription = "Sport icon",
                                colorFilter = ColorFilter.tint(Color.Black),
                                modifier = Modifier.size(24.dp)
                            )
                            Text(
                                text = pickedSport,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                textAlign = TextAlign.Start,
                                modifier = Modifier
                                    .padding(start = 8.dp)
                                    .weight(1f)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(16.dp))
                    // Bottone 2
                    OutlinedButton(
                        onClick = {
                            calendarState.show()
                        },
                        border = BorderStroke(1.dp, Color.Black),
                        shape = RoundedCornerShape(25), // = 50% percent
                        // or shape = CircleShape
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Black, containerColor = Color(
                            0xFFF8F1FF
                        )
                        )){
                        Icon(
                            imageVector = Icons.Outlined.DateRange,
                            contentDescription = "Settings",
                            tint = Color.Black,
                        )
                    }

                }
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Color.White)
                .drawWithContent {
                    drawContent()
                    drawRect(
                        color = Color.Black.copy(alpha = 0.1f),
                        topLeft = Offset(0f, size.height - 4.dp.toPx()),
                        size = Size(size.width, 10.dp.toPx())
                    )
                }
        ) {
            Spacer(modifier = Modifier.height(10.dp))
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Color.White)
                .padding(bottom = 50.dp)
        ) {
            val viewModel2 = viewModel<AllSportsViewModel>()
            val isLoading by viewModel2.isLoading.collectAsState()
            val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = isLoading)

            SwipeRefresh(
                state = swipeRefreshState,
                onRefresh = viewModel2::loadStuff,)
            {
                LazyColumn {
                    items(courtsAvailableUiState.courtsAvailableList.size) { _ ->
                        courtsAvailableUiState.courtsAvailableList.forEach {
                            Box(modifier = Modifier.aspectRatio(1.5f)) {
                                CoilImage(
                                    modifier = Modifier
                                        .shadow(10.dp, RoundedCornerShape(0.dp))
                                        .fillMaxSize()
                                        .height(100.dp),
                                    sport = it.sport
                                )
                                Text(
                                    text = it.name,
                                    style = androidx.compose.material3.MaterialTheme.typography.titleMedium.copy(
                                        color = Color.White
                                    ),
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 26.sp,
                                    textAlign = TextAlign.Start,
                                    modifier = Modifier
                                        .align(Alignment.BottomStart)
                                        .padding(16.dp)
                                )
                            }
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(0.dp)
                            ) {
                                Column {
                                    Text(
                                        text = "Orari disponibili per il giorno selezionato: ${pickedDate.format(
                                            DateTimeFormatter.ofPattern("dd/MM/yyyy")
                                        )}",
                                        fontSize = 14.sp,
                                        color = Color.Gray,
                                        modifier = Modifier.padding(16.dp)
                                    )
                                    val (slots, setSlots) = remember { mutableStateOf(
                                        listOf(
                                            "8:00",
                                            "9:00",
                                            "10:00",
                                            "11:00",
                                            "12:00",
                                            "13:00",
                                            "14:00"
                                        )
                                    )}

                                    LaunchedEffect(pickedDate) {
                                        val slotFlow = viewModel.getSlot(pickedDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")), it.id)
                                        slotFlow.collectLatest { list ->
                                            setSlots(slots - list.toSet())
                                            println("${it.name} $list")
                                        }
                                    }


                                    HourButtons(hours = slots)

                                }
                            }
                        }
                    }
                }
            }


        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HourButton(hour: String) {
    Box(
        modifier = Modifier
            .clickable {}
            .padding(2.dp)
            .width(60.dp)
            .shadow(7.dp, shape = RoundedCornerShape(8.dp))
            .background(
                color = Color.White,
                shape = RoundedCornerShape(8.dp),
            )
            .border(1.dp, Color.Black, RoundedCornerShape(8.dp))
    ) {
        Text(
            text = hour,
            style = MaterialTheme.typography.labelMedium,
            color = Color.Black,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 8.dp, vertical = 12.dp)
        )
    }
}


@Composable
fun HourButtons(hours: List<String>) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
    ) {
        items(hours.size) { index ->
            HourButton(hour = hours[index])
        }
    }
}

@Composable
fun CoilImage(modifier: Modifier = Modifier, sport: String) {
    val imageUrl = when (sport) {
        "calcio" -> "https://www.parrocchiecurtatone.it/wp-content/uploads/2020/07/WhatsApp-Image-2020-07-23-at-17.53.36-1984x1200.jpeg"
        "basket" -> "https://images.unsplash.com/photo-1467809941367-bbf259d44dd6?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1000&q=80"
        "tennis" -> "https://images.unsplash.com/photo-1627246939899-23f10c79192c?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1170&q=80"
        else -> R.drawable.placeholder // Immagine predefinita per sport sconosciuti
    }

    Box(modifier = modifier) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(imageUrl)
                .crossfade(true)
                .build(),
            placeholder = painterResource(R.drawable.placeholder),
            contentDescription = "Court Image",
            contentScale = ContentScale.Crop,
            modifier = modifier
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        0f to Color.Transparent,
                        0.2f to Color.Transparent,
                        1f to Color.Black
                    )
                )
                .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
        )
    }
}

//--------------------------------------------------------------------------------
@Composable
fun Ciao() {
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
        modifier = Modifier.fillMaxWidth(),
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
                    .background(
                        color = MaterialTheme.colorScheme.surface,
                        shape = MaterialTheme.shapes.large.copy(
                            topStart = CornerSize(10.dp),
                            topEnd = CornerSize(10.dp)
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
                                text = "Centro Sportivo Robilant",
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Piazza Generale, Torino",
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

        item {
            Button(
                onClick = { /*TODO*/ },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp)
            ) {
                Text(text = "Save")
            }
        }

        item {
            OutlinedButton(
                onClick = { },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp)
            ) {
                Text(text = "Delete")
            }
        }
    }
















//    Box(modifier = Modifier.fillMaxSize()) {
//        Box(
//            modifier = Modifier
//                .fillMaxWidth()
//                .height(200.dp)
//                .background(Color.Red)
//                .align(Alignment.TopCenter)
//        )
//        AsyncImage(
//            model = ImageRequest.Builder(LocalContext.current)
//                .data("https://www.parrocchiecurtatone.it/wp-content/uploads/2020/07/WhatsApp-Image-2020-07-23-at-17.53.36-1984x1200.jpeg")
//                .crossfade(true)
//                .build(),
//            placeholder = painterResource(R.drawable.placeholder),
//            contentDescription = "Court Image",
//            contentScale = ContentScale.Crop,
//        )
//        Box(
//            modifier = Modifier
//                .fillMaxSize()
//                .background(
//                    brush = Brush.verticalGradient(
//                        0f to Color.Transparent,
//                        0.0001f to Color.Transparent,
//                        1f to Color.Black
//                    )
//                )
//                .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
//        )
//
//        //QUESTA
//        Box(
//            modifier = Modifier
//                .fillMaxWidth()
//                .fillMaxHeight()
//                .offset(y = (185).dp)
//                .background(MaterialTheme.colorScheme.surface, MaterialTheme.shapes.large)
//        ) {
//            Column {
//                Box {
//                    Row(
//                        verticalAlignment = Alignment.CenterVertically,
//                        horizontalArrangement = Arrangement.SpaceBetween,
//                        modifier = Modifier.padding(16.dp)
//                    ) {
//                        Column(modifier = Modifier.weight(3f)) {
//                            Text(
//                                text = "Centro Sportivo Robilant",
//                                style = MaterialTheme.typography.headlineSmall,
//                                fontWeight = FontWeight.Bold
//                            )
//                            Text(
//                                text = "Piazza Generale, Torino",
//                                style = MaterialTheme.typography.labelSmall,
//                                color = Color.Gray,
//                                textAlign = TextAlign.Start
//                            )
//                        }
//                        Box(
//                            modifier = Modifier
//                                .weight(1f)
//                                .align(Alignment.CenterVertically)
//                        ) {
//                            Image(
//                                painter = painterResource(R.drawable.ic_calcio5),
//                                contentDescription = "Sport icon",
//                                colorFilter = ColorFilter.tint(Color.Black),
//                                modifier = Modifier
//                                    .size(29.dp)
//                                    .align(Alignment.Center)
//                            )
//                        }
//                    }
//                }
//
//                Divider(
//                    color = Color.LightGray,
//                    thickness = 1.dp,
//                    modifier = Modifier
//                        .padding(start = 16.dp, end = 16.dp, top = 8.dp)
//                        .height(1.dp)
//                        .shadow(elevation = 4.dp, shape = MaterialTheme.shapes.medium)
//                )
//                CalendarScreen()
//
//            }
//        }
//    }
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
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .padding(16.dp)
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
fun PeoplePicker(
    selectedValue: Int,
    onValueSelected: (Int) -> Unit
) {
    val numbers = (1..5).toList()

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
            .wrapContentSize(Alignment.Center)
    ) {
        Text(
            text = "Select number of people:",
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            numbers.forEach { number ->
                val isSelected = number == selectedValue
                val color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                val background = if (isSelected) MaterialTheme.colorScheme.inversePrimary else Color.Transparent

                Box(
                    modifier = Modifier
                        .padding(4.dp)
                        .size(48.dp)
                        .clickable { onValueSelected(number) }
                        .background(background, CircleShape)
                        .clip(CircleShape)
                ) {
                    Text(
                        text = number.toString(),
                        style = MaterialTheme.typography.labelMedium.copy(color = color),
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
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



@Preview(showBackground = true)
@Composable
fun CourtPreview(){
    Ciao()
}


