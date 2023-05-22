package com.example.courtreservationapplicationjetpack.views.profile

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.KeyboardArrowRight
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.example.courtreservationapplicationjetpack.CourtTopAppBar
import com.example.courtreservationapplicationjetpack.R
import com.example.courtreservationapplicationjetpack.components.BottomBar
import com.example.courtreservationapplicationjetpack.navigation.NavigationDestination
import com.example.courtreservationapplicationjetpack.ui.appViewModel.AppViewModelProvider

object ProfileDestination : NavigationDestination {
    override val route = "profile"
    override val titleRes = "Profile"
    override val icon = Icons.Default.Person
    const val profileIdArg = "profileIdArg"
    val routeWithArgs = "${EditProfileDestination.route}/{$profileIdArg}"
}

@ExperimentalMaterial3Api
@Composable
fun Profile(
    modifier: Modifier = Modifier,
    navigateToEditProfileDestination: (Int) -> Unit,
    navigateToSportPreferencesDestination: () -> Unit,
    navigateToAchievementsDestination: () -> Unit,
    navController: NavController,
    navigateBack: () -> Unit,
    viewModel: ProfileViewModel = viewModel(factory = AppViewModelProvider.Factory)

){
    val uiState = viewModel.uiState.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    Scaffold(
        topBar = {
            CourtTopAppBar(canNavigateBack = false)
        },
        bottomBar = { BottomBar(navController = navController as NavHostController) }

    ) {
            innerPadding ->
        ProfileBody(
            modifier = modifier.padding(innerPadding),
            navController = rememberNavController(),
            profileUiState = uiState.value,
            navigateToEditProfileDestination = navigateToEditProfileDestination,
            navigateToSportPreferencesDestination =navigateToSportPreferencesDestination,
            navigateToAchievementsDestination = navigateToAchievementsDestination
        )
    }
}

@Composable
fun ProfileBody(
    modifier: Modifier = Modifier,
    navController: NavController,
    profileUiState: ProfileUiState,
    navigateToEditProfileDestination: (Int) -> Unit,
    navigateToSportPreferencesDestination: () -> Unit,
    navigateToAchievementsDestination: () -> Unit,

){

    LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 60.dp)
        ) {
            item {
                // User's image, name, email and edit button
                UserDetails(profileUiState = profileUiState,
                    navigateToEditProfileDestination = navigateToEditProfileDestination)
            }
            item{
                OptionsItemStyle(
                    profileUiState = profileUiState,
                    navigateToSportPreferencesDestination = navigateToSportPreferencesDestination,
                    navigateToAchievementsDestination = navigateToAchievementsDestination
                )
            }
        }
    }


// This composable displays user's image, name, email and edit button
@Composable
private fun UserDetails(
    profileUiState: ProfileUiState,
    navigateToEditProfileDestination: (Int) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // User's image
        Image(
            modifier = Modifier
                .size(72.dp)
                .clip(shape = CircleShape),
            //this should be the image in the db for each user
            painter = if(profileUiState.userDetails.imageUri!=="" && profileUiState.userDetails.imageUri!==null){
                rememberAsyncImagePainter(model = Uri.parse(profileUiState.userDetails.imageUri)?: R.drawable.ic_person_new)
            }else{
                painterResource(id = R.drawable.ic_person_new)
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
                Text(
                    text = profileUiState.userDetails.name,
                    style = TextStyle(
                        fontSize = 23.sp,
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(2.dp))
                // User's email
                Text(
                    text = profileUiState.userDetails.email,
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
                    navigateToEditProfileDestination(profileUiState.userDetails.id!!)
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

// Row style for options
@Composable
private fun OptionsItemStyle(
    navigateToSportPreferencesDestination: () -> Unit,
    navigateToAchievementsDestination: () -> Unit,
    profileUiState: ProfileUiState
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(all = 16.dp)
            .clickable(
                onClick = {
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
                       // fontFamily = FontFamily(Font(R.font.roboto_medium, FontWeight.Medium))
                    )
                )

                Spacer(modifier = Modifier.height(2.dp))

                // Sub title
                Text(
                    text = "Manage your sport preferences",
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
}








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


