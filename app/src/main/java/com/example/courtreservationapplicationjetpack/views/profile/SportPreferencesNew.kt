package com.example.courtreservationapplicationjetpack.views.profile

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.courtreservationapplicationjetpack.CourtTopAppBar
import com.example.courtreservationapplicationjetpack.components.BottomBar
import com.example.courtreservationapplicationjetpack.firestore.Sport
import com.example.courtreservationapplicationjetpack.firestore.SportsPreferencesUiState
import com.example.courtreservationapplicationjetpack.firestore.UserViewModel
import com.example.courtreservationapplicationjetpack.navigation.NavigationDestination
import com.example.courtreservationapplicationjetpack.signIn.GoogleAuthUiClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.Locale

object SportPreferencesDestination : NavigationDestination {
    override val route  = "sport_preferences"
    override val titleRes = "Sport Preferences"
    override val icon = Icons.Default.Place

}

@ExperimentalMaterial3Api
@Composable
fun SportPreferences(
    navController: NavController,
    modifier: Modifier = Modifier,
    onNavigateUp: () -> Unit,
    navigateToProfileDestination: () ->Unit,
    googleAuthUiClient: GoogleAuthUiClient,
    viewModel: UserViewModel = viewModel(),

    ) {
    val email = googleAuthUiClient.getSignedInUser()?.email
    var launchOnce by rememberSaveable { mutableStateOf(true) }
    if(launchOnce){
        viewModel.getSportsList()
        if (email != null) {
            viewModel.getSportsWithLevels(email)
        }
        launchOnce = false
    }
    val sportsList by remember { mutableStateOf(viewModel.sports) }


    Scaffold(
        topBar = { CourtTopAppBar(canNavigateBack = true,
            navigateUp = onNavigateUp, text = "Sports") },
        bottomBar = { BottomBar(navController = navController as NavHostController) }
    ) { innerPadding ->
        SportsBody(
            modifier = modifier.padding(innerPadding),
            navigateToProfileDestination = navigateToProfileDestination,
            sportsList = sportsList,
            email = email
        )
    }
}

@Composable
fun SportsBody(
    sportsList: State<List<String>>,
    modifier: Modifier = Modifier,
    navigateToProfileDestination: () ->Unit,
    email: String?,
    viewModel: UserViewModel = viewModel()
){

    val selectedSportsWithLevels by viewModel.sportsPreferencesUiState.collectAsState()

    if (selectedSportsWithLevels.isLoading) {
        // Show circular progress indicator while loading
        Box(modifier = Modifier.fillMaxSize()){
            CircularProgressIndicator( modifier = Modifier.align(Alignment.Center))
        }
        return
    }

    var selectedSports by remember { mutableStateOf(emptySet<String>()) }
    val sportsWithLevels = remember { mutableMapOf<String, String>() }
    val coroutineScope = rememberCoroutineScope()
    selectedSports = selectedSportsWithLevels.sportsList.map { it.sportName }.toMutableSet()
    val initialLevels: Map<String, String> = selectedSportsWithLevels.sportsList.associate { sport ->
        sport.sportName to sport.masteryLevel
    }
    val context = LocalContext.current


    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Row(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Select your preferred sports",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .weight(3f)
                        .align(Alignment.CenterVertically)
                )
                Spacer(modifier = Modifier.weight(1f))
            }
        }

        val saveClick = {
            coroutineScope.launch {
                val sports = sportsWithLevels.map { (sportName, masteryLevel) ->
                    Sport(sportName, masteryLevel)
                }
                val uncheckedSports = sportsList.value.filter { !selectedSports.contains(it) }
                if (email != null) {
                    viewModel.updateSportsPreferences(email, sports, uncheckedSports)
                }
                Toast.makeText(context, "Saved successfully", Toast.LENGTH_SHORT).show()
                navigateToProfileDestination()
            } }

        SportsList(
            sportsList = sportsList.value,
            onSportCheckedChange = { sport, isChecked, masteryLevel ->
                if (isChecked) {
                    selectedSports += sport
                    sportsWithLevels[sport] = masteryLevel // Update mastery level for the selected sport in the map
                } else {
                    selectedSports -= sport
                    sportsWithLevels.remove(sport)
                }
            },
            selectedSportsWithLevels = selectedSportsWithLevels,
            initialLevels = initialLevels,
            saveClick = saveClick
        )
    }
}

@Composable
private fun SportsList(
    sportsList: List<String>,
    onSportCheckedChange: (String, Boolean, String) -> Unit,
    selectedSportsWithLevels: SportsPreferencesUiState,
    initialLevels: Map<String, String>,
    saveClick: () -> Job
) {
    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        itemsIndexed(items = sportsList.toList(), key = { index, _ -> index }) { index, sport ->
            val isChecked = rememberSaveable {
                mutableStateOf(initialLevels.containsKey(sport))
            }
            val selectedLevel = rememberSaveable {
                mutableStateOf(initialLevels[sport] ?: "Beginner")
            }
            // Check if the sport exists in the user's preference
            val sportPreference = selectedSportsWithLevels.sportsList.filter { it.sportName == sport }

            if (sportPreference.isNotEmpty() && !initialLevels.containsKey(sport)) {
                isChecked.value = true
                selectedLevel.value = sportPreference[0].masteryLevel ?: ""
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = {
                        isChecked.value = !isChecked.value
                        onSportCheckedChange(sport, isChecked.value, selectedLevel.value)
                    }),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = isChecked.value,
                    onCheckedChange = {
                        isChecked.value = it
                        onSportCheckedChange(sport, isChecked.value, selectedLevel.value)
                    },
                    modifier = Modifier.weight(0.3f)
                )

                Text(
                    text = sport.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() },
                    modifier = Modifier.weight(0.7f),
                    fontWeight = FontWeight.Bold
                )

                if (isChecked.value) {
                    SportLevelInput(
                        onLevelChanged = {
                            // save the level for this sport
                            // update the selected level
                            selectedLevel.value = it
                            // pass the selected level as a plain string to the onSportCheckedChange lambda
                            onSportCheckedChange(sport, isChecked.value, selectedLevel.value)
                        },
                        currentLevel = sportPreference.firstOrNull()?.masteryLevel ?: ""
                    )
                }
            }

            Divider()
        }
        item{
            Spacer(modifier = Modifier.height(16.dp))
            Box(modifier = Modifier.fillMaxWidth()) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    Button(
                        onClick = {
                            saveClick()
                        },
                        modifier = Modifier.align(Alignment.CenterVertically).fillMaxWidth()
                    ) {
                        Text(text = "Save", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SportLevelInput(
    onLevelChanged: (String) -> Unit,
    currentLevel: String = ""
) {
    var expanded by remember { mutableStateOf(false) }
    val levels = listOf("Beginner", "Intermediate", "Advanced", "Expert")
    var selectedLevel by rememberSaveable { mutableStateOf(currentLevel.takeIf { it.isNotEmpty() } ?: "Beginner") }
    Box(modifier = Modifier.wrapContentSize()) {
        OutlinedButton(
            onClick = { expanded = true },
            modifier = Modifier.width(150.dp)
        ) {
            Text(
                text = selectedLevel,
                fontWeight = FontWeight.Bold
            )
            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = null,
                modifier = Modifier.padding(start = 2.dp)
            )
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.align(Alignment.TopStart)
        ) {
            levels.forEach { level ->
                DropdownMenuItem(
                    text = { Text(level, color = Color.Black) },
                    onClick = {
                        selectedLevel = level
                        onLevelChanged(selectedLevel)
                        expanded = false
                    })
            }
        }
    }
}

