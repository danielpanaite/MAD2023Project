package com.example.courtreservationapplicationjetpack.views.profile

import android.util.Log
import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
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
import com.example.courtreservationapplicationjetpack.navigation.NavigationDestination
import com.example.courtreservationapplicationjetpack.ui.appViewModel.AppViewModelProvider
import com.example.courtreservationapplicationjetpack.views.courts.AllSportsViewModel
import com.maxkeppeker.sheets.core.models.base.rememberUseCaseState
import com.maxkeppeler.sheets.date_time.DateTimeDialog
import com.maxkeppeler.sheets.date_time.models.DateTimeSelection
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter


object NewAchievementsDestination : NavigationDestination {
    override val route  = "new_chievements"
    override val titleRes = "new Achievements"
    override val icon = Icons.Default.Place
}



@ExperimentalMaterial3Api
@Composable
fun NewAchievements(
    navController: NavController,
    modifier: Modifier = Modifier,
    onNavigateUp: () -> Unit,
    navigateToAchievementsDestination: () -> Unit,
    viewModel: AchievementsViewModel = viewModel(factory = AppViewModelProvider.Factory),
    viewModelSports : AllSportsViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {

    val sportsUiState by viewModelSports.allSportsUiState.collectAsState()


    Scaffold(
        topBar = { CourtTopAppBar(canNavigateBack = true,
            navigateUp = onNavigateUp) },
        bottomBar = { BottomBar(navController = navController as NavHostController) }
    ) { innerPadding ->
        Box(
            modifier = modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(Color.White)
                .verticalScroll(rememberScrollState())
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(text = "Insert your achievement",
                    style = androidx.compose.material3.MaterialTheme.typography.titleMedium,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)

                )
                Spacer(modifier = Modifier.height(20.dp))
                NewAchievementsBody(
                    sportsList = sportsUiState.sportsList,
                    modifier = modifier,
                    viewModel = viewModel,
                    navigateToAchievementsDestination  =navigateToAchievementsDestination
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewAchievementsBody(
    sportsList: List<String>,
    modifier: Modifier = Modifier,
    viewModel: AchievementsViewModel,
    navigateToAchievementsDestination: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()

    var isMenuExpanded by remember { mutableStateOf(false) }
    var selectedSport by remember { mutableStateOf("") }
    //var date by remember { mutableStateOf("") }
    var certificateName by remember { mutableStateOf("") }
    var additionalInfo by remember { mutableStateOf("") }
    val selectedDate = remember { mutableStateOf<LocalDate?>(null) }
    var showDatePicker by remember { mutableStateOf(false) }
    val dateFormat = SimpleDateFormat("dd/MM/yyyy")

    val dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    var date by remember { mutableStateOf<LocalDate?>(null) }

    var showErrorDialog by remember { mutableStateOf(false) }
    var message by remember {
        mutableStateOf("")
    }
    fun closeSelection(){
        showDatePicker = false

    }


    Column(modifier.padding(top = 5.dp)) {
        Row(modifier = Modifier.fillMaxWidth().padding(bottom = 11.dp)) {

            Box(
                modifier = Modifier
                    .weight(0.5f)
            ) {
                OutlinedButton(
                    onClick = { isMenuExpanded = true },
                    modifier = modifier.fillMaxWidth()
                ) {
                    if (selectedSport.isEmpty()) {
                        Text(
                            text = "Choose the sport",
                            color = Color.Gray
                        )
                    } else {
                        Text(
                            text = selectedSport,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = null,
                        modifier = Modifier.padding(start = 2.dp)
                    )
                }
                DropdownMenu(
                    expanded = isMenuExpanded,
                    onDismissRequest = { isMenuExpanded = false },
                    modifier = Modifier
                ) {

                    sportsList.forEach { sport ->
                        Log.d("sport", "$sport")
                        DropdownMenuItem(
                            modifier = Modifier.fillMaxWidth(),
                            text = { Text(sport, color = Color.Black) },
                            onClick = {
                                selectedSport = sport
                                isMenuExpanded = false
                            }
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.width(8.dp)) // Spazio tra i bottoni

            Box(
                modifier = Modifier
                    .weight(0.5f)
            ) {
                OutlinedButton(
                    onClick = {
                        if (!showDatePicker) {
                            showDatePicker = true
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    if (date == null) {
                        Text(
                            text = "Insert Date",
                            color = Color.Gray
                        )
                    } else {
                        Text(
                            text = dateFormatter.format(date!!),
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = null,
                        modifier = Modifier.padding(start = 2.dp)
                    )
                }
            }


        }

        if (showDatePicker) {
            DateTimeDialog(
                state = rememberUseCaseState(visible = true, onCloseRequest = { closeSelection() }),
                selection = DateTimeSelection.Date { newDate ->
                    selectedDate.value = newDate
                    date = LocalDate.from(newDate)
                    showDatePicker = false
                },
            )
        }

        OutlinedTextField(
            value = certificateName,
            onValueChange = { certificateName = it },
            label = { Text("Nome del certificato") },
            shape = RoundedCornerShape(50.dp),
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
                .padding(top = 5.dp)

                .height(60.dp)
                .background(Color.White)
        )
        OutlinedTextField(
            value = additionalInfo,
            onValueChange = { additionalInfo = it },
            label = { Text("Informazioni aggiuntive") },
            shape = RoundedCornerShape(50.dp),
            singleLine = false,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
                .padding(top = 5.dp)

                .background(Color.White)


        )
        Button(
            onClick = {

                if(selectedSport!="" && date.toString()!="" && certificateName!=""){
                    coroutineScope.launch {
                        viewModel.addAchievement(
                            selectedSport,
                            1,
                            date.toString(),
                            certificateName,
                            additionalInfo
                        )
                    }
                    navigateToAchievementsDestination()
                }else{
                    if(selectedSport==""){
                        message = "Select a sport"
                        showErrorDialog = true
                    }else if(date.toString() =="" || date == null){
                        message = "Select a date"
                        showErrorDialog = true
                    }else if(certificateName == ""){
                        message = "Insert a correct certificate name"
                        showErrorDialog = true
                    }

                }

                //salva i dati qui
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
                .padding(top = 16.dp)

                .height(56.dp)
                .background(Color.White, RoundedCornerShape(16.dp))
        ) {
            Text(text = "Salva")
        }
        if (showErrorDialog) {
            AlertDialog(
                onDismissRequest = { showErrorDialog = false },
                title = { Text(text = "Errore") },
                text = { Text(text = message) },
                confirmButton = {
                    Button(onClick = { showErrorDialog = false }) {
                        Text(text = "Ok")
                    }
                }
            )
        }
    }
}
