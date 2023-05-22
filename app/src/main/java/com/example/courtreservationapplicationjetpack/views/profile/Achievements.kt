package com.example.courtreservationapplicationjetpack.views.profile

import android.content.res.Resources.Theme
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
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
        topBar = { CourtTopAppBar(canNavigateBack = true,
            navigateUp = onNavigateUp) },
        bottomBar = { BottomBar(navController = navController as NavHostController) }
    ) { innerPadding ->
        Box(modifier = modifier
            .fillMaxSize()
            .padding(innerPadding)){
            AchievementsBody(
                achievementList = achievementsUi.achievementsList,
                viewModel = viewModel,
                modifier = Modifier.padding(top = 5.dp),

            )

            FloatingActionButton(
                onClick = { navigateToNewAchievementsDestination() },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(20.dp),
                containerColor = MaterialTheme.colors.primary, // Set the background color to primary color
                contentColor = MaterialTheme.colors.onPrimary, // Set the content color to match the onPrimary color
                shape = CircleShape // Set the shape to CircleShape for a round button
            ) {
                Icon(
                    Icons.Filled.Add,
                    contentDescription = "Add",
                    modifier = Modifier.background(color = MaterialTheme.colors.primary)
                )
            }

        }

    }
}

@Composable
fun AchievementsBody(
    achievementList: List<Achievements>?,
    modifier: Modifier = Modifier,
    viewModel: AchievementsViewModel
) {
    val coroutineScope = rememberCoroutineScope()

    if (achievementList.isNullOrEmpty()) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(Color.White)
                .verticalScroll(rememberScrollState())
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "You don't have any achievements saved, click on the add button to insert a new one",

                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)

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
                        viewModel.deleteAchievement(achievement.id) }
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
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
    ) {
        Column(
            modifier = Modifier
                .padding(top = 16.dp, start = 16.dp, end = 16.dp, bottom = 8.dp)
                .fillMaxWidth()
        ) {
            Row(
            ) {

                Text(
                    text = "Sport: ${achievement.sportName}",
                    style = MaterialTheme.typography.subtitle2
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = "Date: ${achievement.date}",
                    style = MaterialTheme.typography.subtitle2
                )

                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.TopEnd
                ) {
                    IconButton(
                        onClick = onDeleteClick,
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
            Text(
                text = "Title: ${achievement.idUser}",
                style = MaterialTheme.typography.h6,
                modifier = Modifier.padding(vertical = 8.dp)
            )
            Text(
                text = "Description: ${achievement.description}",
                style = MaterialTheme.typography.body1,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }
    }
}

