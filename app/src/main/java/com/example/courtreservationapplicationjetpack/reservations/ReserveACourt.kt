package com.example.courtreservationapplicationjetpack.reservations

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController


@ExperimentalMaterial3Api
@Composable
fun ReserveACourt(

   // viewModel: ReservationEntryViewModel = viewModel(factory = AppViewModelProvider.Factory)
){

    val coroutineScope = rememberCoroutineScope()


}

@ExperimentalMaterial3Api
@Composable
@Preview(showBackground = true)
fun ReserveACourtPreview(){
    ReserveACourt()
}