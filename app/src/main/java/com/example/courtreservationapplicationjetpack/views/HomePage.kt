package com.example.courtreservationapplicationjetpack.views

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.example.courtreservationapplicationjetpack.CourtTopAppBar
import com.example.courtreservationapplicationjetpack.components.BottomBar
import com.example.courtreservationapplicationjetpack.navigation.NavigationDestination


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
    navController: NavController,
    modifier: Modifier = Modifier,
) {

    Scaffold(
        topBar = {
            CourtTopAppBar(canNavigateBack = false)
        },
        bottomBar = { BottomBar(navController = navController as NavHostController)}

    ) {
            innerPadding ->
        HomeBody(
            navController = rememberNavController(),
            //navController = navController,
            modifier = modifier.padding(innerPadding),
            navigateToAllSports = navigateToAllSports,
            navigateToReviews = navigateToReviews
        )
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeBody(
    modifier: Modifier = Modifier,
    navController: NavController = rememberNavController(),
    navigateToAllSports: () -> Unit,
    navigateToReviews: () -> Unit,
){
    Column(modifier = modifier.fillMaxSize()){
        Card(
            onClick = navigateToAllSports,
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
                .border(BorderStroke(1.dp, Color.Gray), MaterialTheme.shapes.medium),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant,

                ),
        ) {
            Image(
                painter = rememberAsyncImagePainter
                    (
                    model ="https://images.unsplash.com/photo-1511415221243-04dab195b318?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=908&q=80",

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

        Card(
            onClick = navigateToReviews,
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
                .border(BorderStroke(1.dp, Color.Gray), MaterialTheme.shapes.medium),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant,

                ),
        ) {
            Image(
                painter = rememberAsyncImagePainter
                    (
                    model ="https://media.istockphoto.com/id/1208411337/photo/consumer-reviews-concepts-with-bubble-people-review-comments-and-smartphone-rating-or.jpg?s=612x612&w=0&k=20&c=Rs8wAo4zS3FBEs4N_4a76zTSukulfrS6AErBKHZVJ9c=",

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

}