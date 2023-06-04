package com.example.courtreservationapplicationjetpack.views.notifications

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Star
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
import com.example.courtreservationapplicationjetpack.firestore.Notification
import com.example.courtreservationapplicationjetpack.firestore.NotificationViewModel
import com.example.courtreservationapplicationjetpack.firestore.UserViewModel
import com.example.courtreservationapplicationjetpack.firestore.Users
import com.example.courtreservationapplicationjetpack.firestore.toDate
import com.example.courtreservationapplicationjetpack.firestore.toTime
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
    userViewModel: UserViewModel = viewModel()
){
    val email = googleAuthUiClient.getSignedInUser()?.email
    if(email != null)
        viewModel.getUserNotifications(email)
    if(viewModel.notifications.value.isNotEmpty())
        userViewModel.getUserListByEmails(viewModel.notifications.value.map { it.sender })
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 16.dp)
    ) {
        if(viewModel.notifications.value.isNotEmpty() && userViewModel.users.value.isNotEmpty())
            for( i in viewModel.notifications.value.indices){
                if(viewModel.notifications.value[i].status == "pending")
                    item{
                        NotificationItem(
                            notification = viewModel.notifications.value[i],
                            sender = userViewModel.users.value.first { it.email == viewModel.notifications.value[i].sender }
                        )
                    }
            }
    }
}

@Composable
fun NotificationItem(
    notification: Notification,
    sender: Users,
    viewModel: NotificationViewModel = viewModel(),
    userViewModel: UserViewModel = viewModel()
){
    val toastFriend = Toast.makeText(LocalContext.current, "Friend added!", Toast.LENGTH_SHORT)
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
                    else
                        Icon(Icons.Default.Star, contentDescription = "Play", modifier = Modifier.size(40.dp))
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
        Divider(thickness = 1.dp, color = MaterialTheme.colorScheme.primaryContainer)
        Divider(thickness = 1.dp, color = MaterialTheme.colorScheme.primaryContainer)
        Surface(color = Color.White){
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(end = 16.dp)
            ) {
                IconButton(onClick = {
                    viewModel.updateNotificationStatus(notification.id, "confirmed")
                    userViewModel.addFriend(notification.receiver, notification.sender)
                    toastFriend.show()
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