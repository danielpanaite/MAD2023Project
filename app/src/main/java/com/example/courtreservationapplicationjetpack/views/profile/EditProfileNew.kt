package com.example.courtreservationapplicationjetpack.views.profile

import android.Manifest
import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.foundation.Image
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
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.transform.CircleCropTransformation
import com.example.courtreservationapplicationjetpack.CourtTopAppBar
import com.example.courtreservationapplicationjetpack.R
import com.example.courtreservationapplicationjetpack.components.BottomBar
import com.example.courtreservationapplicationjetpack.firestore.UserViewModel
import com.example.courtreservationapplicationjetpack.firestore.Users
import com.example.courtreservationapplicationjetpack.navigation.NavigationDestination
import com.example.courtreservationapplicationjetpack.signIn.GoogleAuthUiClient
import java.io.ByteArrayOutputStream
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date


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
    var launchOnce by rememberSaveable { mutableStateOf(true) }
    if (launchOnce) {
        viewModel.getUserByEmail(profileArg!!)
        launchOnce = false
    }
    val userDetails by remember { mutableStateOf(viewModel.user) } //reservation to be edited


    val profileImageUrl = remember { mutableStateOf("") }

    val myImage: Bitmap = BitmapFactory.decodeResource(Resources.getSystem(), android.R.mipmap.sym_def_app_icon)
    val chosenPhoto = remember { mutableStateOf<Bitmap>(myImage) }

    Log.d("chosenPhoto", "$chosenPhoto")

    val imageChoosen = remember { mutableStateOf(false) }




    //val chosenPhotoUri = remember { mutableStateOf<Uri?>(null) }

    // Recupera l'URL dell'immagine del profilo dall'oggetto UserDetails
    LaunchedEffect(userDetails) {
        userDetails.value.imageUri?.let { imageUri ->
            profileImageUrl.value = imageUri
        }
        Log.d("dentro launch effect", "${userDetails.value.imageUri}")
        Log.d("profile imageUrl", "${profileImageUrl}")

    }
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ){
        if (it) {
            Toast.makeText(context, "Permission Granted", Toast.LENGTH_SHORT).show()

        } else {
            Toast.makeText(context, "Permission Denied", Toast.LENGTH_SHORT).show()
        }
    }

    Scaffold(
        topBar = {
            CourtTopAppBar(
                canNavigateBack = true,
                navigateUp = onNavigateUp,
                text = "Edit Profile"
            )
        },
        bottomBar = { BottomBar(navController = navController as NavHostController) }
    ) { innerPadding ->
        ProfileEntryBody(
            user = userDetails,
            navigateToProfileDestination = navigateToProfileDestination,
            onSaveClick = {

                val baos = ByteArrayOutputStream()
                chosenPhoto.value.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                val data = baos.toByteArray()
                viewModel.updateProfile(data, imageChoosen)
if(imageChoosen.value){
    viewModel.uploadImageToStorage(data)

}

                navigateToProfileDestination()
            },
            //chosenPhotoUri = chosenPhotoUri,
            chosenPhoto = chosenPhoto,
            modifier = modifier.padding(innerPadding),
            profileImageUrl = profileImageUrl.value,
            imageChoosen = imageChoosen
        )
    }


}

@Composable
fun ProfileEntryBody(
    user: MutableState<Users>,
    imageChoosen: MutableState<Boolean>,
    //chosenPhotoUri: MutableState<Uri?>,
    chosenPhoto: MutableState<Bitmap>,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier,
    navigateToProfileDestination: () -> Unit,
    profileImageUrl: String,
    viewModel: UserViewModel = viewModel()

) {
    val context = LocalContext.current
    //val chosenPhotoUriState = rememberUpdatedState(chosenPhotoUri.value)

    var showErrorDialog by remember { mutableStateOf(false) }

    /*
    LaunchedEffect(chosenPhotoUri.value) {
        Log.d("launch eff chosenPhoto", "${chosenPhotoUri.value}")

        user.value = user.value.copy(imageUri = chosenPhotoUri.value?.toString())
    }

     */

    val phonePattern = Regex("^\\+(?:[0-9]●?){6,14}[0-9]$")

    val agePattern = Regex("^[0-9]+$")



    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .padding(1.dp),
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        item {
            ProfileInputForm(user = user,
                chosenPhoto = chosenPhoto,
                onSaveClick = onSaveClick,
                profileImageUrl = profileImageUrl,
                imageChoosen = imageChoosen

                )
        }
        item {
            Button(
                onClick = {
// Verifica se il numero di telefono corrisponde al pattern
                    val isValidPhoneNumber = user.value.phone?.let { phonePattern.matches(it) }
                    if (user.value.name != "" && user.value.email != "" &&
                        (user.value.age!=null && user.value.age!! >13)
                        && (user.value.phone!="" && isValidPhoneNumber == true)
                        && agePattern.matches(user.value.age.toString()) == true

                    ) {
                        onSaveClick()

                        // Carica l'immagine nello storage di Firebase
                        //viewModel.uploadImageToStorage(context, chosenPhotoUri.value)
                        //navigateToProfileDestination()
                    } else {
                        showErrorDialog = true
                    }
                },
                modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp)
            ) {
                Text(text = "Save")
            }

        }
    }

    if (showErrorDialog) {
        AlertDialog(
            onDismissRequest = { showErrorDialog = false },
            title = { Text(text = "Error") },
            text = if (user.value.name == "") {
                { Text(text = "Please insert at least a name") }
            } else if (!user.value.email.matches(Regex("\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}\\b"))) {
                { Text(text = "Insert a valid email") }
            }else if (agePattern.matches(user.value.age.toString())== false) {
                { Text(text = "Choose a valid age ") }
            }else if (user.value.age!! <14) {
                    { Text(text = "You need to be at least 14 to use the app ") }
            }else if(user.value.phone?.let { phonePattern.matches(it) } == false){
                {Text(text = "Insert a valid phone number with the correct prefix")}
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
    imageChoosen: MutableState<Boolean>,
    chosenPhoto: MutableState<Bitmap>,
    modifier: Modifier = Modifier,
    profileImageUrl: String, // URL dell'immagine del profilo
    enabled: Boolean = true
) {

    val context = LocalContext.current
    val loadImageCamera = rememberLauncherForActivityResult(ActivityResultContracts.TakePicturePreview()) {
        if (it != null) {
            chosenPhoto.value = it
            imageChoosen.value = true
        }
    }

    val loadImageGal = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            if (Build.VERSION.SDK_INT < 29) {
                chosenPhoto.value = MediaStore.Images.Media.getBitmap(context.contentResolver, it)
                imageChoosen.value = true
            } else {
                val source = it.let { uri -> ImageDecoder.createSource(context.contentResolver, uri) }
                chosenPhoto.value = source?.let { it1 -> ImageDecoder.decodeBitmap(it1) }!!
                imageChoosen.value = true
            }
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {
        if (it) {
            Toast.makeText(context, "Permission Granted", Toast.LENGTH_SHORT).show()
            loadImageCamera.launch()
        } else {
            Toast.makeText(context, "Permission Denied", Toast.LENGTH_SHORT).show()
        }
    }
    val showMenu = remember { mutableStateOf(false) }

    Box(
        modifier = Modifier.fillMaxSize(),
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp),
            contentAlignment = Alignment.Center
        ) {
            Log.d("imageChosen.value", "${imageChoosen.value}")

            if(imageChoosen.value){
                    // Mostra l'immagine selezionata dall'utente
                    Log.d("chosenPhoto !=null", "${chosenPhoto.value}")
                    Image(chosenPhoto.value.asImageBitmap(), contentDescription = "image",
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop,
                        colorFilter = if (imageChoosen.value && profileImageUrl == "") {
                            ColorFilter.tint(Color.Black.copy(alpha = 0.3f))
                        } else {
                            null
                        })

                } else if (profileImageUrl !=="") {
                    // Mostra l'immagine del profilo esistente
                         Image(painter = rememberAsyncImagePainter(
                        ImageRequest.Builder(LocalContext.current)
                            .data(Uri.parse(profileImageUrl))
                            .apply <ImageRequest.Builder>(block = fun ImageRequest.Builder.() {
                                crossfade(true)
                                placeholder(R.drawable.baseline_person_24)
                                transformations(CircleCropTransformation())
                            }).build()

                    )
                         , contentDescription = "Selected Image",
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape),
                colorFilter = if (imageChoosen.value && profileImageUrl == "") {
                    ColorFilter.tint(Color.Black.copy(alpha = 0.3f))
                } else {
                    null
                })
                } else {
                    // Mostra un'immagine di default
                    Log.d("non c'è nessuna foto", "${user.value.imageUri}")
                   Image(painter = rememberAsyncImagePainter(
                        ImageRequest.Builder(LocalContext.current)
                            .data(data = R.drawable.baseline_person_24)
                            .apply<ImageRequest.Builder>(block = fun ImageRequest.Builder.() {
                                crossfade(true)
                                placeholder(R.drawable.baseline_person_24)
                                transformations(CircleCropTransformation())
                            }).build(),
                    )
                       , contentDescription = "Selected Image",
                       modifier = Modifier
                           .size(100.dp)
                           .clip(CircleShape),
                       colorFilter = if (chosenPhoto == null && profileImageUrl == "") {
                           ColorFilter.tint(Color.Black.copy(alpha = 0.3f))
                       } else {
                           null
                       })

                }
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
            DropdownMenu(
                expanded = showMenu.value,
                onDismissRequest = { showMenu.value = false },
                modifier = Modifier
                    .wrapContentSize()
            ) {
                DropdownMenuItem(
                    onClick = {
                        showMenu.value = false
                        loadImageGal.launch("image/*")
                    },
                    text = { Text("Select Image from gallery", color = Color.Black) },
                )
                DropdownMenuItem(
                    onClick = {
                        showMenu.value = false
                        permissionLauncher.launch(Manifest.permission.CAMERA)
                    },
                    text = { Text("Take a photo from camera", color = Color.Black) },
                )
            }
        }



        Column(
            modifier = Modifier
                .padding(8.dp)
                .padding(top = 130.dp)
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ){
                user.value.name?.let {
                    OutlinedTextField(
                        value = it,
                        onValueChange = { user.value = user.value.copy(name = it) },
                        label = { Text(text = "Name") },
                        modifier = Modifier
                            .fillMaxWidth(),
                        enabled = enabled,
                        singleLine = true
                    )
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ){
                user.value.nickname?.let {
                    OutlinedTextField(
                        value = it,
                        onValueChange = { user.value = user.value.copy(nickname = it) },
                        label = { Text(text = "Nickname") },
                        modifier = Modifier
                            .fillMaxWidth(),
                        enabled = enabled,
                        singleLine = true
                    )
                }
            }

            /*Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ){
                OutlinedTextField(
                    value = user.value.email,
                    onValueChange = { user.value = user.value.copy(email = it) },
                    label = { Text(text = "EMAIL") },
                    modifier = Modifier
                        .fillMaxWidth(),
                    enabled = enabled,
                    singleLine = true
                )
            }
            */
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ){
                user.value.address?.let {
                    OutlinedTextField(
                        value = it,
                        onValueChange = { user.value = user.value.copy(address = it) },
                        label = { Text(text = "Address") },
                        modifier = Modifier
                            .fillMaxWidth(),
                        enabled = enabled,
                        singleLine = true
                    )
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ){
                OutlinedTextField(
                    value = user.value.phone.toString(),
                    onValueChange = { user.value = user.value.copy(phone = it) },
                    label = { Text(text = "Phone") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    enabled = enabled
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ){

                OutlinedTextField(
                    value = user.value.age?.toString() ?: "",
                    onValueChange = { input ->
                        val age = if (input.isNotEmpty()) input.toIntOrNull() else null
                        user.value = user.value.copy(age = age)
                    },
                    label = { Text(text = "Age") },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = enabled
                )
            }




        }


    }
}

fun Context.createImageFile(): File {
    // Create an image file name
    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
    val imageFileName = "JPEG_" + timeStamp +"_"
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


/* funziona con imageBitmap:
val context = LocalContext.current
    val myImage: Bitmap = BitmapFactory.decodeResource(Resources.getSystem(), android.R.mipmap.sym_def_app_icon)
    val result = remember { mutableStateOf<Bitmap>(myImage) }
    val loadImage = rememberLauncherForActivityResult(ActivityResultContracts.TakePicturePreview()) {
        if (it != null) {
            result.value = it
        }

    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(result.value.asImageBitmap(), contentDescription = "image",
        modifier = Modifier
            .size(300.dp)
            .padding(10.dp))
        OutlinedButton(onClick = {loadImage.launch()},
        modifier = Modifier.fillMaxWidth()
        ){
            Text(text = "Click Image", fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.primary)
                    .fillMaxWidth()
            )
        }
    }
 */