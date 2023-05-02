package com.example.courtreservationapplicationjetpack.routes

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.graphics.Color


@ExperimentalMaterial3Api
@Composable
fun EditProfile(){

    val notification = rememberSaveable { mutableStateOf("") }
    if(notification.value.isNotEmpty()){
        Toast.makeText(LocalContext.current, notification.value, Toast.LENGTH_LONG).show()
        notification.value = ""
    }

    var fullName by rememberSaveable{
        mutableStateOf("default name")
    }
    var username by rememberSaveable{
        mutableStateOf("default username")
    }
    var email by rememberSaveable{
        mutableStateOf("default email")
    }
    var phoneNumber by rememberSaveable{
        mutableStateOf("default phone number")
    }
    var birthDate by rememberSaveable{
        mutableStateOf("default birth date")
    }
    var sex by rememberSaveable{
        mutableStateOf("default sex")
    }
    var place by rememberSaveable{
        mutableStateOf("default place play")
    }



    Column(modifier = Modifier
        .verticalScroll(rememberScrollState())
        .padding(8.dp)
        .padding(vertical = 60.dp)
    ){
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween

        ){
            Text(text = "Cancel", modifier = Modifier.clickable { notification.value = "cancelled" })
            Text(text = "Save", modifier = Modifier.clickable { notification.value = "Profile Updated" })

        }

        EditProfileImage()

        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(start = 4.dp, end = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Name", modifier = Modifier.width(100.dp))
            TextField(value = fullName, onValueChange = {fullName =it},
                colors = TextFieldDefaults.textFieldColors(
                    textColor = Color.Black,

                    )
            )
        }
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(start = 4.dp, end = 4.dp)
            .padding(top = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Username", modifier = Modifier.width(100.dp).padding(top = 8.dp))
            TextField(value = username, onValueChange = {username =it},
                colors = TextFieldDefaults.textFieldColors(
                    textColor = Color.Black,

                    )
            )
        }
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(start = 4.dp, end = 4.dp)
            .padding(top = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Email", modifier = Modifier.width(100.dp))
            TextField(value = email, onValueChange = {email=it},
                colors = TextFieldDefaults.textFieldColors(
                    textColor = Color.Black,

                    )
            )
        }
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(start = 4.dp, end = 4.dp)
            .padding(top = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Phone Number", modifier = Modifier.width(100.dp))
            TextField(value = phoneNumber, onValueChange = {phoneNumber =it},
                colors = TextFieldDefaults.textFieldColors(
                    textColor = Color.Black,

                    )
            )
        }
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(start = 4.dp, end = 4.dp)
            .padding(top = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Sex", modifier = Modifier.width(100.dp))
            TextField(value = sex, onValueChange = {sex =it},
                colors = TextFieldDefaults.textFieldColors(
                    textColor = Color.Black,

                    )
            )
        }
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(start = 4.dp, end = 4.dp)
            .padding(top = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Place", modifier = Modifier.width(100.dp))
            TextField(value = place, onValueChange = {place =it},
                colors = TextFieldDefaults.textFieldColors(
                    textColor = Color.Black,

                    )
            )
        }
    }
}

@Composable
fun EditProfileImage(){
    val imageUri = rememberSaveable{
        mutableStateOf("")
    }
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

@ExperimentalMaterial3Api
@Composable
@Preview(showBackground = true)
fun EditProfileScreenPreview(){
    EditProfile()
}