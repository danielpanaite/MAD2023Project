package com.example.courtreservationapplicationjetpack.views.profile


import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Build
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.KeyboardArrowRight
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import coil.compose.rememberAsyncImagePainter
import com.example.courtreservationapplicationjetpack.CourtTopAppBar
import com.example.courtreservationapplicationjetpack.R
import com.example.courtreservationapplicationjetpack.components.BottomBar
import com.example.courtreservationapplicationjetpack.navigation.NavigationDestination
import com.example.courtreservationapplicationjetpack.ui.appViewModel.AppViewModelProvider
import com.example.courtreservationapplicationjetpack.views.reservations.ReservationDetailsUiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

//import com.example.courtreservationapplicationjetpack.navigation.Screens

object ProfileDestination : NavigationDestination {
    override val route = "profile"
    override val titleRes = "Profile"
    override val icon = Icons.Default.Person
    const val profileIdArg = "profileIdArg"
    val routeWithArgs = "${EditProfileDestination.route}/{$profileIdArg}"


}

//private val optionsList: ArrayList<OptionsData> = ArrayList()


@ExperimentalMaterial3Api
@Composable
fun Profile(
    modifier: Modifier = Modifier,
    navigateToEditProfileDestination: (Int) -> Unit,
    navigateToSportPreferencesDestination: () -> Unit,
    navController: NavController,
    navigateBack: () -> Unit,
    viewModel: ProfileViewModel = viewModel(factory = AppViewModelProvider.Factory)

){

    val uiState = viewModel.uiState.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    Scaffold(
        topBar = {
            CourtTopAppBar(canNavigateBack = true,
                navigateUp = navigateBack)
        },
        /*
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navigateToEditProfileDestination(uiState.value.userDetails.id!!) },
                modifier = Modifier.navigationBarsPadding()
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "edit",
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        },
        */
        bottomBar = { BottomBar(navController = navController as NavHostController) }

    ) {
            innerPadding ->
        ProfileBody(
            modifier = modifier.padding(innerPadding),
            navController = rememberNavController(),
            profileUiState = uiState.value,
            navigateToEditProfileDestination = navigateToEditProfileDestination,
                    navigateToSportPreferencesDestination =navigateToSportPreferencesDestination

        )
    }


}

@Composable
fun ProfileBody(
    modifier: Modifier = Modifier,
    navController: NavController,
    profileUiState: ProfileUiState,
    navigateToEditProfileDestination: (Int) -> Unit,
    navigateToSportPreferencesDestination: () -> Unit

){
    /*
    // This indicates if the optionsList has data or not
    // Initially, the list is empty. So, its value is false.
    var listPrepared by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(Unit) {
        withContext(Dispatchers.Default) {
            optionsList.clear()
            // Add the data to optionsList
            prepareOptionsData()
            listPrepared = true
        }
    }
    */

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
                    navigateToSportPreferencesDestination = navigateToSportPreferencesDestination
                )
            }
            /*
            // Show the options
            items(optionsList) { item ->
                OptionsItemStyle(item = item)
            }
            */

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
            painter = painterResource(id = R.drawable.baseline_person_24),
            contentDescription = "Your Image"
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
                        fontSize = 22.sp,
                        //fontFamily = FontFamily(Font(R.font.roboto_regular, FontWeight.Bold)),
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
                        //fontFamily = FontFamily(Font(R.font.roboto_regular, FontWeight.Normal)),
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
                    modifier = Modifier.size(24.dp),
                    imageVector = Icons.Outlined.Edit,
                    contentDescription = "Edit Details",
                    //tint = MaterialTheme.colors.primary
                )
            }

        }
    }
}



// Row style for options
@Composable
private fun OptionsItemStyle(
    navigateToSportPreferencesDestination: () -> Unit,
    profileUiState: ProfileUiState
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
                /*
            .clickable(enabled = true) {
                Toast
                    .makeText(item.title, Toast.LENGTH_SHORT)
                    .show()
            }*/
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
            imageVector = Icons.Outlined.Star,
            contentDescription = "Sports",

            //tint = MaterialTheme.colors.primary
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
            /*
        .clickable(enabled = true) {
            Toast
                .makeText(item.title, Toast.LENGTH_SHORT)
                .show()
        }*/
            .padding(all = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Icon(
            modifier = Modifier
                .size(32.dp),
            imageVector = Icons.Outlined.CheckCircle,
            contentDescription = "Achievements",
            //tint = MaterialTheme.colors.primary
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

/*
private fun prepareOptionsData() {

    val appIcons = Icons.Outlined

    optionsList.add(
        OptionsData(
            icon = appIcons.Star,
            title = "Sports",
            subTitle = "Manage your sport preferences",

        nav =  SportPreferences.route
        )
    )

    optionsList.add(
        OptionsData(
            icon = appIcons.CheckCircle,
            title = "Achievements",
            subTitle = "Manage your achievements",
            nav =  SportPreferences.route

        )
    )
    /*
    optionsList.add(
        OptionsData(
            icon = appIcons.Settings,
            title = "Settings",
            subTitle = "App notification settings"
        )
    )
    */
}
data class OptionsData(val icon: ImageVector, val title: String, val subTitle: String, val nav: NavigationDestination)

/*

@ExperimentalMaterial3Api
@Composable
fun Profile(
    modifier: Modifier = Modifier,
    navigateToEditProfileDestination: (Int) -> Unit,
    navController: NavController,
    navigateBack: () -> Unit,
    viewModel: ProfileViewModel = viewModel(factory = AppViewModelProvider.Factory)

){

    Column(modifier = Modifier
        .verticalScroll(rememberScrollState())
        .padding(8.dp)
        .padding(vertical = 60.dp)
    ){
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
            horizontalArrangement = Arrangement.Center

        ){
            Text(text = "Your Profile Information")

        }

        ProfileImage()
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Name", modifier = Modifier.width(100.dp))
            Text(text = "Laura")
        }
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp)
            .padding(top = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Username", modifier = Modifier
                .width(100.dp)
                .padding(top = 8.dp))
            Text(text = "Laura")
        }
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp)
            .padding(top = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Email", modifier = Modifier.width(100.dp))
            Text(text = "Laura@gmail.com")    }
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp)
            .padding(top = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Phone Number", modifier = Modifier.width(100.dp))
            Text(text = "34294839")
        }

        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp)
            .padding(top = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Sex", modifier = Modifier.width(100.dp))
            Text(text = "female")
        }
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp)
            .padding(top = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Place", modifier = Modifier.width(100.dp))
            Text(text = "Turin")
        }

        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp)
            .padding(top = 8.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Button(onClick = { navigateToEditProfileDestination}) {
                Text(text = "Edit Profile")
            }
        }
    }
}

@Composable
fun ProfileImage(){
    val imageUri = rememberSaveable{
        mutableStateOf("")
    }
    val painter = rememberAsyncImagePainter(
        if(imageUri.value.isEmpty())
            R.drawable.baseline_person_24
        else
            imageUri.value
    )



    Column(modifier = Modifier
        .padding(8.dp)
        .fillMaxWidth(),
    horizontalAlignment = Alignment.CenterHorizontally
        ){
        Card(shape = CircleShape,
         modifier = Modifier
             .padding(8.dp)
             .size(100.dp)
        ){
            Image(
                painter=painter,
                contentDescription = null,
                modifier = Modifier
                    .wrapContentSize(),
                contentScale = ContentScale.Crop

            )
        }
    }


}

*/


/*
@ExperimentalMaterial3Api
@Composable
@Preview(showBackground = true)
fun ProfileScreenPreview(){
    Profile(navController = rememberNavController(), navigateToEditProfileDestination = {},
        navigateBack= {})
}

 */