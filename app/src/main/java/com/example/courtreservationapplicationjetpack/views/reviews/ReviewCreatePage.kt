package com.example.courtreservationapplicationjetpack.views.reviews

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.courtreservationapplicationjetpack.CourtTopAppBar
import com.example.courtreservationapplicationjetpack.components.BottomBar
import com.example.courtreservationapplicationjetpack.models.courts.Court
import com.example.courtreservationapplicationjetpack.models.reviews.Review
import com.example.courtreservationapplicationjetpack.navigation.NavigationDestination
import com.example.courtreservationapplicationjetpack.ui.appViewModel.AppViewModelProvider
import com.gowtham.ratingbar.RatingBar
import com.gowtham.ratingbar.RatingBarConfig
import com.gowtham.ratingbar.RatingBarStyle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

object ReviewCreatePageDestination : NavigationDestination {
    override val route = "review_create_page"
    const val courtIdArg = "courtId"
    val routeWithArgs = "$route/{$courtIdArg}"
    override val titleRes = "Reviews"
    override val icon = Icons.Default.Star
}

@Composable
fun ReviewCreatePage(
    navController: NavController,
    onNavigateUp: () -> Unit,
    viewModel: ReviewCreateViewModel = viewModel(factory = AppViewModelProvider.Factory),
) {
    val courtUiState by viewModel.courtUiState.collectAsState()

    val toastCreate = Toast.makeText(LocalContext.current, "Review sent!", Toast.LENGTH_SHORT)
    val toastDelete = Toast.makeText(LocalContext.current, "Review deleted!", Toast.LENGTH_SHORT)
    Scaffold(
        topBar = { CourtTopAppBar(canNavigateBack = true, navigateUp = onNavigateUp, text = "Write your review") },
        bottomBar = { BottomBar(navController = navController as NavHostController) }
    ) {
        innerPadding ->
        CreateForm(
            reviewUiState = viewModel.reviewsUiState,
            onReviewValueChange = viewModel::updateUiState,
            court = courtUiState.court,
            onSaveClick = {
                CoroutineScope(Dispatchers.IO).launch {
                    println(viewModel.reviewsUiState.review)
                    viewModel.createReview()
                }
                toastCreate.show()
                navController.navigate(ReviewMainPageDestination.route)
            },
            onDeleteClick = {
                CoroutineScope(Dispatchers.IO).launch {
                    println(viewModel.reviewsUiState.review)
                    viewModel.deleteReview()
                }
                toastDelete.show()
                navController.navigate(ReviewMainPageDestination.route)
            },
            modifier = Modifier.padding(innerPadding)
        )
    }

}

@Composable
fun CreateForm(
    reviewUiState: ReviewCreateViewModel.ReviewUiState,
    onReviewValueChange: (Review) -> Unit,
    court: Court?,
    onSaveClick: () -> Unit,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier
){
    val reservationFormatter: DateTimeFormatter =
        DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val rating = remember {
        mutableStateOf(0)
    }
    Card(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ){
        if(reviewUiState.review.id == null && court != null){
            reviewUiState.review.user = 1
            reviewUiState.review.court = court.id
            reviewUiState.review.date = LocalDate.now().format(reservationFormatter)
        }else{
            println(reviewUiState.review)
            rating.value = reviewUiState.review.rating
        }
        Column(modifier = Modifier.fillMaxSize()){
            OutlinedTextField(
                value = reviewUiState.review.review ,
                onValueChange = {onReviewValueChange(reviewUiState.review.copy(review = it))},
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                label = {Text(text = "Review")},
                modifier = Modifier
                    .fillMaxWidth(),
                singleLine = false
            )
            RatingBar(
                value = rating.value.toFloat(),
                config = RatingBarConfig()
                    .style(RatingBarStyle.HighLighted)
                    .size(32.dp)
                    .activeColor(MaterialTheme.colorScheme.primary),
                onValueChange = {
                    rating.value = it.toInt()
                },
                onRatingChanged = {
                    onReviewValueChange(reviewUiState.review.copy(rating = it.toInt()))
                },
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 16.dp, bottom = 16.dp)
            )
            Button(onClick = onSaveClick,
                enabled = reviewUiState.isEntryValid,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp))
            {
                Text(text = "Submit")
            }
            if(reviewUiState.review.id != null) {
                OutlinedButton(
                    onClick = onDeleteClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp)
                )
                {
                    Text(text = "Delete")
                }
            }
        }
    }
}