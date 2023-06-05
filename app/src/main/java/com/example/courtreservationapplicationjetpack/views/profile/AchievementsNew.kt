package com.example.courtreservationapplicationjetpack.views.profile

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
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
import com.example.courtreservationapplicationjetpack.firestore.Achievements
import com.example.courtreservationapplicationjetpack.firestore.UserViewModel
import com.example.courtreservationapplicationjetpack.firestore.toDate
import com.example.courtreservationapplicationjetpack.navigation.NavigationDestination
import com.example.courtreservationapplicationjetpack.signIn.GoogleAuthUiClient
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

object AchievementsDestination : NavigationDestination {
    override val route  = "achievements"
    override val titleRes = "Achievements"
    override val icon = Icons.Default.Place

}
@Composable
fun Achievements(
    navController: NavController,
    navigateBackAction: () -> Unit,
    modifier: Modifier = Modifier,
    onNavigateUp: () -> Unit,
    navigateToProfileDestination: () -> Unit,
    googleAuthUiClient: GoogleAuthUiClient,


    navigateToNewAchievementsDestination: () -> Unit,
    viewModel: UserViewModel = viewModel(),

) {

    val email = googleAuthUiClient.getSignedInUser()?.email
    var launchOnce by rememberSaveable { mutableStateOf(true) }
    if(launchOnce){
        if (email != null) {
            viewModel.getAchievements(email)
        }
        launchOnce = false
    }
    val achievementsUi by viewModel.achievements.collectAsState()

    if (achievementsUi.isLoading) {
        // Show circular progress indicator while loading
        Box(modifier = Modifier.fillMaxSize()){
            CircularProgressIndicator( modifier = Modifier.align(Alignment.Center))
        }
        return
    }

    Scaffold(
        topBar = { CourtTopAppBar(canNavigateBack = true,  navigateUp = navigateToProfileDestination) },
        bottomBar = { BottomBar(navController = navController as NavHostController) }
    ) { innerPadding ->
        Column(
            modifier
                .fillMaxSize()
                .padding(innerPadding)) {
            AchievementsBody(
                achievementList = achievementsUi.achievementsList,
                viewModel = viewModel,
                email = email,
                modifier = Modifier
                    .weight(1f)
                    .padding(top = 5.dp)
            )

            FloatingActionButton(
                onClick = { navigateToNewAchievementsDestination() },
                modifier = Modifier
                    .padding(20.dp)
                    .align(Alignment.End),
                containerColor = MaterialTheme.colorScheme.primary,
                shape = CircleShape
            ) {
                Icon(
                    Icons.Filled.Add,
                    contentDescription = "Add",
                    //modifier = Modifier.background(color = MaterialTheme.colorScheme.primary)
                )
            }
        }
    }
}


@Composable
fun AchievementsBody(
    achievementList: List<Achievements>?,
    modifier: Modifier = Modifier,
    email: String?,
    viewModel: UserViewModel
) {
    val coroutineScope = rememberCoroutineScope()

    if (achievementList.isNullOrEmpty()) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "You don't have any achievements saved, click on the add button to insert a new one",

                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center


                )

            }
        }


    } else {
        LazyColumn(
            modifier = modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp)
        ) {
            items(achievementList) { achievement ->
                AchievementsItem(
                    achievement = achievement,
                    onDeleteClick = {
                        coroutineScope.launch {
                            //viewModel.deleteAchievement()
                            if (email != null) {
                                viewModel.deleteAchievement(email, achievement)
                            }

                        }
                    }
                )
            }
        }
    }
}
@Composable
fun AchievementsItem(
    achievement: Achievements,
    onDeleteClick: () -> Unit
) {

    val showDialog = remember { mutableStateOf(false) }

    Log.d("achievement", "$achievement")
    Log.d("achievement.toData", achievement.toDate())


    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {

        //CoilImage(modifier = Modifier.fillMaxSize(), sport = achievement.sportName)

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            elevation = CardDefaults.cardElevation( 8.dp )
        ) {
            Box(
                modifier = Modifier
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.Gray.copy(alpha = 0.4f)
                            )
                        )
                    )
            ) {
                val imageUrl = when (achievement.sportName) {
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
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(imageUrl)
                        .crossfade(true)
                        .build(),
                    placeholder = painterResource(R.drawable.placeholder),
                    contentDescription = "Court Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .height(150.dp)
                        .blur(radius = 8.dp)
                )

                Column(
                    modifier = Modifier
                        .padding(top = 16.dp, start = 16.dp, end = 16.dp, bottom = 8.dp)
                        .fillMaxWidth()
                ) {
                    Row(
                    ) {

                        Text(
                            text = "Sport: ${achievement.sportName}",
                            style = MaterialTheme.typography.titleSmall,
                            modifier = Modifier
                                .background(Color.White.copy(0.7f), RoundedCornerShape(4.dp))
                                .padding(4.dp)
                        )

                        Spacer(modifier = Modifier.width(16.dp))
                        Text(
                            text = "Date: ${achievement.toDate()}",
                            style = MaterialTheme.typography.titleSmall,
                            modifier = Modifier.background(Color.White.copy(0.7f), RoundedCornerShape(4.dp)).padding(4.dp)
                        )

                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.TopEnd
                        ) {
                            IconButton(
                                onClick = {
                                    showDialog.value = true
                                },
                                modifier = Modifier.size(24.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Delete",
                                    tint = Color.Red
                                )
                            }
                        }
                    }
                    if (showDialog.value) {
                        AlertDialog(
                            onDismissRequest = { showDialog.value = false },
                            title = { Text("Delete Achievement") },
                            text = { Text("Are you sure you want to delete this achievement?") },
                            confirmButton = {
                                Button(
                                    onClick = {
                                        showDialog.value = false
                                        onDeleteClick()
                                    }
                                ) {
                                    Text("Delete")
                                }
                            },
                            dismissButton = {
                                Button(
                                    onClick = { showDialog.value = false }
                                ) {
                                    Text("Cancel")
                                }
                            }
                        )
                    }
                    Text(
                        text = "Title: ${achievement.title}",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(vertical = 8.dp).background(Color.White.copy(0.7f), RoundedCornerShape(4.dp)).padding(4.dp)
                    )
                    Text(
                        text = "Description: ${achievement.description}",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(bottom = 16.dp).background(Color.White.copy(0.7f), RoundedCornerShape(4.dp)).padding(4.dp)
                    )
                }
            }
        }
    }
}


fun Achievements.toDate(): String{
    return SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(date.toDate())
}