package com.example.courtreservationapplicationjetpack.views.courts

import OptionSample3
import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
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
import kotlinx.coroutines.delay
import java.time.LocalDate
import java.time.format.DateTimeFormatter

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
    PrenotaCampo(sportsList = allSportsUiState.sportsList, courtsViewModel = courtsViewModel, viewModel = viewModel, navController = navController)
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrenotaCampo(sportsList: List<String>, courtsViewModel: CourtsAvailableViewModel, viewModel: AllSportsViewModel, navController: NavController) {
    var pickedDate = remember { mutableStateOf(LocalDate.now()) }

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
        selection = CalendarSelection.Date(
            selectedDate = pickedDate.value
        ) { date ->
            pickedDate.value = date
        }
    )


    Column() {
        Row() {
            TopAppBar(
                title = {
                    Text(
                        text = "Prenotazioni",
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
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
                                        .clickable { navController.navigate("${CourtsAvailableDestination.route}/${it.id}/${pickedDate.value}") }
                                        .height(100.dp),
                                    sport = it.sport
                                )
                                Box(
                                    modifier = Modifier
                                        .align(Alignment.TopEnd)
                                        .padding(16.dp)
                                ) {
                                    RatingBar(5, onRatingChanged = { /* Aggiungi la logica per gestire il cambio di rating */ })
                                }
                                Text(
                                    text = it.name,
                                    style = MaterialTheme.typography.titleMedium.copy(color = Color.White),
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
                                        text = "Orari disponibili per il giorno selezionato: ${pickedDate.value.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))}",
                                        fontSize = 14.sp,
                                        color = Color.Gray,
                                        modifier = Modifier
                                            .padding(16.dp)
                                    )


                                    val slotRiservato = viewModel.getSlot(pickedDate.value.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")), it.id).collectAsState(
                                        initial = emptyList<String>()
                                    )
                                    
                                    HourButtons(reservatedSlot = slotRiservato.value, navigateToCourtsAvailable = { TODO() }, navController = navController, courtID = it.id.toString(), date = pickedDate.value)

                                }
                            }
                        }
                    }
                }
            }


        }
    }
}
@Composable
fun RatingBar(maxRating: Int = 5, onRatingChanged: (Int) -> Unit) {
    var rating by remember { mutableStateOf(0) }
    var touchPosition by remember { mutableStateOf(0f) }

    Row(Modifier.pointerInput(Unit) {
        detectTapGestures { tapOffset ->
            touchPosition = tapOffset.x
            val starWidth = this@pointerInput.size.width / maxRating
            val newRating = kotlin.math.ceil(touchPosition / starWidth).toInt()
            rating = newRating
            onRatingChanged(newRating)
        }
    }) {
        repeat(maxRating) { index ->
            val starColor = if (index < rating) Color(0xFFE0A800) else Color.Gray
            Image(
                //painter = painterResource(id = R.drawable.ic_star),
                imageVector = Icons.Filled.Star,
                contentDescription = null,
                modifier = Modifier
                    .size(28.dp)
                    .clickable { rating = index + 1; onRatingChanged(index + 1) }
                    .let { if (index < rating) it.alpha(1f) else it.alpha(0.5f) },
                colorFilter = ColorFilter.tint(starColor)
            )
        }
    }
}







@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HourButton(hour: String,navController: NavController, navigateToCourtsAvailable: (String) -> Unit, courtID: String, date: LocalDate) {
    Box(
        modifier = Modifier
            .clickable {navController.navigate("${CourtsAvailableDestination.route}/${courtID}/${date.toString()}?hourOptArg=$hour") }
            .padding(bottom = 8.dp)
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
fun HourButtons(courtID: String, date: LocalDate, reservatedSlot: List<String>,navController: NavController, navigateToCourtsAvailable: (String) -> Unit) {
    val hours = listOf(
        "8:00",
        "9:00",
        "10:00",
        "11:00",
        "12:00",
        "13:00",
        "14:00"
    ) - reservatedSlot.toSet()
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
    ) {
        items(hours.size) { index ->
            HourButton(hour = hours[index],navController, navigateToCourtsAvailable = navigateToCourtsAvailable, courtID = courtID, date = date)
        }
    }
}

@Composable
fun CoilImage(modifier: Modifier = Modifier, sport: String) {
    val imageUrl = when (sport) {
        "calcio" -> "https://www.parrocchiecurtatone.it/wp-content/uploads/2020/07/WhatsApp-Image-2020-07-23-at-17.53.36-1984x1200.jpeg"
        "basket" -> "https://images.unsplash.com/photo-1467809941367-bbf259d44dd6?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1000&q=80"
        "tennis" -> "https://images.unsplash.com/photo-1627246939899-23f10c79192c?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1170&q=80"
        "pallavolo" -> "https://th.bing.com/th/id/R.9f02eacbdc53b2217aa8c260cbb5e082?rik=FY%2bHCt5RL6u%2bKQ&riu=http%3a%2f%2fwww.adamsport.it%2fwp-content%2fuploads%2f2015%2f02%2fpallavolo1.jpg&ehk=kYTqz5c7R7TDVrHOW%2frwqBEQ3iadw%2bsTrr99esCAj10%3d&risl=&pid=ImgRaw&r=0"
        "pallamano" -> "https://static.lvengine.net/reconquista/thumb/&w=500&h=300&zc=1&q=95&src=/Imgs/articles/article_59133/IMG_9969_f5.jpg"
        "rugby" -> "https://th.bing.com/th/id/OIP.IWZf734cG-wtATrKOojZVQHaEK?pid=ImgDet&rs=1"
        "softball" -> "https://th.bing.com/th/id/OIP.Md4VheCpdtIgWg5F5UuiiQHaEh?pid=ImgDet&rs=1"
        "beach volley" -> "https://images.unsplash.com/photo-1612872087720-bb876e2e67d1?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=1307&q=80"
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





@Preview(showBackground = true)
@Composable
fun CourtPreview(){

}


