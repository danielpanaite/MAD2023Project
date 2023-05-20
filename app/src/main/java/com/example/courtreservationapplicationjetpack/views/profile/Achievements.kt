package com.example.courtreservationapplicationjetpack.views.profile

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.courtreservationapplicationjetpack.CourtTopAppBar
import com.example.courtreservationapplicationjetpack.components.BottomBar
import com.example.courtreservationapplicationjetpack.models.achievements.Achievements
import com.example.courtreservationapplicationjetpack.navigation.NavigationDestination
import com.example.courtreservationapplicationjetpack.ui.appViewModel.AppViewModelProvider
import kotlinx.coroutines.launch


object AchievementsDestination : NavigationDestination {
    override val route  = "achievements"
    override val titleRes = "Achievements"
    override val icon = Icons.Default.Place

}

@ExperimentalMaterial3Api
@Composable
fun Achievements(
    navController: NavController,
    modifier: Modifier = Modifier,
    onNavigateUp: () -> Unit,
    navigateToNewAchievementsDestination: () -> Unit,

    viewModel: AchievementsViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val achievementsUi by viewModel.achievements.collectAsState()
    Scaffold(
        topBar = { CourtTopAppBar(canNavigateBack = false) },
        bottomBar = { BottomBar(navController = navController as NavHostController) }
    ) { innerPadding ->
        Box(modifier = modifier.padding(innerPadding)){
            AchievementsBody(
                achievementList = achievementsUi.achievementsList,
                modifier = modifier.padding(innerPadding),
                viewModel = viewModel
            )

            FloatingActionButton(onClick = { navigateToNewAchievementsDestination() },
            modifier = Modifier.align(Alignment.BottomEnd)
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Add")

            }
        }

    }
}

@Composable
fun AchievementsBody(
    achievementList: List<Achievements>,
    modifier: Modifier = Modifier,
    viewModel: AchievementsViewModel
) {
    val coroutineScope = rememberCoroutineScope()

    Box(modifier = modifier) {
        LazyColumn(

        ) {

            items(achievementList) { achievement ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 2.dp),
                    // elevation = 4.dp
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {

                                    Text(text = "Sport: ${achievement.sportName}")
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(text = "Title: ${achievement.idUser}")
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(text = "Date: ${achievement.date}")
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(text = "Description: ${achievement.description}")
                                }
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "delete",
                        Modifier.clickable(onClick = {
                            Log.d("prova", "$achievement")
                            coroutineScope.launch {
                                Log.d("dentro coroutine scope", "$achievement")
                                viewModel.deleteAchievement(achievement.id)

                                }


                            }))

                }
            }
        }
    }
}