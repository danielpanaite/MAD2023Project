package com.example.courtreservationapplicationjetpack.views.profile

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.MediaStore
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
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.transform.CircleCropTransformation
import com.example.courtreservationapplicationjetpack.BuildConfig
import com.example.courtreservationapplicationjetpack.CourtTopAppBar
import com.example.courtreservationapplicationjetpack.R
import com.example.courtreservationapplicationjetpack.components.BottomBar
import com.example.courtreservationapplicationjetpack.firestore.Reservation
import com.example.courtreservationapplicationjetpack.firestore.UserViewModel
import com.example.courtreservationapplicationjetpack.firestore.Users
import com.example.courtreservationapplicationjetpack.navigation.NavigationDestination
import com.example.courtreservationapplicationjetpack.signIn.GoogleAuthUiClient
import com.example.courtreservationapplicationjetpack.ui.appViewModel.AppViewModelProvider
import com.google.firebase.Timestamp
import kotlinx.coroutines.launch
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Objects


object EditProfileDestination : NavigationDestination {
    override val route = "profile_edit"
    override val titleRes = "edit Profile"
    override val icon = Icons.Default.Edit
    const val profileArg = "profileArg"
    val routeWithArgs = "$route/{$profileArg}"

}
@ExperimentalMaterial3Api
@Composable
fun EditProfile(
    modifier: Modifier = Modifier,
    navController: NavController,
    navigateBack: () -> Unit,
    onNavigateUp: () -> Unit,
    navigateToProfileDestination: () -> Unit,
    context: Context,
    googleAuthUiClient: GoogleAuthUiClient,
    profileArg: String?,

    viewModel: UserViewModel = viewModel()
) {
    val coroutineScope = rememberCoroutineScope()
    var launchOnce by rememberSaveable { mutableStateOf(true) }
    if (launchOnce) {
        viewModel.getUserByEmail(profileArg!!)
        Log.d("profileArg", "$profileArg")
        launchOnce = false
    }
    val userDetails by remember { mutableStateOf(viewModel.user) } //reservation to be edited
    Log.d("profile ui state", "${viewModel.user}")

    val chosenPhotoUri = remember { mutableStateOf<Uri?>(null) }

    val profileImageUrl = remember { mutableStateOf("") }

    // Recupera l'URL dell'immagine del profilo dall'oggetto UserDetails
    LaunchedEffect(userDetails) {
        userDetails.value.imageUri?.let { imageUri ->
            profileImageUrl.value = imageUri
        }
    }


    Scaffold(
        topBar = {
            CourtTopAppBar(
                canNavigateBack = true,
                navigateUp = onNavigateUp
            )
        },
        bottomBar = { BottomBar(navController = navController as NavHostController) }
    ) { innerPadding ->
        ProfileEntryBody(
            user = userDetails,
            navigateToProfileDestination = navigateToProfileDestination,
            onSaveClick = {
                viewModel.updateProfile(chosenPhotoUri.value)
                //saveImageToStorage(context, userDetails.value.imageUri)
                viewModel.uploadImageToStorage(context, chosenPhotoUri.value)

                navigateToProfileDestination()
            },
            chosenPhotoUri = chosenPhotoUri,
            modifier = modifier.padding(innerPadding),
            profileImageUrl = profileImageUrl
        )
    }
}

@Composable
fun ProfileEntryBody(
    user: MutableState<Users>,
    chosenPhotoUri: MutableState<Uri?>,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier,
    navigateToProfileDestination: () -> Unit,
    profileImageUrl: MutableState<String>,

    viewModel: UserViewModel = viewModel()

) {

    val context = LocalContext.current
    val chosenPhotoUriState = rememberUpdatedState(chosenPhotoUri.value)

    var showErrorDialog by remember { mutableStateOf(false) }

    LaunchedEffect(chosenPhotoUri.value) {
        user.value = user.value.copy(imageUri = chosenPhotoUri.value?.toString())
    }

    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .padding(1.dp),
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        item {
            ProfileInputForm(user = user, chosenPhotoUri=chosenPhotoUri, onSaveClick = onSaveClick, profileImageUrl = profileImageUrl)
        }
        item {
            Button(
                onClick = {
                    if (user.value.name != "" && user.value.email != "") {
                        onSaveClick()

                        // Carica l'immagine nello storage di Firebase
                        viewModel.uploadImageToStorage(context, chosenPhotoUri.value)

                        navigateToProfileDestination()
                    } else {
                        showErrorDialog = true
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "EDIT PROFILE")
            }

        }
    }

    if (showErrorDialog) {
        AlertDialog(
            onDismissRequest = { showErrorDialog = false },

            title = { Text(text = "Error") },
            text = if (user.value.name == "") {
                { Text(text = "Please insert at least a name and an email") }
            } else if (!user.value.email.matches(Regex("\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}\\b"))) {
                { Text(text = "Insert a valid email") }

            } else {
                { Text(text = "I valori inseriti non sono validi.") }

            },
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
    user: MutableState<Users>,
    onSaveClick: () -> Unit,
    chosenPhotoUri: MutableState<Uri?>,
    modifier: Modifier = Modifier,
    profileImageUrl: MutableState<String>,
    enabled: Boolean = true
) {

    val context = LocalContext.current
    val file = context.createImageFile()
    val uri = FileProvider.getUriForFile(
        Objects.requireNonNull(context),
        BuildConfig.APPLICATION_ID + ".provider", file
    )

    var photoUri by rememberSaveable { mutableStateOf<Uri?>(Uri.parse(user.value.imageUri)) }
    var chosenPhoto by rememberSaveable { mutableStateOf<Uri?>(null) }

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        chosenPhotoUri.value = uri
        user.value = user.value.copy(imageUri = uri.toString())
    }

    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) {
        chosenPhotoUri.value = uri
        user.value = user.value.copy(imageUri = uri.toString())
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

    val showMenu = remember { mutableStateOf(false) }

    LaunchedEffect(chosenPhoto) {
        user.value = user.value.copy(imageUri = chosenPhoto.toString())
    }

    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp),
            color = Color.White,
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
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
                        if (profileImageUrl!==null) {
                            rememberAsyncImagePainter(
                                ImageRequest.Builder(LocalContext.current)
                                    .data(data = profileImageUrl)
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
                    colorFilter = if (chosenPhoto == null && profileImageUrl===null) {
                        ColorFilter.tint(Color.Black.copy(alpha = 0.3f))
                    } else {
                        null
                    }
                )
                IconButton(
                    onClick = { showMenu.value = true },
                    modifier = Modifier
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_camera),
                        contentDescription = "camera icon",
                        tint = Color.Black
                    )
                }
            }
        }

        DropdownMenu(
            expanded = showMenu.value,
            onDismissRequest = { showMenu.value = false },
            modifier = Modifier
                .padding(16.dp)
                .wrapContentSize()
        ) {
            DropdownMenuItem(
                onClick = {
                    showMenu.value = false
                    //chosenPhotoUri.value = null
                    launcher.launch("image/*")
                },
                text = { Text("Select Image from gallery", color = Color.Black) },
            )
            DropdownMenuItem(
                onClick = {
                    showMenu.value = false
                    //chosenPhotoUri.value = null
                    permissionLauncher.launch(Manifest.permission.CAMERA)
                },
                text = { Text("take a photo from camera", color = Color.Black) },
            )
        }

        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp)
                .padding(top = 100.dp)
        ) {
            user.value.name?.let {
                TextField(
                    value = it,
                    onValueChange = { user.value = user.value.copy(name = it) },
                    label = { Text(text = "Name") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    enabled = enabled
                )
            }
            user.value.nickname?.let {
                TextField(
                    value = it,
                    onValueChange = { user.value = user.value.copy(nickname = it) },
                    label = { Text(text = "Nickname") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    enabled = enabled
                )
            }
            TextField(
                value = user.value.email,
                onValueChange = { user.value = user.value.copy(email = it) },
                label = { Text(text = "Email") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                enabled = enabled
            )
            user.value.address?.let {
                TextField(
                    value = it,
                    onValueChange = { user.value = user.value.copy(address = it) },
                    label = { Text(text = "Address") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    enabled = enabled
                )
            }
            TextField(
                value = user.value.age.toString(),
                onValueChange = { user.value = user.value.copy(age = it.toIntOrNull()) },
                label = { Text(text = "Age") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                enabled = enabled
            )

        }
    }
}

fun Context.createImageFile(): File {
    // Create an image file name
    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
    val imageFileName = "JPEG-$timeStamp.jpg"
    val image = File.createTempFile(
        imageFileName, /* prefix */
        ".jpg", /* suffix */
        externalCacheDir      /* directory */
    )
    return image
}

/*
fun saveImageToStorage(context: Context, uri: Uri?) {
    if (uri != null) {
        val sourceFile = File(uri.path!!)
        if (sourceFile.exists()) {
            val destinationFile = File(context.filesDir, "profile_image.jpg")
            sourceFile.copyTo(destinationFile, overwrite = true)
        } else {
            // Il file di origine non esiste
            Log.e("saveImageToStorage", "Source file doesn't exist: $uri")
        }
    }
}
*/
