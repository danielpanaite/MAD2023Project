package com.example.courtreservationapplicationjetpack.views.courts

import OptionSample3
import android.annotation.SuppressLint
import android.media.Image
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material3.Surface
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role.Companion.Image
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.courtreservationapplicationjetpack.R
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.maxkeppeker.sheets.core.models.base.rememberUseCaseState
import com.maxkeppeler.sheets.calendar.CalendarDialog
import com.maxkeppeler.sheets.calendar.models.CalendarConfig
import com.maxkeppeler.sheets.calendar.models.CalendarSelection
import com.maxkeppeler.sheets.calendar.models.CalendarStyle
import java.time.LocalDate

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
    val courtsAvailableUiState by courtsViewModel.courtsAvailableUiState.collectAsState()

    // Imposta lo sport su "tennis"
    courtsViewModel.setSport("tennis")

    // Stampa il risultato della query per lo sport "tennis"
    println(courtsAvailableUiState.courtsAvailableList)

    Scaffold(
        topBar = {
            // CourtTopAppBar(canNavigateBack = false)
        },
        bottomBar = { BottomBar(navController = navController as NavHostController) }
    ) {
        PrenotaCampo(sportsList = allSportsUiState.sportsList)
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrenotaCampo(sportsList: List<String>) {
    val (pickedDate, setPickedDate) = remember { mutableStateOf(LocalDate.now()) }
    val calendarState = rememberUseCaseState()
    val disabledDates = remember {
        val today = LocalDate.now()
        val startDate = LocalDate.of(1980, 1, 1)
        generateSequence(today.minusDays(1)) { it.minusDays(1) }
            .takeWhile { it >= startDate }
            .toMutableList()
    }
    val now = LocalDate.now() // ottiene la data odierna
    val future = now.plusYears(5) // aggiunge 5 anni alla data odierna

    val dateRange = now..future //creo closedRange per boundary
    val optionState = rememberUseCaseState()

    OptionSample3(sportList = sportsList, optionState = optionState){
        Log.d("Test", "SportPressed")
    }
    CalendarDialog(
        state = calendarState,
        config = CalendarConfig(
            monthSelection = true,
            yearSelection = true,
            style = CalendarStyle.MONTH,
            disabledDates = disabledDates,
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
                                text = "Calcio a 5",
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
                LazyColumn(content = {
                    items(10){_ ->
                        Box(modifier = Modifier.aspectRatio(1.5f)) {
                            CoilImage(
                                modifier = Modifier
                                    .shadow(10.dp, RoundedCornerShape(0.dp))
                                    .fillMaxSize()
                                    .height(100.dp))
                            Text(
                                text = "Campo da calcio - Torino",
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
                                    text = "433m - Torino (cittá metropolitana di Torino)",
                                    fontSize = 14.sp,
                                    color = Color.Gray,
                                    modifier = Modifier.padding(16.dp)
                                )
                                val hours = listOf("8:00", "9:00", "10:00", "11:00", "12:00", "13:00", "14:00")
                                HourButtons(hours = hours)
                                Box(
                                    modifier = Modifier
                                        .size(16.dp)
                                        .background(Color.White)
                                        .shadow(10.dp, shape = RoundedCornerShape(8.dp))
                                ) {}


                            }
                        }

                    }
                })
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
fun CoilImage(modifier: Modifier = Modifier){
    Box(modifier = modifier
    ){
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data("https://www.parrocchiecurtatone.it/wp-content/uploads/2020/07/WhatsApp-Image-2020-07-23-at-17.53.36-1984x1200.jpeg")
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
