package com.example.courtreservationapplicationjetpack.views.reviews

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.courtreservationapplicationjetpack.CourtTopAppBar
import com.example.courtreservationapplicationjetpack.components.BottomBar
import com.example.courtreservationapplicationjetpack.firestore.Review
import com.example.courtreservationapplicationjetpack.firestore.ReviewViewModel
import com.example.courtreservationapplicationjetpack.navigation.NavigationDestination
import com.example.courtreservationapplicationjetpack.ui.theme.Orange200
import com.gowtham.ratingbar.RatingBar
import com.gowtham.ratingbar.RatingBarConfig
import com.gowtham.ratingbar.RatingBarStyle

object CourtReviewPageDestination : NavigationDestination {
    override val route = "court_review_page"
    const val courtIdArg = "courtIdArg"
    val routeWithArgs = "$route/{$courtIdArg}"
    override val titleRes = "Reviews"
    override val icon = Icons.Default.Star
}

@ExperimentalMaterial3Api
@Composable
fun CourtReviewPage(
    navController: NavController,
    onNavigateUp: () -> Unit,
    courtIdArg: String,
    viewModel: ReviewViewModel = viewModel(),
    ) {
    //val courtReservationsState by viewModel.courtReviewsState.collectAsState()
    LaunchedEffect(courtIdArg) {
        viewModel.getReviewsByCourt(courtIdArg)
    }

    val reviewsState by viewModel.courtReviewsState.collectAsState()


    var isLoading by remember { mutableStateOf(true) } // Add flag to track loading state

    if (reviewsState.isLoading) { // Show circular progress indicator while loading
        Box(modifier = Modifier.fillMaxSize()){
            CircularProgressIndicator( modifier = Modifier.align(Alignment.Center))
        }
        return
    }

            Scaffold(
        topBar = { CourtTopAppBar(canNavigateBack = true, navigateUp = onNavigateUp, text = "Court reviews") },
        bottomBar = { BottomBar(navController = navController as NavHostController) }
    ) {
            innerPadding ->
        CourtReviewsBody(
            modifier = Modifier.padding(innerPadding),
            reviewList = reviewsState.reviewList
        )
    }
}

@Composable
fun CourtReviewsBody(
    modifier: Modifier = Modifier,
    reviewList: List<Review>
){

        // Otherwise, show the list of reviews
        Column(
            modifier = modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (reviewList.isEmpty()) {
                Text(
                    text = "No reviews for this court",
                    style = MaterialTheme.typography.headlineMedium,
                    textAlign = TextAlign.Center
                )
            } else {
                CourtReviewList(
                    reviewList = reviewList
                )
            }
        }

}

@Composable
fun CourtReviewList(
    reviewList: List<Review>
){
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 16.dp)
    ){
        items(reviewList.size){i ->
            Card(modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp),
                elevation = CardDefaults.cardElevation( 8.dp )
            ){
                Surface(color = Color.White){
                    Row(modifier = Modifier
                        .padding(start = 16.dp, top = 8.dp, bottom = 8.dp, end = 16.dp)
                    ){
                        Column(modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.CenterVertically)
                        ){
                            Text(
                                text = reviewList[i].user,
                                style = MaterialTheme.typography.headlineSmall
                            )
                        }
                    }
                }

                Divider(thickness = 1.dp, color = MaterialTheme.colorScheme.primaryContainer)

                Surface(color = Color.White){
                    Column(modifier = Modifier
                        .padding(end = 16.dp)
                    ){
                        RatingBar(
                            value = reviewList[i].rating.toFloat(),
                            config = RatingBarConfig()
                                .style(RatingBarStyle.HighLighted)
                                .size(16.dp)
                                .activeColor(Orange200),
                            onValueChange = {},
                            onRatingChanged = {},
                            modifier = Modifier
                                .align(Alignment.Start)
                                .padding(16.dp)
                        )
                        reviewList[i].review?.let {
                            Text(
                                text = it,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = 16.dp, bottom = 16.dp, end = 16.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}