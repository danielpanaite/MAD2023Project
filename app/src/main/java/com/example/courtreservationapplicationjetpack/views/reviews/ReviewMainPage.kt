package com.example.courtreservationapplicationjetpack.views.reviews

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.Card
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.courtreservationapplicationjetpack.CourtTopAppBar
import com.example.courtreservationapplicationjetpack.R
import com.example.courtreservationapplicationjetpack.components.BottomBar
import com.example.courtreservationapplicationjetpack.models.courts.Court
import com.example.courtreservationapplicationjetpack.models.reservations.Reservation
import com.example.courtreservationapplicationjetpack.models.reviews.Review
import com.example.courtreservationapplicationjetpack.navigation.NavigationDestination
import com.example.courtreservationapplicationjetpack.ui.appViewModel.AppViewModelProvider
import com.example.courtreservationapplicationjetpack.views.reservations.MyReservationsViewModel
import com.gowtham.ratingbar.RatingBar
import com.gowtham.ratingbar.RatingBarConfig
import com.gowtham.ratingbar.RatingBarStyle

object ReviewMainPageDestination : NavigationDestination {
    override val route = "review_main_page"
    override val titleRes = "Reviews"
    override val icon = Icons.Default.Star
}

@ExperimentalMaterial3Api
@Composable
fun ReviewMainPage(
    navigateToCreateReview: (Int) -> Unit,
    navController: NavController,
    onNavigateUp: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: MyReservationsViewModel = viewModel(factory = AppViewModelProvider.Factory),
    reviewViewModel: ReviewViewModel = viewModel(factory = AppViewModelProvider.Factory),
) {
    val myReservationsUiState by viewModel.myReservationsUiState.collectAsState()
    val reservationCourtsState by viewModel.reservationCourtsState.collectAsState()
    val reviewUiState by reviewViewModel.myReviewsUiState.collectAsState()

    viewModel.setCourts(myReservationsUiState.reservationList.map { it.courtId })

    Scaffold(
        topBar = { CourtTopAppBar(canNavigateBack = true, navigateUp = onNavigateUp, text = "Write your review") },
        bottomBar = { BottomBar(navController = navController as NavHostController) }
    ) {
            innerPadding ->
        MyReviewsBody(
            modifier = modifier.padding(innerPadding),
            reservationList = myReservationsUiState.reservationList,
            reviewList = reviewUiState.reviewList,
            courtList = reservationCourtsState.courtList,
            onReviewClick = navigateToCreateReview
        )
    }
}

@Composable
fun MyReviewsBody(
    modifier: Modifier = Modifier,
    reservationList: List<Reservation>,
    reviewList: List<Review>,
    courtList: List<Court>,
    onReviewClick: (Int) -> Unit
){
    Column(
        modifier = modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        if (reservationList.isEmpty() && courtList.isEmpty() && reviewList.isEmpty()) {
            Text(
                text = "No courts available to review",
                style = MaterialTheme.typography.headlineMedium,
                textAlign = TextAlign.Center
            )
        } else {
            ReviewList(
                courtList,
                reviewList,
                onReviewClick = { onReviewClick(it.id) }
            )
        }
    }
}

@Composable
fun ReviewList(
    courtList: List<Court>,
    reviewList: List<Review>,
    onReviewClick: (Court) -> Unit,
){
    LazyColumn(modifier = Modifier.fillMaxSize()){
        items(courtList.size){ci ->
            Card(modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 16.dp)
            ){
                Surface(color = Color.White){
                    Row(modifier = Modifier
                        .padding(16.dp)
                        .height(80.dp)){
                        Card(modifier = Modifier
                            .fillMaxSize()
                            .weight(2f)){
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
                        Column(modifier = Modifier
                            .fillMaxWidth()
                            .weight(6f)
                            .align(Alignment.CenterVertically)
                            .padding(start = 8.dp)){
                            Text(
                                text = courtList[ci].name,
                                textAlign = TextAlign.Start,
                                fontWeight = FontWeight.Normal,
                            )
                            Text(
                                text = courtList[ci].address,
                                textAlign = TextAlign.Start,
                                fontWeight = FontWeight.ExtraLight,
                            )
                        }
                    }
                }
                Divider(thickness = 1.dp, color = MaterialTheme.colorScheme.primaryContainer)
                Surface(color = Color.White){
                    Row(modifier = Modifier
                        .padding(end = 16.dp)
                    ){
                        Button(onClick = {
                            onReviewClick(courtList[ci])
                        },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 32.dp, end = 32.dp, top = 8.dp, bottom = 8.dp)
                                .align(Alignment.CenterVertically)
                        ) {
                            if(!reviewList.none { it.court == courtList[ci].id }) {
                                Text(text = "Edit")
                            }else{
                                Text(text = "Review")
                            }
                        }
                    }
                }
                if(!reviewList.none { it.court == courtList[ci].id }){
                    Divider(thickness = 1.dp, color = MaterialTheme.colorScheme.primaryContainer)
                    Surface(color = Color.White){
                        Column(modifier = Modifier
                            .padding(end = 16.dp)
                        ){
                            Text(
                                text = "My review:",
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 16.dp),
                                color = Color.Gray
                            )
                            Text(
                                text = reviewList.filter { it.court == courtList[ci].id }[0].review,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                            )
                            RatingBar(
                                value = reviewList.filter { it.court == courtList[ci].id }[0].rating.toFloat(),
                                config = RatingBarConfig()
                                    .style(RatingBarStyle.HighLighted)
                                    .size(10.dp)
                                    .activeColor(Color.Gray),
                                onValueChange = {},
                                onRatingChanged = {},
                                modifier = Modifier
                                    .align(Alignment.Start)
                                    .padding(start = 16.dp, bottom = 16.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}