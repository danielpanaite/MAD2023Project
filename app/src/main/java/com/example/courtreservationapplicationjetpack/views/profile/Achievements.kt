package com.example.courtreservationapplicationjetpack.views.profile

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.courtreservationapplicationjetpack.CourtTopAppBar
import com.example.courtreservationapplicationjetpack.components.BottomBar
import com.example.courtreservationapplicationjetpack.models.sport.Sport
import com.example.courtreservationapplicationjetpack.navigation.NavigationDestination
import com.example.courtreservationapplicationjetpack.ui.appViewModel.AppViewModelProvider


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
    val achievementsUi by viewModel.achievementsUi.collectAsState()
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
    achievementList: List<String>,
    modifier: Modifier = Modifier,
    viewModel: AchievementsViewModel
) {
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

                        val achievementValues = achievement?.split("|")?.filter{it.isNotEmpty()}
                            ?.map{
                                val fields = it.split(";")
                                if (fields != null) {
                                    Text(text = "Sport: ${fields[0]}")
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(text = "Title: ${fields[2]}")
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(text = "Date: ${fields[1]}")
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(text = "Description: ${fields[3]}")
                                }

                            }


                    }
                }
            }
        }
    }
}