package com.example.courtreservationapplicationjetpack.views

import android.app.Activity
import android.view.WindowManager
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.courtreservationapplicationjetpack.CourtTopAppBar
import com.example.courtreservationapplicationjetpack.components.BottomBar
import com.example.courtreservationapplicationjetpack.firestore.UserViewModel
import com.example.courtreservationapplicationjetpack.navigation.NavigationDestination
import com.example.courtreservationapplicationjetpack.signIn.GoogleAuthUiClient
import com.example.courtreservationapplicationjetpack.views.profile.ProfileDestination


object MainScreenDestination : NavigationDestination {
    override val route = "home"
    override val titleRes = "Home"
    override val icon = Icons.Default.Home
}

@ExperimentalMaterial3Api
@Composable
fun MainScreen(
    navigateToAllSports: () -> Unit,
    navigateToReviews: () -> Unit,
    navigateToFindFriends: () -> Unit,
    navController: NavController,
    modifier: Modifier = Modifier,
    googleAuthUiClient : GoogleAuthUiClient,
) {

// Dentro il componibile
    val activity = LocalContext.current as? Activity
    activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)

    Scaffold(
        topBar = {
            CourtTopAppBar(canNavigateBack = false, text = "Home")
        },
        bottomBar = { BottomBar(navController = navController as NavHostController)}

    ) {
            innerPadding ->
        HomeBody(
            navController = navController,
            modifier = modifier.padding(innerPadding),
            navigateToAllSports = navigateToAllSports,
            navigateToReviews = navigateToReviews,
            navigateToFindFriends = navigateToFindFriends,
            googleAuthUiClient = googleAuthUiClient
        )
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeBody(
    modifier: Modifier = Modifier,
    navController: NavController,
    navigateToAllSports: () -> Unit,
    navigateToReviews: () -> Unit,
    navigateToFindFriends: () -> Unit,
    googleAuthUiClient : GoogleAuthUiClient,
    userViewModel: UserViewModel = viewModel()
){
    var profileComplete by rememberSaveable { mutableStateOf(false) }
    val email = googleAuthUiClient.getSignedInUser()?.email
    if(email != null)
        userViewModel.getUserByEmail(email)
    LazyColumn(modifier = modifier.fillMaxSize()){
        item {
            Card(
                onClick = navigateToAllSports,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                Image(
                    painter = rememberAsyncImagePainter
                        (
                        model = "https://images.unsplash.com/photo-1511415221243-04dab195b318?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=908&q=80",

                        ), contentDescription = null,

                    modifier = Modifier
                        .clip(MaterialTheme.shapes.medium)
                        .fillMaxWidth()
                        .height(100.dp)
                        .aspectRatio(4f / 3f)
                )
                Column(
                    modifier = Modifier.padding(8.dp)
                ) {
                    Text(
                        text = "Reserve a court",
                        style = MaterialTheme.typography.titleLarge
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Check to see all the available courts",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.height(4.dp))

                }
            }
        }
        item {
            Card(
                onClick = navigateToReviews,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                Image(
                    painter = rememberAsyncImagePainter
                        (
                        model = "https://media.istockphoto.com/id/1208411337/photo/consumer-reviews-concepts-with-bubble-people-review-comments-and-smartphone-rating-or.jpg?s=612x612&w=0&k=20&c=Rs8wAo4zS3FBEs4N_4a76zTSukulfrS6AErBKHZVJ9c=",

                        ), contentDescription = null,
                    modifier = Modifier
                        .clip(MaterialTheme.shapes.medium)
                        .fillMaxWidth()
                        .height(100.dp)
                        .aspectRatio(4f / 3f)
                )
                Column(
                    modifier = Modifier.padding(8.dp)
                ) {
                    Text(
                        text = "Review courts",
                        style = MaterialTheme.typography.titleLarge
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Leave a rating for the courts you have played on!",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                }
            }
        }
        item {
            if (userViewModel.user.value.id != "")
                Card(
                    onClick = {
                        if (userViewModel.user.value.nickname != "")
                            navigateToFindFriends()
                        else
                            profileComplete = true
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(8.dp)
                ) {
                    Image(
                        painter = rememberAsyncImagePainter
                            (
                            model = "https://imgur.com/iXKM4GF.png",

                            ), contentDescription = null,
                        modifier = Modifier
                            .clip(MaterialTheme.shapes.medium)
                            .fillMaxWidth()
                            .height(100.dp)
                            .aspectRatio(4f / 3f)
                    )
                    Column(
                        modifier = Modifier.padding(8.dp)
                    ) {
                        Text(
                            text = "Friends",
                            style = MaterialTheme.typography.titleLarge
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Find new partners to play with based on your chosen sports!",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                    }
                }
        }
    }
    if (profileComplete) {
        CompleteProfileDialog(
            onConfirm = {
                profileComplete = false
                navController.navigate(ProfileDestination.route)
            },
            onCancel = {
                profileComplete = false
            }
        )
    }
}

@Composable
private fun CompleteProfileDialog(
    onConfirm: () -> Unit,
    onCancel: () -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        onDismissRequest = { /* Do nothing */ },
        title = { Text("Wait!") },
        text = { Text("You need to add a nickname for your account before adding friends") },
        modifier = modifier.padding(16.dp),
        dismissButton = {
            Button(onClick = onCancel) {
                Text(text = "Close")
            }
        },
        confirmButton = {
            Button(onClick = onConfirm) {
                Text(text = "Go to profile")
            }
        }
    )
}