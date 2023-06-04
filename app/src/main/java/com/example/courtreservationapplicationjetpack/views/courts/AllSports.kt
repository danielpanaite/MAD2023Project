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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.courtreservationapplicationjetpack.R
import com.example.courtreservationapplicationjetpack.components.BottomBar
import com.example.courtreservationapplicationjetpack.firestore.Court
import com.example.courtreservationapplicationjetpack.firestore.CourtViewModel
import com.example.courtreservationapplicationjetpack.firestore.Reservation
import com.example.courtreservationapplicationjetpack.firestore.ReservationViewModel
import com.example.courtreservationapplicationjetpack.models.reviews.Review
import com.example.courtreservationapplicationjetpack.models.sport.SportDrawables
import com.example.courtreservationapplicationjetpack.navigation.NavigationDestination
import com.example.courtreservationapplicationjetpack.ui.appViewModel.AppViewModelProvider
import com.example.courtreservationapplicationjetpack.views.reviews.ReviewViewModel
import com.google.firebase.Timestamp
import com.maxkeppeker.sheets.core.models.base.rememberUseCaseState
import com.maxkeppeler.sheets.calendar.CalendarDialog
import com.maxkeppeler.sheets.calendar.models.CalendarConfig
import com.maxkeppeler.sheets.calendar.models.CalendarSelection
import com.maxkeppeler.sheets.calendar.models.CalendarStyle
import kotlinx.coroutines.delay
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
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
    //cercare di riprendere uesta stringa per prendere tutti i campi di quello sport
    onNavigateUp: () -> Unit,

    viewModel: AllSportsViewModel = viewModel(factory = AppViewModelProvider.Factory),
    courtsViewModel: CourtsAvailableViewModel = viewModel(factory = AppViewModelProvider.Factory),
    reviewViewModel: ReviewViewModel = viewModel(factory = AppViewModelProvider.Factory),

) {
    val allSportsUiState by viewModel.allSportsUiState.collectAsState()
    val userReviews by reviewViewModel.myReviewsUiState.collectAsState()


    Scaffold(
        topBar = {
            // CourtTopAppBar(canNavigateBack = false)
        },
        bottomBar = { BottomBar(navController = navController as NavHostController) }
    ) {
    PrenotaCampo(sportsList = allSportsUiState.sportsList, courtsViewModel = courtsViewModel, viewModel = viewModel, navController = navController, userReviews = userReviews.reviewList)
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrenotaCampo(sportsList: List<String>, courtsViewModel: CourtsAvailableViewModel, viewModel: AllSportsViewModel, navController: NavController, userReviews: List<Review>) {

    var pickedDate = remember { mutableStateOf(LocalDate.now()) }

    var pickedSport = remember { mutableStateOf("tennis") }
    val calendarState = rememberUseCaseState()
    val now = LocalDate.now() // ottiene la data odierna
    val future = now.plusYears(5) // aggiunge 5 anni alla data odierna

    val dateRange = now..future //creo closedRange per boundary
    val optionState = rememberUseCaseState()

    //questo è il viewmodel firebase
    val firebaseCourtViewModel: CourtViewModel = viewModel()

    LaunchedEffect(pickedSport.value) {
        firebaseCourtViewModel.getCourtsBySport(pickedSport.value)
    }

    val courtList by remember { firebaseCourtViewModel.courts }





    OptionSample3(sportList = sportsList, optionState = optionState, pickedSport = pickedSport, firebaseCourtViewModel = firebaseCourtViewModel)




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
                        text = "Reserve a court",
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {

                            }
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
                        onClick = { optionState.show() },
                        modifier = Modifier.weight(2f),
                        border = BorderStroke(1.dp, Color.Black),
                        shape = RoundedCornerShape(25),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = Color.Black,
                            containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                        )
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Image(
                                painter = painterResource(SportDrawables.getDrawable(pickedSport.value)),
                                contentDescription = "Sport icon",
                                colorFilter = ColorFilter.tint(Color.Black),
                                modifier = Modifier.size(24.dp)
                            )
                            Text(
                                text = pickedSport.value.replaceFirstChar {
                                    if (it.isLowerCase()) it.titlecase(
                                        Locale.getDefault()
                                    ) else it.toString()
                                },
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
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = Color.Black,
                            containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                        )
                    ) {
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


            LazyColumn {
                itemsIndexed(courtList) { index, court ->
                    CourtCard(
                        pickedDate = pickedDate,
                        pickedSport = pickedSport,
                        court = court,
                        navController = navController,
                        key = index.toString() + court.toString() // Utilizza una combinazione di indici e valori di "court" come chiave
                    )
                }
            }

        }
    }
}
@Composable
fun CourtCard(pickedDate: MutableState<LocalDate>, pickedSport: MutableState<String>, court: Court, navController: NavController, key: String) {
    println(key)
    val firebaseReservationViewModel: ReservationViewModel = viewModel()
    val dateInTimeZone = pickedDate.value.atStartOfDay(ZoneId.of("Europe/Rome")).toInstant().epochSecond
    val slots = remember { mutableStateOf(firebaseReservationViewModel.getCourtReservations2(court.id, Timestamp(dateInTimeZone, 0))) }



//    LaunchedEffect(court){
//        while (true) {
//            delay(1000)
//
//            val reservationList: List<Reservation> = slots.value?.value ?: emptyList()
//
//            val timeFormat = DateTimeFormatter.ofPattern("HH:mm")
//
//            val timeStringList: List<String> = reservationList.map { reservation ->
//                val localDateTime = LocalDateTime.ofInstant(reservation.date.toDate().toInstant(), ZoneId.systemDefault())
//                localDateTime.format(timeFormat)
//            }
//            println(court.name)
//            println("EEEEEEEEEEEEE" + court.name + slots.value.value)
//            println(timeStringList)
//        }
//    }



    Box(modifier = Modifier.aspectRatio(1.5f)) {
        println(court.name + " " + court.sport + " " + (court.URL ?: "NONE"))
        CoilImage(
            modifier = Modifier
                .shadow(10.dp, RoundedCornerShape(0.dp))
                .fillMaxSize()
                .clickable { navController.navigate("${CourtsAvailableDestination.route}/${court.id}/${pickedDate.value}") }
                .height(100.dp),
            sport = court.sport,
            URL = court.URL ?: null,
        )
        Text(
            text = court.name,
            style = MaterialTheme.typography.titleMedium.copy(color = Color.White),
            fontWeight = FontWeight.ExtraBold,
            fontSize = 26.sp,
            textAlign = TextAlign.Start,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(16.dp)
        )
    }

    Box(modifier = Modifier
        .fillMaxWidth()
        .padding(0.dp)
        .clickable { navController.navigate("${CourtsAvailableDestination.route}/${court.id}/${pickedDate.value}") }
    )
    {
        Column {

            Text(
                text = "Press to reserve ${court.name} on ${pickedDate.value.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))}",
                fontSize = 14.sp,
                color = Color.Gray,
                fontStyle = FontStyle.Italic,
                modifier = Modifier.padding(16.dp),
                style = androidx.compose.material.MaterialTheme.typography.body1
            )
            Text(
                text = "${court.address}",
                fontSize = 14.sp,
                color = Color.Gray,
                modifier = Modifier.padding(start = 16.dp)
            )
            Text(
                text = "${court.center}",
                fontSize = 14.sp,
                color = Color.Gray,
                modifier = Modifier.padding(start = 16.dp)
            )
            Text(
                text = "${court.capacity} places available",
                fontSize = 14.sp,
                color = Color.Gray,
                modifier = Modifier.padding(start = 16.dp).padding(bottom = 32.dp)
            )
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
            .clickable { navController.navigate("${CourtsAvailableDestination.route}/${courtID}/${date.toString()}?hourOptArg=$hour") }
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
fun HourButtons(courtID: String, date: LocalDate, reservedSlot: List<String>, navController: NavController, navigateToCourtsAvailable: (String) -> Unit, isHoursListEmpty: MutableState<Boolean>) {
    val currentTime = LocalTime.now()
    val currentDate = LocalDate.now()

    val hours = listOf("08:00", "09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00")
        .filter { time ->
            val hour = LocalTime.parse(time)
            date > currentDate || (date == currentDate && hour >= currentTime)
        }
        .toSet() - reservedSlot.toSet()

    isHoursListEmpty.value = hours.isEmpty()

    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
    ) {
        items(hours.size) { index ->
            HourButton(
                hour = hours.toList()[index],
                navController,
                navigateToCourtsAvailable = navigateToCourtsAvailable,
                courtID = courtID,
                date = date
            )
        }
    }
}



@Composable
fun CoilImage(modifier: Modifier = Modifier, sport: String, URL: String? = null) {

    var imageUrl = when (sport) {
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
    if(!URL.isNullOrEmpty()){
        imageUrl = URL
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


