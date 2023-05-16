package com.example.courtreservationapplicationjetpack.views.profile

import android.net.Uri
import android.provider.ContactsContract.Profile
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import com.example.courtreservationapplicationjetpack.R
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.courtreservationapplicationjetpack.CourtTopAppBar
import com.example.courtreservationapplicationjetpack.components.BottomBar
import com.example.courtreservationapplicationjetpack.navigation.NavigationDestination
import com.example.courtreservationapplicationjetpack.ui.appViewModel.AppViewModelProvider
import com.example.courtreservationapplicationjetpack.views.courts.ReservationEntryBody
import com.example.courtreservationapplicationjetpack.views.courts.ReservationInputForm
import com.example.courtreservationapplicationjetpack.views.reservations.EditReservationViewModel
import com.example.courtreservationapplicationjetpack.views.reservations.ReservationDetails
import com.example.courtreservationapplicationjetpack.views.reservations.ReservationsUiState
import kotlinx.coroutines.launch

object EditProfileDestination : NavigationDestination {
    override val route = "profile_edit"
    override val titleRes = "edit Profile"
    override val icon = Icons.Default.Edit
    const val profileIdArg = "profileId"
    val routeWithArgs = "$route/{$profileIdArg}"

}
@ExperimentalMaterial3Api
@Composable
fun EditProfile(
    modifier: Modifier = Modifier,
    navController: NavController,

    navigateBack: () -> Unit,
    onNavigateUp: () -> Unit,

    viewModel: EditProfileViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            CourtTopAppBar(
                canNavigateBack = true,
                navigateUp = onNavigateUp
            )
        },
        bottomBar = { BottomBar(navController = navController as NavHostController) }

    ) {


            innerPadding ->
        ProfileEntryBody(
            profileUiState = viewModel.profileUiState,
            onProfileValueChange = viewModel::updateUiState,
            onSaveClick = {
                coroutineScope.launch {
                    viewModel.updateProfile()
                    //navigateToMyReservations()
                }
            },
            modifier = modifier.padding(innerPadding),
        )
    }
}

@Composable
fun ProfileEntryBody(
    profileUiState: ProfileUiState,
    onProfileValueChange: (UserDetails) -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier
){

    LazyColumn (
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(32.dp)
    ){
        item {
            ProfileInputForm(userDetails = profileUiState.userDetails, onValueChange = onProfileValueChange)
        }
        item {
            Button(onClick = onSaveClick,
                enabled = profileUiState.isEntryValid,
                modifier = Modifier.fillMaxWidth())
            {
                Text(text = "save")
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileInputForm(
    userDetails: UserDetails,
    modifier: Modifier = Modifier,
    onValueChange: (UserDetails) -> Unit ={},
    enabled: Boolean = true
) {


    val notification = rememberSaveable { mutableStateOf("") }
    if (notification.value.isNotEmpty()) {
        Toast.makeText(LocalContext.current, notification.value, Toast.LENGTH_LONG).show()
        notification.value = ""
    }

    Column(
        modifier = Modifier
            //.verticalScroll(rememberScrollState())
            .padding(8.dp)
        //.padding(vertical = 60.dp)
    ) {

        /*
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween

        ) {
            Text(
                text = "Cancel",
                modifier = Modifier.clickable { notification.value = "cancelled" })
            Text(
                text = "Save",
                modifier = Modifier.clickable { notification.value = "Profile Updated" })

        }
        */


        //  EditProfileImage()
        val imageUri = rememberSaveable {
            mutableStateOf("")
        }
        /*
        val painter = rememberAsyncImagePainter(
            if(imageUri.value.isEmpty())
                R.drawable.baseline_person_24
            else
                imageUri.value
        )
        */

        val painter = rememberAsyncImagePainter(
           // if (userDetails.imageUri?.isEmpty()!!)
             //   R.drawable.baseline_person_24
           // else
                userDetails.imageUri
        )


        val launcher =
            rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent())
            { uri: Uri? ->
                uri?.let { imageUri.value = it.toString() }
            }



        Column(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                shape = CircleShape,
                modifier = Modifier
                    .padding(8.dp)
                    .size(100.dp)
            ) {
                Image(
                    painter = painter,
                    contentDescription = null,
                    modifier = Modifier
                        .wrapContentSize()
                        .clickable { launcher.launch("image/*") } //every kind of image is fine "image/*"
                    ,
                    contentScale = ContentScale.Crop

                )
            }
            Text(text = "Change profile picture")
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 4.dp, end = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Name", modifier = Modifier.width(100.dp))
            OutlinedTextField(
                value = userDetails.name,
                onValueChange = { onValueChange(userDetails.copy(name = it)) },
                //keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                label = { Text(text = "name") },
                modifier = Modifier.fillMaxWidth(),
                enabled = enabled,
                singleLine = true
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 4.dp, end = 4.dp)
                .padding(top = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Nickname", modifier = Modifier.width(100.dp).padding(top = 8.dp))
            OutlinedTextField(
                value = userDetails.nickname,
                onValueChange = { onValueChange(userDetails.copy(nickname = it)) },
                //keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                label = { Text(text = "nickname") },
                modifier = Modifier.fillMaxWidth(),
                enabled = enabled,
                singleLine = true
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 4.dp, end = 4.dp)
                .padding(top = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Email", modifier = Modifier.width(100.dp).padding(top = 8.dp))
            OutlinedTextField(
                value = userDetails.email,
                onValueChange = { onValueChange(userDetails.copy(email = it)) },
                label = { Text(text = "email") },
                modifier = Modifier.fillMaxWidth(),
                enabled = enabled,
                singleLine = true
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 4.dp, end = 4.dp)
                .padding(top = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Address", modifier = Modifier.width(100.dp).padding(top = 8.dp))
            OutlinedTextField(
                value = userDetails.address,
                onValueChange = { onValueChange(userDetails.copy(address = it)) },
                label = { Text(text = "address") },
                modifier = Modifier.fillMaxWidth(),
                enabled = enabled,
                singleLine = true
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 4.dp, end = 4.dp)
                .padding(top = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Phone number", modifier = Modifier.width(100.dp).padding(top = 8.dp))
            OutlinedTextField(
                value = userDetails.phone,
                onValueChange = { onValueChange(userDetails.copy(phone = it)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                label = { Text(text = "phone number") },
                modifier = Modifier.fillMaxWidth(),
                enabled = enabled,
                singleLine = true
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 4.dp, end = 4.dp)
                .padding(top = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Age", modifier = Modifier.width(100.dp).padding(top = 8.dp))
            OutlinedTextField(
                value = userDetails.age,
                onValueChange = { onValueChange(userDetails.copy(age = it)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                label = { Text(text = "age") },
                modifier = Modifier.fillMaxWidth(),
                enabled = enabled,
                singleLine = true
            )
        }


        /*
        Column(
            modifier = modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        )
        {
            //user id gia impostato da noi

            OutlinedTextField(
                value = userDetails.name,
                onValueChange = { onValueChange(userDetails.copy(name = it)) },
                //keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                label = { Text(text = "name") },
                modifier = Modifier.fillMaxWidth(),
                enabled = enabled,
                singleLine = true
            )
            OutlinedTextField(
                value = userDetails.nickname,
                onValueChange = { onValueChange(userDetails.copy(nickname = it)) },
                //keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                label = { Text(text = "nickname") },
                modifier = Modifier.fillMaxWidth(),
                enabled = enabled,
                singleLine = true
            )

            OutlinedTextField(
                value = userDetails.email,
                onValueChange = { onValueChange(userDetails.copy(email = it)) },
                label = { Text(text = "email") },
                modifier = Modifier.fillMaxWidth(),
                enabled = enabled,
                singleLine = true
            )
            OutlinedTextField(
                value = userDetails.address,
                onValueChange = { onValueChange(userDetails.copy(address = it)) },
                label = { Text(text = "address") },
                modifier = Modifier.fillMaxWidth(),
                enabled = enabled,
                singleLine = true
            )
            OutlinedTextField(
                value = userDetails.phone,
                onValueChange = { onValueChange(userDetails.copy(phone = it)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                label = { Text(text = "phone number") },
                modifier = Modifier.fillMaxWidth(),
                enabled = enabled,
                singleLine = true
            )
            OutlinedTextField(
                value = userDetails.age,
                onValueChange = { onValueChange(userDetails.copy(age = it)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                label = { Text(text = "age") },
                modifier = Modifier.fillMaxWidth(),
                enabled = enabled,
                singleLine = true
            )
        }
    }

 */

    }
}



@Composable
fun EditProfileImage(){
    val imageUri = rememberSaveable{
        mutableStateOf("")
    }
    /*
    val painter = rememberAsyncImagePainter(
        if(imageUri.value.isEmpty())
            R.drawable.baseline_person_24
        else
            imageUri.value
    )
    */

    val painter = rememberAsyncImagePainter(
        if(imageUri.value.isEmpty())
            R.drawable.baseline_person_24
        else
            imageUri.value
    )



    val launcher = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent())
    {
            uri : Uri? ->
        uri?.let{imageUri.value = it.toString()}
    }



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
                    .wrapContentSize()
                    .clickable { launcher.launch("image/*") } //every kind of image is fine "image/*"
                ,
                contentScale = ContentScale.Crop

            )
        }
        Text(text = "Change profile picture")
    }
}

/*
@ExperimentalMaterial3Api
@Composable
@Preview(showBackground = true)
fun EditProfileScreenPreview(){
    EditProfile()
}

 */