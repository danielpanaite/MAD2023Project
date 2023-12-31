package com.example.courtreservationapplicationjetpack.views.courts

import OptionSample3
import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
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
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
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
import androidx.compose.ui.graphics.vector.rememberVectorPainter
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
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.courtreservationapplicationjetpack.R
import com.example.courtreservationapplicationjetpack.components.BottomBar
import com.example.courtreservationapplicationjetpack.firestore.CityViewModel
import com.example.courtreservationapplicationjetpack.firestore.Court
import com.example.courtreservationapplicationjetpack.firestore.CourtViewModel
import com.example.courtreservationapplicationjetpack.firestore.Notification
import com.example.courtreservationapplicationjetpack.firestore.NotificationViewModel
import com.example.courtreservationapplicationjetpack.firestore.Reservation
import com.example.courtreservationapplicationjetpack.firestore.ReservationViewModel
import com.example.courtreservationapplicationjetpack.firestore.ReviewViewModel
import com.example.courtreservationapplicationjetpack.models.reviews.Review
import com.example.courtreservationapplicationjetpack.models.sport.SportDrawables
import com.example.courtreservationapplicationjetpack.navigation.NavigationDestination
import com.example.courtreservationapplicationjetpack.ui.appViewModel.AppViewModelProvider
import com.example.courtreservationapplicationjetpack.ui.theme.Orange200
import com.example.courtreservationapplicationjetpack.views.reviews.CourtReviewPageDestination
import com.example.courtreservationapplicationjetpack.views.reviews.ReviewCreatePageDestination
//import com.example.courtreservationapplicationjetpack.views.reviews.ReviewViewModel
import com.google.firebase.Timestamp
import com.gowtham.ratingbar.RatingBarConfig
import com.gowtham.ratingbar.RatingBarStyle
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
    //reviewViewModel: ReviewViewModel = viewModel(factory = AppViewModelProvider.Factory),

    reviewNewViewModel: ReviewViewModel = viewModel()

) {
    val allSportsUiState by viewModel.allSportsUiState.collectAsState()
    //val userReviews by reviewViewModel.myReviewsUiState.collectAsState()


    Scaffold(
        topBar = {
            // CourtTopAppBar(canNavigateBack = false)
        },
        bottomBar = { BottomBar(navController = navController as NavHostController) }
    ) {
    PrenotaCampo(
        sportsList = allSportsUiState.sportsList,
        courtsViewModel = courtsViewModel,
        viewModel = viewModel,
        navController = navController,
        //userReviews = userReviews.reviewList,
        reviewViewModel = reviewNewViewModel
    )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrenotaCampo(sportsList: List<String>, courtsViewModel: CourtsAvailableViewModel, viewModel: AllSportsViewModel, navController: NavController,  reviewViewModel: ReviewViewModel) {

    var pickedDate = remember { mutableStateOf(LocalDate.now()) }

    var pickedSport = remember { mutableStateOf("tennis") }
    val calendarState = rememberUseCaseState()
    val now = LocalDate.now() // ottiene la data odierna
    val future = now.plusYears(5) // aggiunge 5 anni alla data odierna

    val dateRange = now..future //creo closedRange per boundary
    val optionState = rememberUseCaseState()

    //questo è il viewmodel firebase
    val firebaseCourtViewModel: CourtViewModel = viewModel()
    val firebaseNotificationViewModel: NotificationViewModel = viewModel()
    val firebaseCityViewModel: CityViewModel = viewModel()

    firebaseCityViewModel.getCities()
    val test = firebaseCityViewModel.cities.value.map { it.city }
    val cityList = test.toList()
    //val cityList = listOf<String>("Torino", "Milano", "Savigliano", "Cuneo", "Saluzzo", "Rho", "Monza")
    var pickedCity = remember { mutableStateOf("Torino") }

    Log.d("CITY", "CITY: $cityList")
    LaunchedEffect(pickedSport.value) {
        firebaseCourtViewModel.getCourtsBySport(pickedSport.value, pickedCity.value)
    }

    val courtList by remember { firebaseCourtViewModel.courts }



        OptionSample3(
            sportList = sportsList,
            optionState = optionState,
            pickedSport = pickedSport,
            firebaseCourtViewModel = firebaseCourtViewModel,
            pickedCity = pickedCity,
        )




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



    Column {
        Row() {
            TopAppBar(
                title = {
                    Text(
                        text = "Reserve a court",
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                },
                backgroundColor = Color.White,
                elevation = 150.dp
            )
        }

        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = Color.White)
            ) {

                CityPicker(modifier = Modifier.padding(horizontal = 16.dp), cityList = cityList, pickedCity = pickedCity){
                    firebaseCourtViewModel.getCourtsBySport(pickedSport.value, pickedCity.value)
                }
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

        if (courtList.isEmpty()) {
            Box(
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = painterResource(R.drawable.nothing_found),
                        contentDescription = "Nothing Found",
                        contentScale = ContentScale.FillWidth,
                        modifier = Modifier
                            .fillMaxWidth()
                            //.aspectRatio(1f)
                            .padding(horizontal = 16.dp)
                            .padding(top = 16.dp)
                            .background(color = Color.Transparent)
                    )
                    Text(
                        text = "We are sorry, but we could not find the field you were looking for. Be sure you have entered the name of the city where you want to search...",
                        color = Color.Gray,
                        modifier = Modifier.padding(horizontal = 16.dp),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Color.Transparent)
                .padding(bottom = 50.dp)
        ) {

            LazyColumn {
                itemsIndexed(courtList) { index, court ->
                        CourtCard(
                            pickedDate = pickedDate,
                            pickedSport = pickedSport,
                            court = court,
                            navController = navController,
                            reviewViewModel = reviewViewModel,
                            key = index.toString() + court.toString() // Utilizza una combinazione di indici e valori di "court" come chiave
                        )
                    if(index == courtList.size - 1){
                        Spacer(modifier = Modifier.height(40.dp))
                    }
                }
            }
        }
    }
}


@Composable
fun CourtCard(pickedDate: MutableState<LocalDate>, pickedSport: MutableState<String>, court: Court, navController: NavController, key: String,
              reviewViewModel: ReviewViewModel) {
    println(key)
    val firebaseReservationViewModel: ReservationViewModel = viewModel()
    val dateInTimeZone = pickedDate.value.atStartOfDay(ZoneId.of("Europe/Rome")).toInstant().epochSecond
    val slots = remember { mutableStateOf(firebaseReservationViewModel.getCourtReservations2(court.id, Timestamp(dateInTimeZone, 0))) }

    //reviewViewModel.getAverageRatingForCourt(courtId = court.id)
    //val reviewCourtAvg by reviewViewModel.avg.collectAsState()
    //Log.d("reviewCourt", "$reviewCourtAvg")

    var launchOnce by rememberSaveable { mutableStateOf(true) }
    if(launchOnce){
        reviewViewModel.getAverageRatingForCourt()
        launchOnce = false
    }

    //LaunchedEffect(Unit) {
     //   reviewViewModel.getAverageRatingForCourt()
    //}
    val averageRatingMap = remember { mutableStateOf(reviewViewModel.averageRatingMap) }




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



    Row(
        modifier = Modifier.padding(start = 0.dp, bottom = 0.dp)
    ) {
        Box(
            modifier = Modifier.aspectRatio(1.5f)
        ) {
            CoilImage(
                modifier = Modifier
                    .shadow(10.dp, RoundedCornerShape(0.dp))
                    .fillMaxSize()
                    .clickable { navController.navigate("${CourtsAvailableDestination.route}/${court.id}/${pickedDate.value}") }
                    .height(100.dp),
                sport = court.sport,
                URL = court.URL ?: null
            )
            Row(
                modifier = Modifier.align(Alignment.BottomStart)
            ) {
                Text(
                    text = court.name,
                    style = MaterialTheme.typography.titleMedium.copy(color = Color.White),
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 26.sp,
                    textAlign = TextAlign.Start,
                    modifier = Modifier
                        .weight(3f)
                        .padding(16.dp)
                )
                Text(
                    //€
                    text = "${court.prezzo}€",
                    style = MaterialTheme.typography.titleMedium.copy(color = Color.White),
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 22.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .weight(1f)
                        .padding(vertical = 16.dp, horizontal = 16.dp)
                        .background(Color.Gray.copy(alpha = 0.2f), RoundedCornerShape(10.dp))
                        .align(Alignment.CenterVertically)
                        //.fillMaxHeight()
                )
            }


            Row(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${court.capacity}",
                    fontSize = 18.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
                Icon(
                    painter = rememberVectorPainter(Icons.Default.Person),
                    contentDescription = "reservation available",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }
            Row(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ){
                com.gowtham.ratingbar.RatingBar(
                    value = averageRatingMap.value[court.id] ?: 0f,
                    config = RatingBarConfig()
                        .style(RatingBarStyle.Normal)
                        .size(18.dp)
                        .inactiveColor(Color.Black)
                        .activeColor(Orange200)
                        .hideInactiveStars(false),
                    onValueChange = {},
                    onRatingChanged = {},
                    modifier = Modifier
                        .background(Color.Gray.copy(alpha = 0.7f), RoundedCornerShape(10.dp))

                )
                Icon(
                    painter = rememberVectorPainter(Icons.Default.ArrowForward),
                    contentDescription = "see reviews",
                    tint = Color.White,
                    modifier = Modifier
                        .size(24.dp)
                        .clickable {
                            navController.navigate("${CourtReviewPageDestination.route}/${court.id}")
                        },
                )
            }
        }
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
//            Text(
//                text = "${court.center}",
//                fontSize = 14.sp,
//                color = Color.Gray,
//                modifier = Modifier.padding(start = 16.dp)
//            )
            Text(
                text = "City of ${court.citta}",
                fontSize = 14.sp,
                color = Color.Gray,
                fontStyle = FontStyle.Italic,
                modifier = Modifier.padding(start = 16.dp),
                style = androidx.compose.material.MaterialTheme.typography.body1
            )
//            Row(
//                modifier = Modifier.padding(start = 16.dp, bottom = 32.dp)
//            ) {
//                Text(
//                    text = "${court.capacity}",
//                    fontSize = 14.sp,
//                    color = Color.Gray
//                )
//                Icon(
//                    painter = rememberVectorPainter(Icons.Default.Person),
//                    contentDescription = "reservation available"
//                )
//            }


        }
    }
    Spacer(modifier = Modifier.height(16.dp))
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
fun CityPicker(modifier: Modifier, cityList: List<String>, pickedCity:  MutableState<String>, onFinish: (Unit) -> Unit) {

    var isMenuExpanded by remember { mutableStateOf(false) }

    Box(modifier = modifier) {
        OutlinedButton(
            onClick = { isMenuExpanded = true },
            modifier = Modifier.fillMaxWidth(),
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
                Text(
                    text = if (pickedCity.value.isEmpty()) "Choose the city" else pickedCity.value,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Start,
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 8.dp),
                    color = if (pickedCity.value.isEmpty()) Color.Gray else Color.Black
                )
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        DropdownMenu(
            expanded = isMenuExpanded,
            onDismissRequest = { isMenuExpanded = false },
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .background(Color.White)
                .padding(vertical = 4.dp)
                .align(Alignment.Center)
        ) {
            cityList.forEach { city ->
                androidx.compose.material3.DropdownMenuItem(
                    modifier = Modifier.fillMaxWidth(),
                    text = { Text(city, color = Color.Black) },
                    onClick = {
                        pickedCity.value = city
                        onFinish(Unit)
                        isMenuExpanded = false
                    }
                )
            }
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


