package com.example.courtreservationapplicationjetpack.views.profile

import android.graphics.fonts.FontStyle
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.courtreservationapplicationjetpack.CourtTopAppBar
import com.example.courtreservationapplicationjetpack.components.BottomBar
import com.example.courtreservationapplicationjetpack.models.sport.Sport
import com.example.courtreservationapplicationjetpack.navigation.NavigationDestination
import com.example.courtreservationapplicationjetpack.ui.appViewModel.AppViewModelProvider
import java.time.format.TextStyle
import com.maxkeppeker.sheets.core.models.base.rememberUseCaseState
import com.maxkeppeler.sheets.date_time.DateTimeDialog
import com.maxkeppeler.sheets.date_time.models.DateTimeSelection
import kotlinx.coroutines.launch
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.util.Date

import java.time.LocalDateTime
import java.time.ZoneId

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
    viewModel: AchievementsViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val favoritesSportsUi by viewModel.favoritesSportsUi.collectAsState()
    Scaffold(
        topBar = { CourtTopAppBar(canNavigateBack = false) },
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
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)

                        )
                Spacer(modifier = Modifier.height(20.dp))
                NewAchievementsBody(
                    sportsList = favoritesSportsUi.sportsList,
                    modifier = modifier,
                    viewModel = viewModel
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewAchievementsBody(
    sportsList: List<Sport>,
    modifier: Modifier = Modifier,
    viewModel: AchievementsViewModel
) {

    val coroutineScope = rememberCoroutineScope()


    var isMenuExpanded by remember { mutableStateOf(false) }

    Log.d("sportList", "$sportsList")
    val sportNameList = sportsList.map{it.sportName}
    Log.d("sportlistname", "${sportNameList}")

    var selectedSport by remember { mutableStateOf("") }

    var date by remember {
        mutableStateOf("")
    }
    var certificateName by remember {
        mutableStateOf("")
    }
    var additionalInfo by remember {
        mutableStateOf("")
    }

    val selectedDate = remember { mutableStateOf<LocalDate?>(null) }
    val savedData = "$selectedSport,$date,$certificateName,$additionalInfo"

    var showDatePicker by remember {mutableStateOf(false)}
    val dateFormat = SimpleDateFormat("dd/MM/yyyy")

    Column( modifier.padding(top=5.dp)){


        Box(modifier = Modifier.wrapContentSize()) {
            OutlinedButton(
                onClick = { isMenuExpanded = true },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = selectedSport,
                    fontWeight = FontWeight.Bold
                )
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
                    .padding(bottom = 16.dp)
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp)
            ) {
                sportNameList.forEach { sport ->
                    Log.d("sport", "$sport")
                    DropdownMenuItem(
                        modifier = Modifier,
                        text = { Text(sport, color = Color.Black) },

                        onClick = {
                            selectedSport = sport
                            //onSportChange(selectedSport)
                            isMenuExpanded = false
                        })
                }
            }
        }
        //Text(text = "Sport selezionato: $selectedSport")

        OutlinedTextField(
            value = date,
            onValueChange = {
                if(!showDatePicker){date = it}},
            label = {Text("Data")},
            readOnly = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
                .height(56.dp)
                .background(Color.White, RoundedCornerShape(30.dp))
                .onFocusChanged { focusState -> if(focusState.isFocused){
                showDatePicker = true}
                }
        )
        if(showDatePicker){
            Log.d("qui ci entri=?", "$showDatePicker")
            DateTimeDialog(state = rememberUseCaseState(visible = true),
                selection = DateTimeSelection.Date{
                        newDate -> selectedDate.value = newDate
                    val localDate = newDate.toEpochDay()
                   date = dateFormat.format(localDate)
                   // date = dateFormat.format(newDate)
                    Log.d("newDate", "$newDate")
                    Log.d("selectedDate.value", "${selectedDate.value}")
                    showDatePicker = false
                })
        }



        OutlinedTextField(
            value = certificateName,
            onValueChange = {certificateName = it},
            label = {Text("Nome del certificato")},
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
                .height(56.dp)
                .background(Color.White, RoundedCornerShape(30.dp))
        )
        OutlinedTextField(
            value = additionalInfo,
            onValueChange = {additionalInfo = it},
            label = {Text("Informazioni aggiuntive")},
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
                .height(56.dp)
                .background(Color.White, RoundedCornerShape(30.dp))
        )
        Button(onClick = {
            coroutineScope.launch {
                         viewModel.updateSportAchievements(selectedSport, 1, savedData)
            }
            //salva i dati qui
        }, modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
            .height(56.dp)
            .background(Color.White, RoundedCornerShape(16.dp))
        ) {
            Text(text = "Salva")
        }
    }
}