package com.example.courtreservationapplicationjetpack.views.reviews

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
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
import com.example.courtreservationapplicationjetpack.firestore.CourtWithId
import com.example.courtreservationapplicationjetpack.firestore.Review
import com.example.courtreservationapplicationjetpack.firestore.ReviewUiState
import com.example.courtreservationapplicationjetpack.firestore.ReviewViewModel
import com.example.courtreservationapplicationjetpack.navigation.NavigationDestination
import com.example.courtreservationapplicationjetpack.signIn.GoogleAuthUiClient
import com.google.firebase.Timestamp
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
    override val titleRes = "Reviews"
    override val icon = Icons.Default.Star
    private const val courtIdArg = "courtIdArg"
    private const val reservationIdArg = "reservationIdArg"

    val routeWithArgs = "$route/{$courtIdArg}/{$reservationIdArg}"

}


@Composable
fun ReviewCreatePage(
    navController: NavController,
    onNavigateUp: () -> Unit,
    courtIdArg: String,
    reservationIdArg: String,
    googleAuthUiClient: GoogleAuthUiClient,
    viewModel: ReviewViewModel = viewModel(),
    ) {
    val email = googleAuthUiClient.getSignedInUser()?.email
    Log.d("courtIdArg", "${courtIdArg}")
    Log.d("reservationArg", "${reservationIdArg}")


    var launchOnce by rememberSaveable { mutableStateOf(true) }
    if (launchOnce) {
        if (email != null) {
            Log.d("courtIdArg", "$courtIdArg")
            viewModel.getReviewByEmailCourtId(email, courtIdArg!!, reservationIdArg)
            viewModel.courtUiState(courtIdArg)
            Log.d("courtUiState", "${viewModel.courtUiState}")
        }
        launchOnce = false
    }


    val courtUiState by viewModel.courtUiState.collectAsState()
    val reviewUiState by viewModel.reviewUiState.collectAsState()


    val toastCreate = Toast.makeText(LocalContext.current, "Review sent!", Toast.LENGTH_SHORT)
    val toastDelete = Toast.makeText(LocalContext.current, "Review deleted!", Toast.LENGTH_SHORT)
    Scaffold(
        topBar = { CourtTopAppBar(canNavigateBack = true, navigateUp = onNavigateUp, text = "Write your review") },
        bottomBar = { BottomBar(navController = navController as NavHostController) }
    ) {
            innerPadding ->
        if (email != null && courtUiState.isNotEmpty()) {
            Log.d("courtUiState", "${courtUiState[0].court}")
            CreateForm(
                email = email,
                reviewUiState = reviewUiState,
                onReviewValueChange = viewModel::updateUiState,
                court = courtUiState[0].court,
                reservationIdArg = reservationIdArg,
                onSaveClick = {
                    CoroutineScope(Dispatchers.IO).launch {
                        println(reviewUiState.review)
                        viewModel.createReview()
                    }
                    toastCreate.show()
                    navController.popBackStack()
                },
                onDeleteClick = {
                    CoroutineScope(Dispatchers.IO).launch {
                        println(reviewUiState.review)
                        viewModel.deleteReview()
                    }
                    toastDelete.show()
                    navController.popBackStack()
                },
                modifier = Modifier.padding(innerPadding)
            )
        }
    }

}

@Composable
fun CreateForm(
    email: String,
    reviewUiState: ReviewUiState,
    onReviewValueChange: (Review) -> Unit,
    court: CourtWithId?,
    reservationIdArg: String,
    onSaveClick: () -> Unit,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier
){
    val reservationFormatter: DateTimeFormatter =
        DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val rating = remember {
        mutableStateOf(0)
    }
    LazyColumn(modifier = Modifier.fillMaxSize()){
        item{
            Card(
                modifier = modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ){
                if (court != null) {
                    if(reviewUiState.review.id == "" && court.idCourt != ""){
                        reviewUiState.review.user = email
                        reviewUiState.review.court = court.idCourt
                        reviewUiState.review.date = Timestamp.now()
                        reviewUiState.review.idReservation = reservationIdArg
                        Log.d("reservationIdArg in card", "$reservationIdArg")

                    }else{
                        println(reviewUiState.review)
                        rating.value = reviewUiState.review.rating!!
                    }
                }
                Column(modifier = Modifier.fillMaxSize()){
                    reviewUiState.review.review?.let { it ->
                        OutlinedTextField(
                            value = it,
                            onValueChange = { onReviewValueChange(reviewUiState.review.copy(review = it))},
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                            label = { Text(text = "Review") },
                            modifier = Modifier
                                .fillMaxWidth(),
                            singleLine = false
                        )
                    }
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
                    if(reviewUiState.review.id != "") {
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
    }
}