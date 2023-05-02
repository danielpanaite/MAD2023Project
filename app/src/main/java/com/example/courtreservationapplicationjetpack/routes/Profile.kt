package com.example.courtreservationapplicationjetpack.routes

import androidx.compose.foundation.Image
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
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.example.courtreservationapplicationjetpack.R
import com.example.courtreservationapplicationjetpack.navigation.Screens


@ExperimentalMaterial3Api
@Composable
fun Profile(
    navController: NavController
){

    var fullName = "Laura"
    var username = "lau"
    var email = "lau@gmail.com"
    var phoneNumber = "3420342424"
    var birthDate = "02/12/1996"
    var sex = "female"
    var place = "Turin"


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
            Button(onClick = { navController.navigate(route = Screens.EditProfile.route) }) {
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


@ExperimentalMaterial3Api
@Composable
@Preview(showBackground = true)
fun ProfileScreenPreview(){
    Profile(navController = rememberNavController())
}