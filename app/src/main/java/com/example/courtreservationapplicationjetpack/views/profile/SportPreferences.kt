package com.example.courtreservationapplicationjetpack.views.profile

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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.courtreservationapplicationjetpack.CourtTopAppBar
import com.example.courtreservationapplicationjetpack.components.BottomBar
import com.example.courtreservationapplicationjetpack.navigation.NavigationDestination
import com.example.courtreservationapplicationjetpack.ui.appViewModel.AppViewModelProvider
import com.example.courtreservationapplicationjetpack.views.courts.AllSportsViewModel
import androidx.compose.ui.text.input.ImeAction
import com.example.courtreservationapplicationjetpack.models.sport.Sport
import kotlinx.coroutines.launch

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
    viewModel: SportPreferencesViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val allSportsUiState by viewModel.allSportsUiState.collectAsState()
    Scaffold(
        topBar = { CourtTopAppBar(canNavigateBack = false) },
        bottomBar = { BottomBar(navController = navController as NavHostController) }
    ) { innerPadding ->
        SportsBody(
            sportsList = allSportsUiState.sportsList,
            modifier = modifier.padding(innerPadding),
            viewModel = viewModel
        )
    }
}

@Composable
fun SportsBody(
    sportsList: List<String>,
    modifier: Modifier = Modifier,
    viewModel: SportPreferencesViewModel
){
    val selectedSportsWithLevels by viewModel.sportPreferencesUiState.collectAsState()

    var selectedSports by remember { mutableStateOf(emptySet<String>()) }
    val sportsWithLevels = remember { mutableMapOf<String, String>() }
    val coroutineScope = rememberCoroutineScope()

    // Initialize selectedSports with the names of the sports that are already selected
    selectedSports = selectedSportsWithLevels.sportsList.map { it.sportName }.toMutableSet()


    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Button(onClick = {
            coroutineScope.launch {
                Log.d("SportsBody", "sportsWithLevels: $sportsWithLevels")

                val sports = sportsWithLevels.map { (sportName, masteryLevel) ->
                    Sport(1,  sportName, masteryLevel, null)
                    viewModel.updateSportMastery(sportName, masteryLevel) // use masteryLevel from map
                }


                val uncheckedSports = sportsList.filter { !selectedSports.contains(it) }

                Log.d("Unchecked sports", "$uncheckedSports")

                Log.d("sportList", "$sportsList")

                Log.d("selectedSports", "$selectedSports")


                viewModel.deleteSports(1, uncheckedSports)
                //viewModel.insertOrUpdateSportsWithLevels(sports)

                //navigateToMyReservations()
            }
        }) {
            Text(text = "Save", modifier = Modifier.weight(0.7f), fontWeight = FontWeight.Bold)
        }
        Text(
            text = "Select your preferred sports",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold
        )
        Log.d("sportsList in SportsBody", "$sportsList")
        Log.d("sportList with levels", "$sportsWithLevels")

        SportsList(
            viewModel =viewModel,
            sportsList = sportsList,
            onSportCheckedChange = { sport, isChecked, masteryLevel ->
                if (isChecked) {
                    Log.d("asteryLevel passato a sportList", "$masteryLevel")

                    println("masteryLevel passato a sportList $masteryLevel")
                    selectedSports += sport
                    sportsWithLevels[sport] = masteryLevel // Update mastery level for the selected sport in the map
                } else {
                    selectedSports -= sport
                    sportsWithLevels.remove(sport)
                }
            },
            selectedSportsWithLevels = selectedSportsWithLevels,
            initialLevels = selectedSportsWithLevels.sportsList.associateBy({it.sportName}, {it.masteryLevel?:""})


        )
        Spacer(modifier = Modifier.height(16.dp))

    }
}
@Composable
private fun SportsList(
    sportsList: List<String>,
    onSportCheckedChange: (String, Boolean, String) -> Unit,
    viewModel: SportPreferencesViewModel,
    selectedSportsWithLevels: SportsPreferencesUiState,
    initialLevels: Map<String, String>
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(items = sportsList, key = { it }) { sport ->
            val isChecked = remember {
                mutableStateOf(initialLevels.containsKey(sport))
            }
            val selectedLevel = remember {
                mutableStateOf(initialLevels[sport] ?: "")
            }

            // Check if the sport exists in the user's preference
            val sportPreference =
                selectedSportsWithLevels.sportsList.find { it.sportName == sport }
            if (sportPreference != null && !initialLevels.containsKey(sport)) {
                // If the sport exists and wasn't pre-selected, set isChecked to true and pre-populate the selectedLevel state
                isChecked.value = true
                selectedLevel.value = sportPreference.masteryLevel ?: ""
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
                    text = sport,
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
                        currentLevel = sportPreference?.masteryLevel ?: ""
                    )
                }
            }

            Divider()
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
    var selectedLevel by remember { mutableStateOf(currentLevel) }

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
