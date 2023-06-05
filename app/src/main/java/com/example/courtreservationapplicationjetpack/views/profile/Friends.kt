package com.example.courtreservationapplicationjetpack.views.profile

import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.courtreservationapplicationjetpack.CourtTopAppBar
import com.example.courtreservationapplicationjetpack.components.BottomBar
import com.example.courtreservationapplicationjetpack.firestore.Notification
import com.example.courtreservationapplicationjetpack.firestore.NotificationViewModel
import com.example.courtreservationapplicationjetpack.firestore.UserViewModel
import com.example.courtreservationapplicationjetpack.firestore.Users
import com.example.courtreservationapplicationjetpack.navigation.NavigationDestination
import com.example.courtreservationapplicationjetpack.signIn.GoogleAuthUiClient
import com.google.firebase.Timestamp

object FriendsDestination : NavigationDestination {
    override val route = "friends"
    override val titleRes = "Friends"
    override val icon = Icons.Default.Notifications
}

@Composable
fun Friends(
    navController: NavController,
    modifier: Modifier = Modifier,
    googleAuthUiClient : GoogleAuthUiClient,
    navigateBack: () -> Unit
){
    Scaffold(
        topBar = {
            CourtTopAppBar(canNavigateBack = true, navigateUp = navigateBack, text = "Friends")
        },
        bottomBar = { BottomBar(navController = navController as NavHostController) }

    ) {
            innerPadding ->
        FriendsBody(
            modifier = modifier.padding(innerPadding),
            googleAuthUiClient = googleAuthUiClient
        )
    }
}

@Composable
fun FriendsBody(
    modifier: Modifier = Modifier,
    googleAuthUiClient : GoogleAuthUiClient,
    userViewModel: UserViewModel = viewModel()
){
    val email = googleAuthUiClient.getSignedInUser()?.email
    if(email != null)
        userViewModel.getUserByEmail(email)
    if(userViewModel.user.value.friends.isNotEmpty())
        userViewModel.getUserListByEmails(userViewModel.user.value.friends)
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 16.dp)
    ) {
        if(userViewModel.users.value.isNotEmpty())
            items(userViewModel.users.value){f ->
                FriendsItem(friend = f)
            }
        if(email != null)
            item{
                AddFriendsItem(email = email)
            }
    }
}

@Composable
fun FriendsItem(
    friend: Users
){
    Card(modifier = Modifier
        .fillMaxWidth()
        .padding(start = 16.dp, end = 16.dp),
        elevation = CardDefaults.cardElevation( 8.dp )
    ){
        Surface(color = Color.White){
            Row(modifier = Modifier
                .padding(start = 16.dp, top = 8.dp, bottom = 8.dp)
            ){
                Column(modifier = Modifier
                    .fillMaxSize()
                    .align(Alignment.CenterVertically)
                    .weight(2f)
                ){
                    if(friend.imageUri != "")
                        Image(
                            modifier = Modifier
                                .size(72.dp)
                                .clip(shape = CircleShape),
                            painter = rememberAsyncImagePainter(model = Uri.parse(friend.imageUri)),
                            contentDescription = "Profile Image",
                            contentScale = ContentScale.Crop
                        )
                    else
                        Icon(Icons.Default.Face, contentDescription = "Friend", modifier = Modifier.size(40.dp))
                }
                Column(modifier = Modifier
                    .fillMaxSize()
                    .weight(6f)
                ){
                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp, bottom = 4.dp)
                    ){
                        Text(text = friend.name.toString(), fontWeight = FontWeight.ExtraLight)
                    }
                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp, bottom = 16.dp)
                    ){
                        Text(text = friend.nickname.toString())
                    }
                }
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
