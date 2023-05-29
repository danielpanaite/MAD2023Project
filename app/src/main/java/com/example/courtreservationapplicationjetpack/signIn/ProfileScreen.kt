package com.example.courtreservationapplicationjetpack.signIn

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Place
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.courtreservationapplicationjetpack.navigation.NavigationDestination
import kotlinx.coroutines.launch

object ProfileGoogleDestination : NavigationDestination {
    override val route  = "profile_google"
    override val titleRes = "SignIn"
    override val icon = Icons.Default.Place

}
@Composable
fun ProfileGoogle(
    googleAuthUiClient : GoogleAuthUiClient,
    context : Context,
    navController: NavController

){
    val lifecycle = LocalLifecycleOwner.current

    ProfileScreen(
        userData = googleAuthUiClient.getSignedInUser(),
        onSignOut = {
            lifecycle.lifecycleScope.launch {
                googleAuthUiClient.signOut()
                Toast.makeText(
                    context,
                    "Signed out",
                    Toast.LENGTH_LONG
                ).show()

                navController.navigate("sign_in")
            }
        }
    )
}
@Composable
fun ProfileScreen(
    userData: UserData?,
    onSignOut: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if(userData?.profilePictureUrl != null) {
            AsyncImage(
                model = userData.profilePictureUrl,
                contentDescription = "Profile picture",
                modifier = Modifier
                    .size(150.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
        if(userData?.username != null) {
            Text(
                text = userData.username,
                textAlign = TextAlign.Center,
                fontSize = 36.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
        Button(onClick = onSignOut) {
            Text(text = "Sign out")
        }
    }
}