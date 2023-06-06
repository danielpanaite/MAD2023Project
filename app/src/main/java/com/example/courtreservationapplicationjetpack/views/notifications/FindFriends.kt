package com.example.courtreservationapplicationjetpack.views.notifications

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.courtreservationapplicationjetpack.CourtTopAppBar
import com.example.courtreservationapplicationjetpack.components.BottomBar
import com.example.courtreservationapplicationjetpack.firestore.Notification
import com.example.courtreservationapplicationjetpack.firestore.NotificationViewModel
import com.example.courtreservationapplicationjetpack.firestore.UserViewModel
import com.example.courtreservationapplicationjetpack.navigation.NavigationDestination
import com.example.courtreservationapplicationjetpack.signIn.GoogleAuthUiClient
import com.example.courtreservationapplicationjetpack.ui.theme.GreyItemInactive
import com.example.courtreservationapplicationjetpack.views.profile.FriendsItem
import com.google.firebase.Timestamp

object FindFriendsDestination : NavigationDestination {
    override val route = "find_friends"
    override val titleRes = "Find Friends"
    override val icon = Icons.Default.Home
}

@Composable
fun FindFriends(
    navController: NavController,
    modifier: Modifier = Modifier,
    googleAuthUiClient : GoogleAuthUiClient,
    navigateBack: () -> Unit
){
    Scaffold(
        topBar = {
            CourtTopAppBar(canNavigateBack = true, navigateUp = navigateBack, text = "Find Friends")
        },
        bottomBar = { BottomBar(navController = navController as NavHostController) }

    ) {
            innerPadding ->
        FindFriendsBody(
            modifier = modifier.padding(innerPadding),
            googleAuthUiClient = googleAuthUiClient
        )
    }
}

@Composable
fun FindFriendsBody(
    modifier: Modifier = Modifier,
    googleAuthUiClient : GoogleAuthUiClient,
    userViewModel: UserViewModel = viewModel(),
    notificationViewModel: NotificationViewModel = viewModel(),
){
    val email = googleAuthUiClient.getSignedInUser()?.email
    if(email != null)
        userViewModel.getUserByEmail(email)
    if(email != null) {
        notificationViewModel.getUserSentFriendRequests(email)
    }
    if(userViewModel.user.value.id != "" && email != null && notificationViewModel.sentrequests.value != null){
        val friends: MutableList<String> = mutableListOf(*userViewModel.user.value.friends.toTypedArray())
        friends.add(email)
        userViewModel.getUsersNotFriends(friends, notificationViewModel.sentrequests.value!!)
    }
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 16.dp)
    ) {
        if(email!= null){
            item {
                AddFriendsItem(email)
            }
        }
        if(userViewModel.users.value.isNotEmpty() && email != null) {
            items(userViewModel.users.value) { f ->
                FriendsItem(friend = f, true, email = email)
            }
        }else{
            item {
                Text(
                    text = "No friends available!",
                    style = MaterialTheme.typography.headlineMedium,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
fun AddFriendsItem(
    email: String,
    notificationViewModel: NotificationViewModel = viewModel(),
){
    val toastFriend = Toast.makeText(LocalContext.current, "Friend request sent!", Toast.LENGTH_SHORT)
    val nicknameState = remember { mutableStateOf("") }
    Card(modifier = Modifier
        .fillMaxWidth()
        .padding(start = 16.dp, end = 16.dp)
        .border(
            BorderStroke(1.dp, MaterialTheme.colorScheme.primaryContainer),
            MaterialTheme.shapes.small
        )
    ){
        Row(modifier = Modifier
            .padding(start = 16.dp, top = 4.dp, bottom = 4.dp),
            horizontalArrangement = Arrangement.Center
        ){
            Text(text = "Invite a user with their nickname", color = GreyItemInactive, textAlign = TextAlign.Center)
        }
        Row(modifier = Modifier
            .padding(start = 16.dp, top = 8.dp, bottom = 8.dp)
        ){
            TextField(value = nicknameState.value, onValueChange = { nicknameState.value = it }, modifier = Modifier.fillMaxWidth())
        }
        Row(modifier = Modifier
            .padding(start = 16.dp, top = 8.dp, bottom = 8.dp)
        ){
            Column(modifier = Modifier
                .fillMaxSize()
                .weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(onClick = {
                    notificationViewModel.sendFriendRequestByNickname(
                        Notification(
                            sender = email,
                            type = "friend",
                            status = "pending",
                            date = Timestamp.now()
                        ), nicknameState.value)
                    toastFriend.show()
                }) {
                    Text("Send friend request")
                }
            }
        }
    }
}