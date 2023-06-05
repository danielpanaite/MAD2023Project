package com.example.courtreservationapplicationjetpack.views.notifications

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
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
import com.example.courtreservationapplicationjetpack.firestore.Court
import com.example.courtreservationapplicationjetpack.firestore.CourtViewModel
import com.example.courtreservationapplicationjetpack.firestore.Notification
import com.example.courtreservationapplicationjetpack.firestore.NotificationViewModel
import com.example.courtreservationapplicationjetpack.firestore.ReservationViewModel
import com.example.courtreservationapplicationjetpack.firestore.UserViewModel
import com.example.courtreservationapplicationjetpack.firestore.Users
import com.example.courtreservationapplicationjetpack.firestore.toDate
import com.example.courtreservationapplicationjetpack.firestore.toTime
import com.example.courtreservationapplicationjetpack.models.sport.SportDrawables
import com.example.courtreservationapplicationjetpack.navigation.NavigationDestination
import com.example.courtreservationapplicationjetpack.signIn.GoogleAuthUiClient
import com.example.courtreservationapplicationjetpack.ui.theme.ClearRed
import com.example.courtreservationapplicationjetpack.ui.theme.ConfirmGreen

object NotificationScreenDestination : NavigationDestination {
    override val route = "notification"
    override val titleRes = "Notifications"
    override val icon = Icons.Default.Notifications
}

@Composable
fun Notifications(
    navController: NavController,
    modifier: Modifier = Modifier,
    googleAuthUiClient : GoogleAuthUiClient
){

    Scaffold(
        topBar = {
            CourtTopAppBar(canNavigateBack = false, text = "Notifications")
        },
        bottomBar = { BottomBar(navController = navController as NavHostController) }

    ) {
            innerPadding ->
        NotificationsBody(
            modifier = modifier.padding(innerPadding),
            googleAuthUiClient = googleAuthUiClient
        )
    }

}

@Composable
fun NotificationsBody(
    modifier: Modifier = Modifier,
    googleAuthUiClient : GoogleAuthUiClient,
    viewModel: NotificationViewModel = viewModel(),
    userViewModel: UserViewModel = viewModel(),
    courtViewModel: CourtViewModel = viewModel(),
){
    val email = googleAuthUiClient.getSignedInUser()?.email
    var courts = emptyList<String>()
    if(email != null)
        viewModel.getUserNotifications(email)
    if(viewModel.notifications.value.isNotEmpty() && viewModel.notifications.value.filter { it.court != "" }.distinctBy{it.court}.isNotEmpty()) {
        courts = viewModel.notifications.value.filter { it.court != "" }.distinctBy{it.court}.map { it.court }
        courtViewModel.getReservationCourts(courts)
    }
    if(viewModel.notifications.value.isNotEmpty() && viewModel.notifications.value.distinctBy{it.sender}.isNotEmpty()) {
        val users = viewModel.notifications.value.distinctBy { it.sender }.map { it.sender }
        userViewModel.getUserListByEmails(users)
    }
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 16.dp)
    ) {
        Log.d("COURTS", courts.toString())
        if(courts.isNotEmpty()) {
            if (viewModel.notifications.value.isNotEmpty() &&
                userViewModel.users.value.isNotEmpty() &&
                courtViewModel.reservationcourts.value.isNotEmpty() &&
                (viewModel.notifications.value.distinctBy { it.sender }.size == userViewModel.users.value.size) &&
                (viewModel.notifications.value.filter { it.court != "" }
                    .distinctBy { it.court }.size == courtViewModel.reservationcourts.value.size)
            )
                for (i in viewModel.notifications.value.indices) {
                    item {
                        NotificationItem(
                            notification = viewModel.notifications.value[i],
                            sender = userViewModel.users.value.first { it.email == viewModel.notifications.value[i].sender },
                            court = courtViewModel.reservationcourts.value.firstOrNull { it.id == viewModel.notifications.value[i].court }
                        )
                    }
                }
        }
        else {
            if (viewModel.notifications.value.isNotEmpty() &&
                userViewModel.users.value.isNotEmpty() &&
                (viewModel.notifications.value.distinctBy { it.sender }.size == userViewModel.users.value.size)
            )
                for (i in viewModel.notifications.value.indices) {
                    item {
                        NotificationItem(
                            notification = viewModel.notifications.value[i],
                            sender = userViewModel.users.value.first { it.email == viewModel.notifications.value[i].sender },
                            court = null
                        )
                    }
                }
        }
    }
}

@Composable
fun NotificationItem(
    notification: Notification,
    sender: Users,
    court: Court?,
    viewModel: NotificationViewModel = viewModel(),
    userViewModel: UserViewModel = viewModel(),
    reservationViewModel: ReservationViewModel = viewModel()
){
    val toastFriend = Toast.makeText(LocalContext.current, "Friend added!", Toast.LENGTH_SHORT)
    val toastInvite = Toast.makeText(LocalContext.current, "Invitation accepted!", Toast.LENGTH_SHORT)
    Card(modifier = Modifier
        .fillMaxWidth()
        .padding(start = 16.dp, end = 16.dp),
        elevation = CardDefaults.cardElevation( 8.dp )
    ){
        Surface(color = Color.White){
            Row(modifier = Modifier
                .padding(start = 16.dp)
            ){
                Column(modifier = Modifier
                    .fillMaxSize()
                    .align(Alignment.CenterVertically)
                    .weight(1f)
                ){
                    if(notification.type == "friend")
                        Icon(Icons.Default.Face, contentDescription = "Friend", modifier = Modifier.size(40.dp))
                    else if(court != null)
                        Image(
                            painter = painterResource(SportDrawables.getDrawable(court.sport)),
                            contentDescription = "Sport icon",
                            colorFilter = ColorFilter.tint(Color.Black),
                            modifier = Modifier.size(40.dp)
                        )
                }
                Column(modifier = Modifier
                    .fillMaxSize()
                    .weight(5f)
                ){
                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp, bottom = 8.dp)
                    ){
                        Text(text = "${notification.toDate()} ${notification.toTime()}", fontWeight = FontWeight.ExtraLight)
                    }
                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp, bottom = 16.dp)
                    ){
                        if(notification.type == "friend")
                            Text(text = "Friend request from ${sender.nickname}")
                        else
                            Text(text = "Invite from ${sender.nickname}")
                    }
                }
            }
        }
        if(court != null) {
            Divider(thickness = 1.dp, color = MaterialTheme.colorScheme.primaryContainer)
            Surface(color = Color.White) {
                Row(
                    modifier = Modifier
                        .padding(16.dp)
                        .height(80.dp)
                ) {
                    Card(
                        modifier = Modifier
                            .fillMaxSize()
                            .weight(2f)
                    ) {
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(court.URL)
                                .crossfade(true)
                                .build(),
                            placeholder = painterResource(R.drawable.placeholder),
                            contentDescription = "Court Image",
                            contentScale = ContentScale.Crop,
                        )
                    }
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(6f)
                            .align(Alignment.CenterVertically)
                            .padding(start = 8.dp)
                    ) {
                        Text(
                            text = court.name,
                            textAlign = TextAlign.Start,
                            fontWeight = FontWeight.Normal,
                        )
                        Text(
                            text = court.address,
                            textAlign = TextAlign.Start,
                            fontWeight = FontWeight.ExtraLight,
                        )
                    }
                }
            }
        }
        Divider(thickness = 1.dp, color = MaterialTheme.colorScheme.primaryContainer)
        Surface(color = Color.White){
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(end = 16.dp)
            ) {
                IconButton(onClick = {
                    viewModel.updateNotificationStatus(notification.id, "confirmed")
                    if(notification.type == "friend") {
                        userViewModel.addFriend(notification.receiver, notification.sender)
                        toastFriend.show()
                    }else{
                        reservationViewModel.acceptInvitation(notification.reservation, notification.receiver)
                        toastInvite.show()
                    }
                                     }, modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)) {
                    Icon(Icons.Default.Check, contentDescription = "Check", tint = ConfirmGreen)
                }
                IconButton(onClick = {
                    viewModel.updateNotificationStatus(notification.id, "declined") }, modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)) {
                    Icon(Icons.Default.Clear, contentDescription = "Clear", tint = ClearRed)
                }
            }
        }
    }
}