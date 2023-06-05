package com.example.courtreservationapplicationjetpack.views.notifications

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.courtreservationapplicationjetpack.CourtTopAppBar
import com.example.courtreservationapplicationjetpack.components.BottomBar
import com.example.courtreservationapplicationjetpack.firestore.UserViewModel
import com.example.courtreservationapplicationjetpack.navigation.NavigationDestination
import com.example.courtreservationapplicationjetpack.signIn.GoogleAuthUiClient
import com.example.courtreservationapplicationjetpack.views.profile.FriendsItem

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
    userViewModel: UserViewModel = viewModel()
){
    val email = googleAuthUiClient.getSignedInUser()?.email
    if(email != null)
        userViewModel.getUserByEmail(email)
    if(userViewModel.user.value.id != "" && email != null) {
        val friends: MutableList<String> = mutableListOf(*userViewModel.user.value.friends.toTypedArray())
        friends.add(email)
        userViewModel.getUserListBySports(friends)
    }
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 16.dp)
    ) {
        if(userViewModel.users.value.isNotEmpty())
            items(userViewModel.users.value){f ->
                FriendsItem(friend = f, true)
            }
        else
            item{
                Text(
                    text = "No people available for your chosen sports",
                    style = MaterialTheme.typography.headlineMedium,
                    textAlign = TextAlign.Center
                )
            }
    }
}