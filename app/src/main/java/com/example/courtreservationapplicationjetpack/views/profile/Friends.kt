package com.example.courtreservationapplicationjetpack.views.profile

import android.net.Uri
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.courtreservationapplicationjetpack.CourtTopAppBar
import com.example.courtreservationapplicationjetpack.components.BottomBar
import com.example.courtreservationapplicationjetpack.firestore.UserViewModel
import com.example.courtreservationapplicationjetpack.firestore.Users
import com.example.courtreservationapplicationjetpack.navigation.NavigationDestination
import com.example.courtreservationapplicationjetpack.signIn.GoogleAuthUiClient
import com.example.courtreservationapplicationjetpack.ui.theme.ClearRed

object FriendsDestination : NavigationDestination {
    override val route = "friends"
    override val titleRes = "Friends"
    override val icon = Icons.Default.Notifications
}

@Composable
fun Friends(
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
                    .weight(5f)
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
                Column(modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
                    .align(Alignment.CenterVertically)
                ){
                    IconButton(onClick = {

                    }, modifier = Modifier
                        .fillMaxWidth()) {
                        Icon(Icons.Default.Clear, contentDescription = "Clear", tint = ClearRed)
                    }
                }
            }
        }
    }
}