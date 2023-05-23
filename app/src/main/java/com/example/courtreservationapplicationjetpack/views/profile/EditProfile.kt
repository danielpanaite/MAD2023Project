package com.example.courtreservationapplicationjetpack.views.profile

import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.courtreservationapplicationjetpack.R
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import coil.request.ImageRequest
import coil.transform.CircleCropTransformation
import com.example.courtreservationapplicationjetpack.BuildConfig
import com.example.courtreservationapplicationjetpack.CourtTopAppBar
import com.example.courtreservationapplicationjetpack.components.BottomBar
import com.example.courtreservationapplicationjetpack.navigation.NavigationDestination
import com.example.courtreservationapplicationjetpack.ui.appViewModel.AppViewModelProvider
import kotlinx.coroutines.launch
import java.io.File
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
    navigateToProfileDestination: () ->Unit,
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
            navigateToProfileDestination = navigateToProfileDestination,
            onSaveClick = {
                coroutineScope.launch {
                    viewModel.updateProfile()
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
    modifier: Modifier = Modifier,
    navigateToProfileDestination: () ->Unit,
    ){

    var showErrorDialog by remember { mutableStateOf(false) }



    LazyColumn (
        modifier = modifier
            .fillMaxWidth()
            .padding(1.dp),
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ){
        item {
            ProfileInputForm(userDetails = profileUiState.userDetails, onValueChange = onProfileValueChange)
        }
        item {
            Button(onClick = {
                 onSaveClick()
                // mettere un toast se tutto è andato bene e i valori sono giusti
                if(profileUiState.isEntryValid){navigateToProfileDestination()
                }else{
                    showErrorDialog = true

                }
            },
                enabled = profileUiState.isEntryValid,
                modifier = Modifier.fillMaxWidth())
            {
                Text(text = "EDIT PROFILE")
            }
        }
    }
    if (showErrorDialog) {
        AlertDialog(
            onDismissRequest = { showErrorDialog = false },
            title = { Text(text = "Errore") },
            text = { Text(text = "I valori inseriti non sono validi.") },
            confirmButton = {
                Button(onClick = { showErrorDialog = false }) {
                    Text(text = "Ok")
                }
            }
        )
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

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri->
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

    val chosenPhotUriState = rememberUpdatedState(chosenPhoto)
/*
    if (chosenPhotUriState.value != chosenPhoto) {
        Log.d("ma qui ci entri", "${chosenPhoto}")
        onValueChange(userDetails.copy(imageUri = chosenPhoto.toString()))
    }

 */
    val showMenu = remember { mutableStateOf(false) }


    LaunchedEffect(chosenPhoto){
        onValueChange(userDetails.copy(imageUri = chosenPhoto.toString()))
    }



    Box(
        modifier = Modifier.fillMaxSize(),
    ){
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                color = Color.White,

            ) {

                    Box(modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp),
                        contentAlignment = Alignment.Center


                    )
                    {
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
                                if (userDetails.imageUri != null) {

                                    rememberAsyncImagePainter(
                                        ImageRequest.Builder(LocalContext.current)
                                            .data(data = Uri.parse(userDetails.imageUri))
                                            .apply<ImageRequest.Builder>(block = fun ImageRequest.Builder.() {
                                                crossfade(true)
                                                placeholder(R.drawable.baseline_person_24)
                                                transformations(CircleCropTransformation())
                                            }).build()
                                    )
                                } else {
                                    rememberAsyncImagePainter(

                                        ImageRequest.Builder(LocalContext.current)
                                            .data(data = R.drawable.baseline_person_24)
                                            .apply<ImageRequest.Builder>(block = fun ImageRequest.Builder.() {
                                                crossfade(true)
                                                placeholder(R.drawable.baseline_person_24)
                                                transformations(CircleCropTransformation())
                                            }).build(),

                                    )

                                }

                            },
                            contentDescription = "Selected Image",
                            modifier = Modifier
                                .size(100.dp)
                                .clip(CircleShape),
                            colorFilter = if (chosenPhoto == null && userDetails.imageUri == null) {
                                ColorFilter.tint(Color.Black.copy(alpha = 0.3f))
                            } else {
                                null
                            }



                        )
                        IconButton(
                            onClick = { showMenu.value = true },
                            modifier = Modifier
                        ) {
                            Icon (
                                painter = painterResource(id = R.drawable.ic_camera),
                                contentDescription = "camera icon",
                                tint = Color.Black.copy(alpha = 0.70f)


                            )

                    }
                }
            }






                if(showMenu.value){
                    DropdownMenu(
                        expanded = showMenu.value,
                        onDismissRequest = { showMenu.value = false },
                        modifier = Modifier.wrapContentSize()
                    ) {
                        DropdownMenuItem(
                            onClick = {
                                launcher.launch("image/*")
                                showMenu.value = false
                            },
                            text = { Text("Select Image from gallery", color = Color.Black) },
                        )
                        DropdownMenuItem(
                            onClick = {
                                val permissionCheckResult =
                                    ContextCompat.checkSelfPermission(context, android.Manifest.permission.CAMERA)
                                if (permissionCheckResult == PackageManager.PERMISSION_GRANTED) {
                                    cameraLauncher.launch(uri)
                                } else {
                                    // Request a permission
                                    permissionLauncher.launch(android.Manifest.permission.CAMERA)
                                }
                                showMenu.value = false
                            },
                            text = { Text("Capture Image From Camera", color = Color.Black) },
                        )
                    }
                }
            }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Color.White)
            .border(
                border = BorderStroke(
                    width = 1.dp,
                    color = Color.LightGray
                ),
                shape = RoundedCornerShape(8.dp)
            )
        //elevation = 4.dp
    ) {
        // Aggiungi qui la composable per i campi dati del profilo, divisi in diversi TextField
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
                            onValueChange(userDetails.copy(name = it))
                    },
                    label = { Text(text = "NAME") },
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
                    label = { Text(text = "NICKNAME") },
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
                    label = { Text(text = "EMAIL") },
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
                        onValueChange(userDetails.copy(address=it))

                    },

                    label = { Text(text = "ADDRESS") },
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
                    label = { Text(text = "PHONE NUMBER") },
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
                    label = { Text(text = "AGE") },
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


@ExperimentalMaterial3Api
@Composable
@Preview(showBackground = true)
fun ProfileEntryBody(){
    ProfileEntryBody(
        ProfileUiState(
            UserDetails(
                1, "MarkVerisjnsins", "marki", "laura@polito.it",
                "via monginevro 20", "10", "+39389939339",
                "1", ""

            ), true
        ),
        onProfileValueChange ={},
        onSaveClick ={},
        modifier = Modifier,
        navigateToProfileDestination ={},


    )
}

