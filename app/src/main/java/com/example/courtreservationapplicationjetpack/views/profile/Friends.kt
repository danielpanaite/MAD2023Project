package com.example.courtreservationapplicationjetpack.views.profile

import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.outlined.AddCircle
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.courtreservationapplicationjetpack.CourtTopAppBar
import com.example.courtreservationapplicationjetpack.R
import com.example.courtreservationapplicationjetpack.components.BottomBar
import com.example.courtreservationapplicationjetpack.firestore.Notification
import com.example.courtreservationapplicationjetpack.firestore.NotificationViewModel
import com.example.courtreservationapplicationjetpack.firestore.UserViewModel
import com.example.courtreservationapplicationjetpack.firestore.Users
import com.example.courtreservationapplicationjetpack.navigation.NavigationDestination
import com.example.courtreservationapplicationjetpack.signIn.GoogleAuthUiClient
import com.example.courtreservationapplicationjetpack.ui.theme.GreyItemInactive
import com.example.courtreservationapplicationjetpack.views.notifications.FindFriendsDestination
import com.google.firebase.Timestamp
import java.util.Locale

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
            googleAuthUiClient = googleAuthUiClient,
            navController = navController
        )
    }
}

@Composable
fun FriendsBody(
    modifier: Modifier = Modifier,
    googleAuthUiClient : GoogleAuthUiClient,
    userViewModel: UserViewModel = viewModel(),
    navController: NavController,
){
    val composition = rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.save_success))
    val progress by animateLottieCompositionAsState(
        composition = composition.value,
        iterations = LottieConstants.IterateForever
    )
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
                items(userViewModel.users.value) { f ->
                    FriendsItem(friend = f, false)
                }
        else
            item {
                Box(modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp)) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(0.5f)
                            .align(Alignment.TopCenter),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        val fcomposition = rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.no_friends))
                        LottieAnimation(
                            modifier = Modifier
                                .size(190.dp)
                                .align(Alignment.CenterHorizontally),
                            contentScale = ContentScale.FillWidth,
                            composition = fcomposition.value,
                            progress = { progress },
                            maintainOriginalImageBounds = true
                        )
                        Text(
                            text = "Oops..looks like you don't have any friends on Sportify yet! You can add them by pressing here.",
                            color = Color.Gray,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                        Button(onClick = ({
                            navController.navigate(FindFriendsDestination.route)
                        }), modifier = Modifier.padding(vertical = 8.dp).padding(bottom = 32.dp) ) {
                            Text(text = "Add friends")
                        }
                    }
                }
            }
    }
}

@Composable
fun FriendsItem(
    friend: Users,
    invite: Boolean,
    email: String = "",
    notificationViewModel: NotificationViewModel = viewModel()
){
    val toastFriend = Toast.makeText(LocalContext.current, "Friend request sent!", Toast.LENGTH_SHORT)
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
                    .padding(start = 8.dp)
                    .weight(5f)
                ){
                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp, bottom = 4.dp)
                    ){
                        Text(text = "@${friend.nickname.toString()}", fontWeight = FontWeight.ExtraLight, style = MaterialTheme.typography.bodyMedium)
                    }
                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp, bottom = 16.dp)
                    ){
                        Text(text = friend.name.toString())
                    }
                }
                if(invite)
                    Column(modifier = Modifier
                        .fillMaxSize()
                        .align(Alignment.CenterVertically)
                        .padding(end = 16.dp)
                        .weight(1f)
                    ){
                        Icon(Icons.Outlined.AddCircle,
                            contentDescription = "Add",
                            modifier = Modifier
                                .size(40.dp)
                                .clickable {
                                    notificationViewModel.createNotification(
                                        Notification(
                                            sender = email,
                                            receiver = friend.email,
                                            type = "friend",
                                            status = "pending",
                                            date = Timestamp.now()
                                        )
                                    )
                                    toastFriend.show()
                                },
                            tint = GreyItemInactive
                        )
                    }
            }
        }
        if(friend.sportPreferences.isNotEmpty()) {
            Divider(thickness = 1.dp, color = MaterialTheme.colorScheme.primaryContainer)
            Surface(color = Color.White) {
                Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "Interests", modifier = Modifier.padding(top = 4.dp), color = GreyItemInactive)
                    for (i in friend.sportPreferences.indices) {
                        Row(
                            modifier = Modifier.padding(top = 8.dp, bottom = 8.dp).offset(x = (-8).dp)
                        ) {
                            Column(modifier = Modifier.fillMaxWidth().weight(1f), horizontalAlignment = Alignment.End){
                                Text(text = "${friend.sportPreferences[i].sportName.replaceFirstChar {
                                if (it.isLowerCase()) it.titlecase(Locale.getDefault())
                                else it.toString() }} ")
                            }
                            Column(modifier = Modifier.fillMaxWidth().weight(1f), horizontalAlignment = Alignment.Start){
                                Text(text = "- ${friend.sportPreferences[i].masteryLevel}")
                            }

                        }
                    }
                }
            }
        }
    }
}