package com.example.courtreservationapplicationjetpack.views.courts

import android.annotation.SuppressLint
import android.media.MediaPlayer
import android.util.Log
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonDefaults.textButtonColors
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.courtreservationapplicationjetpack.CourtTopAppBar
import com.example.courtreservationapplicationjetpack.components.BottomBar
import com.example.courtreservationapplicationjetpack.navigation.NavigationDestination
import com.example.courtreservationapplicationjetpack.views.reservations.ReservationDetailsDestination
import com.example.courtreservationapplicationjetpack.ui.appViewModel.AppViewModelProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import androidx.test.core.app.ActivityScenario.launch
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.chargemap.compose.numberpicker.NumberPicker
import com.example.courtreservationapplicationjetpack.R
import com.example.courtreservationapplicationjetpack.firestore.CourtViewModel
import com.example.courtreservationapplicationjetpack.firestore.Reservation
import com.example.courtreservationapplicationjetpack.firestore.ReservationViewModel
import com.example.courtreservationapplicationjetpack.firestore.UserViewModel
import com.example.courtreservationapplicationjetpack.models.courts.Court
import com.example.courtreservationapplicationjetpack.signIn.GoogleAuthUiClient
import com.example.courtreservationapplicationjetpack.signIn.SignInDestination
import com.example.courtreservationapplicationjetpack.views.courts.CourtsAvailableDestination.hourOptArg
import com.example.courtreservationapplicationjetpack.views.reservations.MyReservationsDestination
import com.google.firebase.Timestamp
import com.maxkeppeker.sheets.core.models.base.UseCaseState
import com.maxkeppeker.sheets.core.models.base.rememberUseCaseState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Date
import java.util.Locale
import java.util.TimeZone

object CourtsAvailableDestination : NavigationDestination {
    override val route  = "my_courts"
    override val titleRes = "My Courts"
    override val icon = Icons.Default.Star
    private const val courtID = "courtID"
    private const val dateArg = "dateArg"
    private const val hourOptArg = "hourOptArg"
    val routeWithArgs = "$route/{$courtID}/{$dateArg}?hourOptArg={$hourOptArg}"
}



@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@ExperimentalMaterial3Api
@Composable
fun CourtsAvailable(
    courtID: String,
    pickedDate: String,
    hourOptArg: String,
    navController: NavController,
    modifier: Modifier = Modifier,
    navigateToCourtReservation: (Int) -> Unit,
    googleAuthUiClient: GoogleAuthUiClient,
    viewModel: CourtsAvailableViewModel = viewModel(factory = AppViewModelProvider.Factory),
    allSportViewModel: AllSportsViewModel = viewModel(factory = AppViewModelProvider.Factory),
) {
    val courtsAvailableUiState by viewModel.courtsAvailableUiState.collectAsState()
    val selectedDate = remember { mutableStateOf(LocalDate.parse(pickedDate)) }
    val isMissingSomething = remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    var pickedHour = remember { mutableStateOf(hourOptArg) }
    val (pickedPeople, setPickedPeople) = remember { mutableStateOf("1") }
    val (additionsText, setAdditionsText) = remember { mutableStateOf("") }
    val showDialog = remember { mutableStateOf(false) }
    val firebaseReservationViewModel: ReservationViewModel = viewModel()
    val reservationDetails by remember { mutableStateOf(firebaseReservationViewModel.reservation) }
    val userViewModel: UserViewModel = viewModel()

    val email = googleAuthUiClient.getSignedInUser()?.email


    LaunchedEffect(selectedDate.value){
        pickedHour.value = pickedHour.value
        Log.d("Pick", "Picked hour: ${pickedHour.value}")
    }

    Scaffold(
        topBar = {
            //CourtTopAppBar(canNavigateBack = false)
        },
        //bottomBar = { BottomBar(navController = navController as NavHostController) }
        bottomBar = {
            Row(modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
                .padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Button(
                    onClick = {
                        if(pickedHour.value.isNotEmpty()) {
                            isMissingSomething.value = false
                            coroutineScope.launch {
//                                viewModel.addReservation(
//                                    null,
//                                    "1",
//                                    courtID,
//                                    selectedDate.value.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
//                                        .toString(),
//                                    pickedHour.value,
//                                    additionsText,
//                                    pickedPeople
//                                )
                                firebaseReservationViewModel.insertReservation(reservationDetails.value)
                                showDialog.value = true
                            }
                        }
                        else{
                            //TODO: show error
                            isMissingSomething.value = true
                        }

                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp)
                ) {
                    Text(text = "Save")
                }
            }

        }

    ) {
            _ ->
        if(isMissingSomething.value) {
            AlertDialog(
                onDismissRequest = { /* Azione da eseguire quando si chiude il popup */ },
                title = {
                    Text(
                        text = "Error while saving",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = androidx.compose.ui.text.TextStyle(
                            fontSize = 16.sp,
                            textAlign = TextAlign.Center
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                text = {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
//                        Box(
//                            contentAlignment = Alignment.Center,
//                            modifier = Modifier
//                                .size(64.dp)
//                                .padding(top = 4.dp)
//                        ) {
//                            Icon(
//                                painter = painterResource(R.drawable.ic_error),
//                                contentDescription = "Error Icon",
//                                tint = Color.Red
//                            )
//                        }
                        Box(
                            modifier = Modifier
                                .height(200.dp)
                                .padding(bottom = 8.dp)
                                .border(
                                    width = 2.dp,
                                    color = Color.Gray,
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .background(Color.Red.copy(alpha = 0.1f))
                        ) {
                            val context = LocalContext.current
                            val mediaPlayer = remember { MediaPlayer.create(context, R.raw.fail_sound) }

                            DisposableEffect(Unit) {
                                onDispose {
                                    mediaPlayer.release()
                                }
                            }

                            LaunchedEffect(Unit) {
                                mediaPlayer.setVolume(0.3f, 0.3f)
                                mediaPlayer.start()
                            }
                            val composition = rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.save_failure))
                            val progress by animateLottieCompositionAsState(
                                composition = composition.value,
                                iterations = LottieConstants.IterateForever
                            )
                            LottieAnimation(
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.FillBounds,
                                composition = composition.value,
                                progress = { progress },
                                maintainOriginalImageBounds = true
                            )
                        }
                        Text(
                            text = "Make sure you have entered your reservation slot",
                            modifier = Modifier.padding(top = 8.dp),
                            style = androidx.compose.ui.text.TextStyle(
                                fontSize = 14.sp,
                                textAlign = TextAlign.Center,
                            )
                        )
                    }
                },
                confirmButton = {
//                    Button(
//                        onClick = {
//                            /* Azione da eseguire quando si fa clic sul pulsante "Show in Calendar" */
//                            navController.navigate(MyReservationsDestination.route)
//                        }
//                    ) {
//                        Text("Show in Calendar")
//                    }
                },
                dismissButton = {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Button(
                            onClick = {
                                /* Azione da eseguire quando si fa clic sul pulsante "Close" */
                                isMissingSomething.value = false
                                //navController.popBackStack()
                            },
                            modifier = Modifier
                                .sizeIn(minWidth = 120.dp, minHeight = 48.dp)
                        ) {
                            Text("Close")
                        }
                    }
                }
            )
        }

        Ciao(
            courtID = courtID,
            viewModel = viewModel,
            selectedDate = selectedDate,
            pickedHour = pickedHour,
            setPickedPeople = setPickedPeople,
            setAdditionsText = setAdditionsText,
            additionsText = additionsText,
            showDialog = showDialog,
            navController = navController,
            allSportViewModel = allSportViewModel,
            reservationDetails = reservationDetails,
            userEmail = email,
            pickedPeople = pickedPeople
        )
//        CourtsBody(
//            courtList = courtsAvailableUiState.courtsAvailableList,
//            modifier = modifier.padding(innerPadding),
//            onCourtClick = navigateToCourtReservation,
//        )
    }

}

@Composable
fun CourtsBody(
    courtList: List<Court>,
    modifier: Modifier = Modifier,
    onCourtClick: (Int) -> Unit,

    ){

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        //ReservationsListHeader()
        //Divider()
        if (courtList.isEmpty()) {
            Text(
                text = "no court for this sport in the db",
                style = MaterialTheme.typography.bodySmall //trovare subtitle
            )
        } else {
            CourtList(
                courtList = courtList,
                onCourtClick = { onCourtClick(it.id) }
            )
        }
    }
}

@Composable
private fun CourtList(
    courtList: List<Court>,
    modifier: Modifier = Modifier,
    onCourtClick: (Court) -> Unit,
) {
    LazyColumn(modifier = modifier, verticalArrangement = Arrangement.spacedBy(8.dp)) {
        items(items = courtList, //key = { it.id }
        ) { court ->
            CourtItem(court = court,
                //navigateToDetailsReservation = navigateToDetailsReservation
                onCourtClick = onCourtClick
            )
            Divider()
        }
    }
}


@Composable
private fun CourtItem(
    court: Court,
    modifier: Modifier = Modifier,
    onCourtClick: (Court) -> Unit,

    ) {
    Row(modifier = modifier
        .fillMaxWidth()
        .clickable {
            onCourtClick(court)
        }
        .padding(vertical = 16.dp)
    ) {
        Text(text = court.name,  modifier = Modifier.weight(0.7f), fontWeight = FontWeight.Bold)


    }
}

//--------------------------------------------------------------------------------
@SuppressLint("CoroutineCreationDuringComposition", "UnrememberedMutableState")
@Composable
fun Ciao(
    courtID: String,
    viewModel: CourtsAvailableViewModel,
    selectedDate: MutableState<LocalDate>,
    pickedHour: MutableState<String>,
    setPickedPeople: (String) -> Unit,
    setAdditionsText: (String) -> Unit,
    additionsText: String,
    showDialog: MutableState<Boolean>,
    navController: NavController,
    allSportViewModel: AllSportsViewModel,
    reservationDetails: MutableState<Reservation>,
    userEmail: String?,
    pickedPeople: String
) {
    val firebaseCourtViewModel: CourtViewModel = viewModel()
    val courtState = remember { mutableStateOf<com.example.courtreservationapplicationjetpack.firestore.Court?>(null) }


    LaunchedEffect(Unit) {
        firebaseCourtViewModel.getCourtById(courtID)
    }

    LaunchedEffect(firebaseCourtViewModel.court.value) {
        courtState.value = firebaseCourtViewModel.court.value
    }


    val composition = rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.save_success))
    val progress by animateLottieCompositionAsState(
        composition = composition.value,
        iterations = LottieConstants.IterateForever
    )



    if(showDialog.value) {
        AlertDialog(
            onDismissRequest = { TODO() },
            title = {
                Text(
                    text = "Reservation Correctly Saved",
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = androidx.compose.ui.text.TextStyle(
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .height(200.dp)
                            .fillMaxWidth()
                            .padding(bottom = 8.dp)
                            .border(
                                width = 2.dp,
                                color = Color.Gray,
                                shape = RoundedCornerShape(8.dp)
                            )
                            .background(Color.Green.copy(alpha = 0.1f))
                    ) {
                        val context = LocalContext.current
                        val mediaPlayer = remember { MediaPlayer.create(context, R.raw.success_sound) }

                        DisposableEffect(Unit) {
                            onDispose {
                                mediaPlayer.release()
                            }
                        }

                        LaunchedEffect(Unit) {
                            mediaPlayer.start()
                        }

                        LottieAnimation(
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.FillBounds,
                            composition = composition.value,
                            progress = { progress },
                            maintainOriginalImageBounds = true
                        )
                    }
                    Text(
                        text = "Hai salvato correttamente la tua prenotazione",
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        style = androidx.compose.ui.text.TextStyle(
                            fontSize = 16.sp,
                            textAlign = TextAlign.Center
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        navController.navigate(MyReservationsDestination.route)
                    }
                ) {
                    Text("Show in Calendar")
                }
            },
            dismissButton = {
                Button(
                    onClick = {
                        navController.popBackStack()
                    }
                ) {
                    Text("Close")
                }
            }
        )




    }

//    LaunchedEffect(Unit) {
//        viewModel.getCourt(courtID.toInt()).collect { courtValue ->
//            courtState.value = courtValue
//        }
//    }
//
//    val court = courtState.value

    val lazyListState = rememberLazyListState()
    val firstItemTranslationY by remember {
        derivedStateOf {
            when {
                lazyListState.layoutInfo.visibleItemsInfo.isNotEmpty() &&
                        lazyListState.firstVisibleItemIndex == 0 ->
                    lazyListState.firstVisibleItemScrollOffset * .7f
                else -> 0f
            }
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
//            .clip(
//                RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp)
//            ),
        ,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        state = lazyListState
    ) {
        item {
            Box(modifier = Modifier.fillMaxWidth()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .background(Color.Red)
                        .align(Alignment.TopCenter)
                )
                var imageUrl = when (courtState.value?.sport) {
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
                if(!courtState.value?.URL.isNullOrEmpty()){
                    imageUrl = courtState.value?.URL!!
                }

                AsyncImage(
                    modifier = Modifier.fillMaxWidth(),
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(imageUrl)
                        .crossfade(true)
                        .build(),
                    placeholder = painterResource(R.drawable.placeholder),
                    contentDescription = "Court Image",
                    contentScale = ContentScale.Crop,
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            brush = Brush.verticalGradient(
                                0f to Color.Transparent,
                                0.0001f to Color.Transparent,
                                1f to Color.Black
                            )
                        )
                    //.clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
                )
            }
        }

        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(y = (-90).dp)
                    .background(
                        color = MaterialTheme.colorScheme.surface,
                        shape = MaterialTheme.shapes.large.copy(
                            topStart = CornerSize(20.dp),
                            topEnd = CornerSize(20.dp)
                        )
                    )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(3f)) {
                            Text(
                                text = "${courtState.value?.name}",
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "${courtState.value?.address}",
                                style = MaterialTheme.typography.labelSmall,
                                color = Color.Gray,
                                textAlign = TextAlign.Start
                            )
                        }
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .align(Alignment.CenterVertically)
                        ) {
                            val sportIcon = when (courtState.value?.sport) {
                                "calcio" -> R.drawable.ic_calcio5
                                "basket" -> R.drawable.ic_basket
                                "beach volley" -> R.drawable.ic_beachvolley
                                "pallavolo" -> R.drawable.ic_volley
                                "tennis" -> R.drawable.ic_tennis
                                "pallamano" -> R.drawable.pallamano
                                "rugby" -> R.drawable.ic_rugby
                                "softball" -> R.drawable.ic_softball
                                else ->  R.drawable.ic_question_mark
                            }

                            Image(
                                painter = painterResource(sportIcon),
                                contentDescription = "Sport icon",
                                colorFilter = ColorFilter.tint(Color.Black),
                                modifier = Modifier
                                    .size(29.dp)
                                    .align(Alignment.Center)
                            )
                        }
                    }

                    Divider(
                        color = Color.LightGray,
                        thickness = 1.dp,
                        modifier = Modifier
                            .padding(top = 8.dp)
                            .height(1.dp)
                            .shadow(elevation = 4.dp, shape = MaterialTheme.shapes.medium)
                    )

                    CalendarScreen(selectedDate, pickedHour)

                    val currentTime = LocalTime.now()

                    val filteredHours = listOf("08:00", "09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00")
                    Log.d("Oggi", LocalDate.now().toString())
                    Log.d("SelectedDate", selectedDate.value.toString())

                    var filteredHoursForToday = filteredHours

                    if(filteredHours.isNotEmpty()) {
                        filteredHoursForToday =
                            if (LocalDate.now().toString() == selectedDate.value.toString()) {
                                filteredHours.filter { LocalTime.parse(it) > currentTime }
                            } else {
                                filteredHours
                            }
                    }

                    if(courtState.value != null) {
                        TextGrid(
                            pickedHour,
                            filteredHoursForToday,
                            selectedDate.value,
                            allSportViewModel,
                            courtState.value!!.id,
                        )
                    }


                    Row(
                        modifier = Modifier
                            .padding(horizontal = 0.dp, vertical = 8.dp)
                        //.padding(8.dp)
                    ) {
                        var pickerValue by remember { mutableStateOf(1) }
                        setPickedPeople(pickerValue.toString())
                        Text(
                            text = "Numero di persone coinvolte: $pickerValue",
                            style = MaterialTheme.typography.labelMedium,
                            color = Color.Gray,
                            modifier = Modifier
                                .weight(1f)
                                .align(Alignment.CenterVertically)
                        )

                        NumberPicker(
                            modifier = Modifier
                                .width(120.dp)
                                .align(Alignment.CenterVertically),
                            value = pickerValue,
                            range = 0..10,
                            onValueChange = {
                                pickerValue = it
                                setPickedPeople(it.toString())
                            },
                            dividersColor = Color.Black.copy(alpha = 0.7f),
                        )


                    }
                    Row {
                        AdditionsInput(setAdditionsText,onAdditionsChanged = {})
                    }

                    if(pickedHour.value.isNotEmpty()) {
                        val dateInTimeZone =
                            selectedDate.value.atTime(LocalTime.parse(pickedHour.value))
                                .atZone(ZoneId.of("Europe/Rome")).toInstant().epochSecond

                        if (courtState.value != null && userEmail != null) {
                            reservationDetails.value = reservationDetails.value.copy(
                                id = courtID,
                                date = Timestamp(dateInTimeZone, 0),
                                user = userEmail!!,
                                notes = additionsText,
                                people = pickedPeople.toInt(),
                                court = courtState.value!!.id
                            )
                        }
                    }


                }
            }
        }
    }
}
@Composable
fun CalendarScreen(selectedDate: MutableState<LocalDate>, pickedHour: MutableState<String>) {
    val scrollState = rememberScrollState()
    val startDate = LocalDate.now()


    Column {
        Row(
            modifier = Modifier
                .horizontalScroll(scrollState)
                .padding(horizontal = 0.dp, vertical = 8.dp)
        ) {
            val daysToShow = 100
            repeat(daysToShow) { index ->
                val currentDate = startDate.plusDays(index.toLong())
                val dayOfWeek =
                    currentDate.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault())
                val dayOfMonth = currentDate.dayOfMonth
                val month = currentDate.month.getDisplayName(TextStyle.SHORT, Locale.getDefault())

                DayButton(
                    dayOfWeek = dayOfWeek,
                    dayOfMonth = dayOfMonth,
                    month = month,
                    isSelected = selectedDate.value == currentDate,
                    onDaySelected = {
                        if(selectedDate.value != currentDate) {
                            pickedHour.value = ""
                        }
                        selectedDate.value = currentDate
                    }
                )
            }
        }
        Row(
            modifier = Modifier.padding(horizontal = 0.dp, vertical = 8.dp)
        ) {
            Text(
                text = "Timeslot disponibili per il giorno ${
                    selectedDate.value.format(
                        DateTimeFormatter.ofPattern("dd/MM/yyyy")
                    )
                }",
                style = MaterialTheme.typography.labelMedium,
                color = Color.Gray
            )
        }


    }
}


@Composable
fun TextGrid(pickedHour: MutableState<String>, textList: List<String>, selectedDate: LocalDate, allSportViewModel: AllSportsViewModel, courtID: String) {

    Log.d("pickedHour", pickedHour.value)


    val slotsState: MutableState<List<String>> = remember { mutableStateOf(emptyList()) }
    if (courtID != null) {
        val firebaseReservationViewModel: ReservationViewModel = androidx.lifecycle.viewmodel.compose.viewModel()

        val dateInTimeZone = selectedDate.atStartOfDay(ZoneId.of("Europe/Rome")).toInstant().epochSecond
        firebaseReservationViewModel.getCourtReservations(courtID, Timestamp(dateInTimeZone, 0))

        val slots = firebaseReservationViewModel.courtres.value.map { reservation ->
            val date = Date(reservation.date.seconds * 1000 + reservation.date.nanoseconds / 1000000)

            val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
            timeFormat.timeZone = TimeZone.getTimeZone(ZoneId.of("Europe/Rome"))

            timeFormat.format(date)
        }
        slotsState.value = slots
    }

    val rows = (textList.size + 4) / 5 // Calcola il numero di righe necessarie per visualizzare tutti gli elementi
    val selectedButtonIndex = remember {
        mutableStateOf(textList.indexOfFirst { it == pickedHour.value })
    }

    println("slotsState" + slotsState.value)

    val hoursList = if (slotsState.value.isNotEmpty()) {
        textList - slotsState.value.toSet()
    } else {
        textList
    }
    println("hoursList $hoursList")

    Column {
        repeat(rows) { rowIndex ->
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 0.dp)) {
                for (columnIndex in 0 until 5) {
                    val index = rowIndex * 5 + columnIndex
                    if (index < hoursList.size) {
                        val isSelected = hoursList[index] == pickedHour.value

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(50.dp)
                                .padding(4.dp)
                                .clip(
                                    if (isSelected) MaterialTheme.shapes.small else MaterialTheme.shapes.small
                                )
                                .background(if (isSelected) Color.Black else Color.Transparent)
                                .border(
                                    BorderStroke(1.dp, if (isSelected) Color.Black else Color.Gray),
                                    shape = MaterialTheme.shapes.small
                                )
                                .clickable {
                                    pickedHour.value = hoursList[index]
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = hoursList[index],
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isSelected) Color.White else Color.Black
                                ),
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                            )
                        }
                    } else {
                        Spacer(
                            modifier = Modifier
                                .weight(1f)
                                .aspectRatio(1f)
                                .padding(2.dp)
                        )
                    }
                }
            }
        }
    }
}







@Composable
fun DayButton(
    dayOfWeek: String,
    dayOfMonth: Int,
    month: String,
    isSelected: Boolean,
    onDaySelected: () -> Unit
) {
    val textColor = animateTextColor(isSelected)
    val circleColor = animateCircleColor(isSelected)

    Column(
        modifier = Modifier
            .padding(horizontal = 2.dp)
            .height(90.dp)
            .width(50.dp)
            .background(Color.Transparent, RoundedCornerShape(8.dp))
            .clickable(onClick = onDaySelected),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = dayOfWeek.uppercase(Locale.getDefault()),
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold,
            color = circleColor
        )
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(if (isSelected) Color.Black else Color.Transparent, shape = CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = dayOfMonth.toString(),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = textColor
            )
        }
        Text(
            text = month,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold,
            color = circleColor
        )
    }
}
@Composable
private fun animateTextColor(isSelected: Boolean): Color {
    return animateColorAsState(
        targetValue = if (isSelected) Color.White else Color.Gray,
        animationSpec = tween(durationMillis = 200) // Specify the desired duration
    ).value
}

@Composable
private fun animateCircleColor(isSelected: Boolean): Color {
    return animateColorAsState(
        targetValue = if (isSelected) Color.Black else Color.Gray,
        animationSpec = tween(durationMillis = 200) // Specify the desired duration
    ).value
}
//--------------------------------------------------------------------------------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdditionsInput(setAdditionsText: (String) -> Unit, onAdditionsChanged: (String) -> Unit) {
    var additionsText by remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(top = 0.dp)) {
        Text(
            text = "Additions",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 0.dp)
        )

        TextField(
            value = additionsText,
            onValueChange = { text ->
                additionsText = text
                setAdditionsText(text)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 0.dp, vertical = 8.dp)
                .background(MaterialTheme.colorScheme.surface),
            textStyle = MaterialTheme.typography.bodyMedium,
            placeholder = { Text(text = "Enter additions here") },
            maxLines = 3,
            singleLine = false,
            colors = TextFieldDefaults.textFieldColors(
                containerColor = MaterialTheme.colorScheme.surface,
                focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                unfocusedIndicatorColor = Color.Gray
            ),
        )
    }
}






@Preview(showBackground = true)
@Composable
fun preview(){
    //AdditionsInput(onAdditionsChanged = {})
}