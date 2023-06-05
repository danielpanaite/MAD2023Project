package com.example.courtreservationapplicationjetpack.views.reviews

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.courtreservationapplicationjetpack.CourtTopAppBar
import com.example.courtreservationapplicationjetpack.R
import com.example.courtreservationapplicationjetpack.components.BottomBar
import com.example.courtreservationapplicationjetpack.firestore.Court
import com.example.courtreservationapplicationjetpack.firestore.CourtWithId
import com.example.courtreservationapplicationjetpack.firestore.Reservation
import com.example.courtreservationapplicationjetpack.firestore.ReservationViewModel
import com.example.courtreservationapplicationjetpack.firestore.Review
import com.example.courtreservationapplicationjetpack.firestore.ReviewViewModel
import com.example.courtreservationapplicationjetpack.navigation.NavigationDestination
import com.example.courtreservationapplicationjetpack.signIn.GoogleAuthUiClient
import com.example.courtreservationapplicationjetpack.ui.theme.Orange200
import com.example.courtreservationapplicationjetpack.views.courts.CourtsAvailableDestination
import com.gowtham.ratingbar.RatingBar
import com.gowtham.ratingbar.RatingBarConfig
import com.gowtham.ratingbar.RatingBarStyle
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter


object ReviewMainPageDestination : NavigationDestination {
    override val route = "review_main_page"
    override val titleRes = "Reviews"
    override val icon = Icons.Default.Star
}

@ExperimentalMaterial3Api
@Composable
fun ReviewMainPage(
    navigateToCreateReview: (Array<String>) -> Unit,
    navController: NavController,
    onNavigateUp: () -> Unit,
    modifier: Modifier = Modifier,
    googleAuthUiClient: GoogleAuthUiClient,
    viewModel: ReviewViewModel = viewModel(),
) {
    val reservationFormatter: DateTimeFormatter =
        DateTimeFormatter.ofPattern("dd/MM/yyyy")
    val lifecycle = LocalLifecycleOwner.current

    val email = googleAuthUiClient.getSignedInUser()?.email

    if (email != null) {
        viewModel.getReviewByUser(email)
        viewModel.getReservationByEmail(email)
        viewModel.getCourtsWithId()
    }

    val reviewUiState by viewModel.myReviewsUiState.collectAsState()
    Log.d("reviewUiState", "$reviewUiState")
    val myReservationsUiState = viewModel.myReservationUiState
    Log.d("myReservationsUiState", "$myReservationsUiState")

    val reservationCourtsState by viewModel.reservationCourtsState.collectAsState()
    Log.d("reservationCourtsState", "$reservationCourtsState")


    Scaffold(
        topBar = { CourtTopAppBar(canNavigateBack = true, navigateUp = onNavigateUp, text = "Write your review") },
        bottomBar = { BottomBar(navController = navController as NavHostController) }
    ) {
            innerPadding ->
        MyReviewsBody(
            modifier = modifier.padding(innerPadding),
            reservationList = myReservationsUiState.value,
            reviewList = reviewUiState.reviewList,
            courtList = reservationCourtsState.courtList,
            onReviewClick = navigateToCreateReview,
            navController = navController
        )
    }
}

@Composable
fun MyReviewsBody(
    modifier: Modifier = Modifier,
    reservationList: List<Reservation>,
    reviewList: List<Review>,
    courtList: List<CourtWithId>,
    navController: NavController,
    onReviewClick: (Array<String>) -> Unit
){
    val text = buildAnnotatedString {
        withStyle(style = SpanStyle(fontSize = 16.sp)) {
            append("No courts available to review!")
        }
        append("\n") // Aggiungi un carattere di nuova linea per andare a capo
        withStyle(style = SpanStyle(fontSize = 14.sp)) {
            append("You can review a court after playing in it")
        }
    }
    Column(
        modifier = modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        if (reservationList.isEmpty() && courtList.isEmpty() && reviewList.isEmpty()) {
            Text(
                text = text,
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center,
                modifier = modifier.align(Alignment.CenterHorizontally)
            )
        } else {
            ReviewList(
                courtList,
                reviewList,
                reservationList,
                navController,
                onReviewClick = { onReviewClick(it); Log.d("it", "$it") }
            )
        }
    }
}

@Composable
fun ReviewList(
    courtList: List<CourtWithId>,
    reviewList: List<Review>,
    reservationList: List<Reservation>,
    navController: NavController,
    onReviewClick: (Array<String>) -> Unit,
){
    val reservationFormatter: DateTimeFormatter =
        DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val toReservationFormatter: DateTimeFormatter =
        DateTimeFormatter.ofPattern("dd/MM/yyyy")
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 16.dp)
    ){
        items(courtList.size){ci ->
            Card(modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp),
                elevation = CardDefaults.cardElevation( 8.dp )
            ) {
                Surface(color = Color.White) {
                    Row(
                        modifier = Modifier
                            .padding(16.dp)
                            .height(80.dp)
                    ) {
                        Card(
                            modifier = Modifier
                                .fillMaxSize()
                                .weight(2f)
                        ) {
                            AsyncImage(
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data("https://www.parrocchiecurtatone.it/wp-content/uploads/2020/07/WhatsApp-Image-2020-07-23-at-17.53.36-1984x1200.jpeg")
                                    .crossfade(true)
                                    .build(),
                                placeholder = painterResource(R.drawable.placeholder),
                                contentDescription = "Court Image",
                                contentScale = ContentScale.Crop,
                            )
                        }
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(6f)
                                .align(Alignment.CenterVertically)
                                .padding(start = 8.dp)
                        ) {
                            Text(
                                text = courtList[ci].court.name,
                                textAlign = TextAlign.Start,
                                fontWeight = FontWeight.Normal,
                            )
                            Text(
                                text = courtList[ci].court.address,
                                textAlign = TextAlign.Start,
                                fontWeight = FontWeight.ExtraLight,
                            )
                        }
                    }
                }
                if (!reservationList.none { it.court == courtList[ci].idCourt }) {
                    Log.d("reservationList", "${reservationList}")

                    val reservation = reservationList.filter { it.court == courtList[ci].idCourt }
                    Log.d("reservation", "${reservation}")

                    for(reservation in reservation) {
                        Log.d("reservation", "${reservation}")
                    // mettere che itera su un altra lista che sono le reservation per quel campo
                    Divider(thickness = 1.dp, color = MaterialTheme.colorScheme.primaryContainer)
                    Surface(color = Color.White) {
                        Row(
                            modifier = Modifier
                                .padding(end = 16.dp)
                        ) {
                            Text(
                                text = reservation.date.toDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().let{toReservationFormatter.format(it)},
                                modifier = Modifier.padding(start = 20.dp, top = 19.dp, bottom = 10.dp)

                            )
                            Button(
                                onClick = {
                                    val daPassare = arrayOf(courtList[ci].idCourt, reservation.id)
                                    //navController.navigate("${CourtsAvailableDestination.route}/${court.id}/${pickedDate.value}"
                                    navController.navigate("${ReviewCreatePageDestination.route}/${courtList[ci].idCourt}/${reservation.id}")
                                    Log.d("courtList[ci].idCourt", "${courtList[ci].idCourt}")
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = 32.dp, end = 32.dp, top = 8.dp, bottom = 8.dp)
                                    .align(Alignment.CenterVertically)
                            ) {
                                Log.d("reviewList", "$reviewList")


                                if (!reviewList.none { it.court == courtList[ci].idCourt && it.idReservation == reservation.id}) {
                                    Text(text = "Edit")
                                } else {
                                    Text(text = "Review")
                                }
                            }
                        }
                    }



                            if (!reviewList.none { it.court == courtList[ci].idCourt && it.idReservation == reservation.id }) {
                                Log.d("reviewList", "${reviewList}")

                                val review = reviewList.filter { it.court == courtList[ci].idCourt }
                                    .filter { it.idReservation == reservation.id }
                                Log.d("review", "${review}")
                                if (!review.isEmpty()) {
                                    val reviewDate = review[0].date?.toDate()?.toInstant()
                                        ?.atZone(ZoneId.systemDefault())?.toLocalDate()
                                    val formattedDate =
                                        reviewDate?.let { toReservationFormatter.format(it) }
                                   //Divider(
                                    //    thickness = 1.dp,
                                    //    color = MaterialTheme.colorScheme.primaryContainer
                                   // )
                                    Surface(color = Color.White) {
                                        Column(
                                            modifier = Modifier
                                                .padding(end = 16.dp)
                                        ) {
                                            Row(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(16.dp)
                                            ) {
                                                review[0].rating?.let {
                                                    RatingBar(
                                                        value = it.toFloat(),
                                                        config = RatingBarConfig()
                                                            .style(RatingBarStyle.HighLighted)
                                                            .size(18.dp)
                                                            .activeColor(Orange200),
                                                        onValueChange = {},
                                                        onRatingChanged = {},
                                                        modifier = Modifier
                                                            .fillMaxWidth()
                                                            .weight(3f)
                                                    )
                                                }
                                                Text(
                                                    text = formattedDate ?: "",
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .weight(1f)
                                                        .offset(y = (-1).dp),
                                                    fontWeight = FontWeight.ExtraLight,
                                                    style = MaterialTheme.typography.bodyMedium
                                                )
                                            }
                                            review[0].review?.let {
                                                Text(
                                                    text = it,
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .padding(
                                                            start = 16.dp,
                                                            bottom = 16.dp,
                                                            end = 16.dp
                                                        )
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }

                }
            }
        }
    }
}