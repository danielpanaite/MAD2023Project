package com.example.courtreservationapplicationjetpack.views.profile

import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.courtreservationapplicationjetpack.CourtTopAppBar
import com.example.courtreservationapplicationjetpack.R
import com.example.courtreservationapplicationjetpack.components.BottomBar
import com.example.courtreservationapplicationjetpack.firestore.UserViewModel
import com.example.courtreservationapplicationjetpack.firestore.Users
import com.example.courtreservationapplicationjetpack.navigation.NavigationDestination
import com.example.courtreservationapplicationjetpack.signIn.GoogleAuthUiClient
import kotlinx.coroutines.launch


object ProfileDestination : NavigationDestination {
    override val route = "profile"
    override val titleRes = "Profile"
    override val icon = Icons.Default.Person
    const val profileArg = "profileArg"
    val routeWithArgs = "${EditProfileDestination.route}/{$profileArg}"
}

@ExperimentalMaterial3Api
@Composable
fun Profile(
    modifier: Modifier = Modifier,
    navigateToEditProfileDestination: (String) -> Unit,
    navigateToSportPreferencesDestination: () -> Unit,
    navigateToAchievementsDestination: () -> Unit,
    navController: NavController,
    navigateBack: () -> Unit,
    context: Context,
    googleAuthUiClient : GoogleAuthUiClient,
    viewModel: UserViewModel = viewModel()

){
    val lifecycle = LocalLifecycleOwner.current

    val email = googleAuthUiClient.getSignedInUser()?.email

    if (email != null) {
        viewModel.getUserByEmail(email)
    }
    val user = viewModel.user.value

    Scaffold(
        topBar = {
            CourtTopAppBar(canNavigateBack = false, text = "Profile")
        },
        bottomBar = { BottomBar(navController = navController as NavHostController) }
    ) {
            innerPadding ->
        ProfileBody(
            modifier = modifier.padding(innerPadding),
            navController = navController,
            user = user,
            navigateToEditProfileDestination = navigateToEditProfileDestination,
            navigateToSportPreferencesDestination =navigateToSportPreferencesDestination,
            navigateToAchievementsDestination = navigateToAchievementsDestination,
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
}

@Composable
fun ProfileBody(
    modifier: Modifier = Modifier,
    navController: NavController,
    user: Users,
    navigateToEditProfileDestination: (String) -> Unit,
    navigateToSportPreferencesDestination: () -> Unit,
    navigateToAchievementsDestination: () -> Unit,
    onSignOut: () -> Unit

){

    Box() {
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
        ) {
            item {
                // User's image, name, email and edit button
                UserDetails(
                    user = user,
                    navigateToEditProfileDestination = navigateToEditProfileDestination,
                    navController = navController
                )
            }
            item {
                OptionsItemStyle(
                    user = user,
                    navigateToSportPreferencesDestination = navigateToSportPreferencesDestination,
                    navigateToAchievementsDestination = navigateToAchievementsDestination,
                    navController = navController
                )
            }
        }
        Button(
            onClick = onSignOut, modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 100.dp)
        ) {
            Text(text = "Sign out")
        }
    }

}


// This composable displays user's image, name, email and edit button
@Composable
private fun UserDetails(
user: Users,
navigateToEditProfileDestination: (String) -> Unit,
navController: NavController
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)

        ) {
        Log.d("email", user.email)


        // User's image
        Image(
            modifier = Modifier
                .size(72.dp)
                .clip(shape = CircleShape),
            contentScale = ContentScale.Crop,
            //this should be the image in the db for each user
            painter = if(user.imageUri!=="" && user.imageUri!==null){
                rememberAsyncImagePainter(model = Uri.parse(user.imageUri)?: R.drawable.ic_person_new)
            }else{
                painterResource(id = R.drawable.ic_bottom_profile)
            },
            contentDescription = "Profile Image"

        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .weight(weight = 3f, fill = false)
                    .padding(start = 16.dp)
            ) {
                // User's name
                user.name?.let {
                    Text(
                        text = it,
                        style = TextStyle(
                            fontSize = 23.sp,
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Spacer(modifier = Modifier.height(2.dp))
                // User's email
                Text(
                    text = user.email,
                    style = TextStyle(
                        fontSize = 14.sp,
                        color = Color.Gray,
                        letterSpacing = (0.8).sp
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            // Edit button
            IconButton(
                modifier = Modifier
                    .weight(weight = 1f, fill = false),
                onClick = {
                   navigateToEditProfileDestination(user.email!!)
                }) {
                Icon(
                    modifier = Modifier.size(30.dp),
                    imageVector = Icons.Outlined.Edit,
                    contentDescription = "Edit Details",
                )
            }

        }
    }
}

@Composable
private fun OptionsItemStyle(
    navigateToSportPreferencesDestination: () -> Unit,
    navigateToAchievementsDestination: () -> Unit,
    navController: NavController,
    user: Users
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(all = 16.dp)
            .clickable(
                onClick = {
                    //navigateToSportPreferencesDestination(user.email!!)
                    navigateToSportPreferencesDestination()

                }),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = Modifier
                .size(32.dp),
            painter = painterResource(id = R.drawable.ic_star_new),
            contentDescription = "Sports",
        )

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .weight(weight = 3f, fill = false)
                    .padding(start = 16.dp)
            ) {

                // Title
                Text(
                    text = "Sports",
                    style = TextStyle(
                        fontSize = 18.sp,
                    )
                )

                Spacer(modifier = Modifier.height(2.dp))

                // Sub title
                Text(
                    text = "Manage your sport preferences",
                    style = TextStyle(
                        fontSize = 14.sp,
                        letterSpacing = (0.8).sp,
                        color = Color.Gray
                    )
                )

            }

            // Right arrow icon
            Icon(
                modifier = Modifier
                    .weight(weight = 1f, fill = false),
                imageVector = Icons.Outlined.KeyboardArrowRight,
                contentDescription = "Sport",
                tint = Color.Black.copy(alpha = 0.70f)
            )
        }

    }
    Row(
        modifier = Modifier
            .fillMaxWidth()

            .clickable(
                onClick = {
                    navigateToAchievementsDestination()
                })
            .padding(all = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = Modifier
                .size(32.dp),
            painter = painterResource(id = R.drawable.ic_medal),
            contentDescription = "Achievements",
        )
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .weight(weight = 3f, fill = false)
                    .padding(start = 16.dp)
            ) {

                // Title
                Text(
                    text = "Achievements",
                    style = TextStyle(
                        fontSize = 18.sp,
                        // fontFamily = FontFamily(Font(R.font.roboto_medium, FontWeight.Medium))
                    )
                )

                Spacer(modifier = Modifier.height(2.dp))

                // Sub title
                Text(
                    text = "Manage your achievements",
                    style = TextStyle(
                        fontSize = 14.sp,
                        letterSpacing = (0.8).sp,
                        //fontFamily = FontFamily(Font(R.font.roboto_regular, FontWeight.Normal)),
                        color = Color.Gray
                    )
                )

            }

            // Right arrow icon
            Icon(
                modifier = Modifier
                    .weight(weight = 1f, fill = false),
                imageVector = Icons.Outlined.KeyboardArrowRight,
                contentDescription = "Achievements",
                tint = Color.Black.copy(alpha = 0.70f)
            )
        }

    }
    Row(
        modifier = Modifier
            .fillMaxWidth()

            .clickable(
                onClick = {
                    navController.navigate(FriendsDestination.route)
                })
            .padding(all = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            Icons.Default.Face,
            contentDescription = "Friend",
            modifier = Modifier.size(40.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .weight(weight = 3f, fill = false)
                    .padding(start = 16.dp)
            ) {

                // Title
                Text(
                    text = "Friends",
                    style = TextStyle(
                        fontSize = 18.sp,
                        // fontFamily = FontFamily(Font(R.font.roboto_medium, FontWeight.Medium))
                    )
                )

                Spacer(modifier = Modifier.height(2.dp))

                // Sub title
                Text(
                    text = "Manage your friend list",
                    style = TextStyle(
                        fontSize = 14.sp,
                        letterSpacing = (0.8).sp,
                        //fontFamily = FontFamily(Font(R.font.roboto_regular, FontWeight.Normal)),
                        color = Color.Gray
                    )
                )

            }

            // Right arrow icon
            Icon(
                modifier = Modifier
                    .weight(weight = 1f, fill = false),
                imageVector = Icons.Outlined.KeyboardArrowRight,
                contentDescription = "Achievements",
                tint = Color.Black.copy(alpha = 0.70f)
            )
        }

    }
}







/*
@ExperimentalMaterial3Api
@Composable
@Preview(showBackground = true)
fun ProfileScreenUser(){
    UserDetails(
        ProfileUiState(
            UserDetails(
                1, "Mark", "marki", "laura@polito.it",
                "via monginevro 20", "10", "+39389939339",
                "1", ""

            ), true
        ), navigateToEditProfileDestination ={}

    )
}


@ExperimentalMaterial3Api
@Composable
@Preview(showBackground = true)
fun ProfileBody(){
    ProfileBody(
        modifier = Modifier,
        rememberNavController(),

        ProfileUiState(
            UserDetails(
                1, "MarkVerisjnsins", "marki", "laura@polito.it",
                "via monginevro 20", "10", "+39389939339",
                "1", ""

            ), true
        ),
        navigateToEditProfileDestination ={},
        navigateToSportPreferencesDestination ={},
        navigateToAchievementsDestination ={}


    )
}

 */


