package com.example.courtreservationapplicationjetpack.views.profile
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager

import android.net.Uri
import android.provider.ContactsContract.Profile
import android.provider.Settings.Global.getString
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import coil.request.ImageRequest
import coil.transform.CircleCropTransformation
import com.example.courtreservationapplicationjetpack.BuildConfig
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
import java.io.File
import java.io.FileDescriptor
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Objects

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
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ){
        item {
            ProfileInputForm(userDetails = profileUiState.userDetails, onValueChange = onProfileValueChange)
        }
        item {
            Button(onClick = {
                // Set the imageUri field in the UserDetails object

                //val updatedDetails = profileUiState.userDetails.copy(imageUri = profileUiState.userDetails.imageUri)
                // Call the onSaveClick callback
                onSaveClick()
            },
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

    val context = LocalContext.current
    val file = context.createImageFile()
    val uri = FileProvider.getUriForFile(
        Objects.requireNonNull(context),
        BuildConfig.APPLICATION_ID + ".provider", file
    )

    var photoUri by rememberSaveable { mutableStateOf<Uri?>(Uri.parse(userDetails.imageUri)) }
    var chosenPhoto by rememberSaveable { mutableStateOf<Uri?>(null)}

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        chosenPhoto = uri
    }
    var cameraLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) {
            chosenPhoto = uri
        }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {
        if (it) {
            Toast.makeText(context, "Permission Granted", Toast.LENGTH_SHORT).show()
            cameraLauncher.launch(uri)
        } else {
            Toast.makeText(context, "Permission Denied", Toast.LENGTH_SHORT).show()
        }
    }

Log.d("imageUri in ProfileInputForm", "${userDetails.imageUri}")
    Log.d("imageUri in ProfileInputForm in uri form", "${Uri.parse(userDetails.imageUri)}")

    Log.d("photoUri", "$photoUri")
    Log.d("chosenPhotoUri", "$chosenPhoto")

    val chosenPhotUriState = rememberUpdatedState(chosenPhoto)

    if (chosenPhotUriState.value != chosenPhoto) {
        Log.d("ma qui ci entri", "${chosenPhoto}")
        onValueChange(userDetails.copy(imageUri = chosenPhoto.toString()))
    }

    LaunchedEffect(chosenPhoto){

        Log.d("ma qui ci entri launched Effect", "${chosenPhoto}")
        onValueChange(userDetails.copy(imageUri = chosenPhoto.toString()))

    }

    Column(modifier = modifier) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = if (chosenPhoto != null) {
                    rememberAsyncImagePainter(
                        ImageRequest.Builder(LocalContext.current)
                            .data(data = chosenPhoto)
                            .apply<ImageRequest.Builder>(block = fun ImageRequest.Builder.() {
                                crossfade(true)
                                placeholder(R.drawable.baseline_person_24)
                                transformations(CircleCropTransformation())
                            }).build()

                    )


                } else {
                    if(userDetails.imageUri!=null){

                        rememberAsyncImagePainter(
                            ImageRequest.Builder(LocalContext.current)
                                .data(data = Uri.parse(userDetails.imageUri))
                                .apply<ImageRequest.Builder>(block = fun ImageRequest.Builder.() {
                                    crossfade(true)
                                    placeholder(R.drawable.baseline_person_24)
                                    transformations(CircleCropTransformation())
                                }).build()
                        )
                        }else{
                        rememberAsyncImagePainter(

                            ImageRequest.Builder(LocalContext.current)
                                .data(data = R.drawable.baseline_person_24)
                                .apply<ImageRequest.Builder>(block = fun ImageRequest.Builder.() {
                                    crossfade(true)
                                    placeholder(R.drawable.baseline_person_24)
                                    transformations(CircleCropTransformation())
                                }).build()
                        )
                        }

                },
                contentDescription = "Selected Image",
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape) ,

            )

        }

        Log.d("chosenPhotoUri state", "$chosenPhotUriState")
        Log.d("chosenPhoto", "$chosenPhoto")


//con le immagini prese dalla galleria inizialmente non le mostra, con quelle prese dalla camera su

        Button(
            onClick = { launcher.launch("image/*") },
            modifier = Modifier
                .padding(top = 10.dp)
                .fillMaxWidth()
                .align(CenterHorizontally)
        ) {
            Text("Select Image from gallery")
        }

        Button(onClick = {
            val permissionCheckResult =
                ContextCompat.checkSelfPermission(context, android.Manifest.permission.CAMERA)
            if (permissionCheckResult == PackageManager.PERMISSION_GRANTED) {
                cameraLauncher.launch(uri)
            } else {
                // Request a permission
                permissionLauncher.launch(android.Manifest.permission.CAMERA)
            }
        }, modifier = Modifier
            .padding(top = 12.dp)
            .fillMaxWidth()
            .align(CenterHorizontally)) {
            Text(text = "Capture Image From Camera")
        }

        /*
        if (capturedImageUri.path?.isNotEmpty() == true) {
        */

        Column(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()

        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {

                OutlinedTextField(
                    value = userDetails.name,
                    onValueChange = {
                        if(chosenPhoto!=null){

                            onValueChange(userDetails.copy(name = it, imageUri = chosenPhoto.toString()))

                        }
                                    },
                    label = { Text(text = "name") },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = enabled,
                    singleLine = true
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {

                OutlinedTextField(
                    value = userDetails.nickname,
                    onValueChange = { onValueChange(userDetails.copy(nickname = it)) },
                    label = { Text(text = "nickname") },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = enabled,
                    singleLine = true
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {

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
                    .padding(vertical = 4.dp)
            ) {

                OutlinedTextField(
                    value = userDetails.address,
                    onValueChange = {
                        if(chosenPhoto!=null){
                            val updatedDetails =userDetails.copy(address=it, imageUri = chosenPhoto.toString())

                            onValueChange(updatedDetails)
                        }else{
                            val updatedDetails =userDetails.copy(address=it, imageUri = photoUri.toString())
                            onValueChange(updatedDetails)
                        }
                    },

                    label = { Text(text = "address") },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = enabled,
                    singleLine = true
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {

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
                    .padding(vertical = 4.dp)
            ) {

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



    }

}


fun Context.createImageFile(): File {
    // Create an image file name
    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
    val imageFileName = "JPEG_" + timeStamp + "_"
    val image = File.createTempFile(
        imageFileName, /* prefix */
        ".jpg", /* suffix */
        externalCacheDir      /* directory */
    )
    return image
}
