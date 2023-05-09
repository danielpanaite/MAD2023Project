package com.example.courtreservationapplicationjetpack.home

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.Icons.Default
import androidx.compose.material.icons.filled.Home

import androidx.compose.material3.*
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.courtreservationapplicationjetpack.CourtTopAppBar
import com.example.courtreservationapplicationjetpack.components.BottomBar
import com.example.courtreservationapplicationjetpack.navigation.NavigationDestination

//import com.example.courtreservationapplicationjetpack.navigation.Screens


object MainScreenDestination : NavigationDestination {
    override val route = "home"
    override val titleRes = "Home"
    override val icon = Icons.Default.Home

}

@ExperimentalMaterial3Api
@Composable
fun MainScreen(
    navigateToAllSports: () -> Unit,
    navController: NavController,
    modifier: Modifier = Modifier,
) {

    Scaffold(
        topBar = {
            CourtTopAppBar(canNavigateBack = false)
        },
        bottomBar = { BottomBar(navController = navController as NavHostController)}

    ) {
            innerPadding ->
        HomeBody(
            navController = rememberNavController(),
            //navController = navController,
            modifier = modifier.padding(innerPadding),
            navigateToAllSports = navigateToAllSports
        )
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeBody(
    navController: NavController = rememberNavController(),
    modifier: Modifier = Modifier,
    navigateToAllSports: () -> Unit
){
    Card(
        //onClick = {navController.navigate(route = Screens.ReserveACourt.route)},
        //onClick = navigateToReserveACourt,
        onClick = navigateToAllSports,


        modifier = Modifier
            .height(280.dp)
            .fillMaxWidth()
            .padding(20.dp)
            .padding(top = 60.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,

            ),
    ) {
        Image(
            painter = rememberAsyncImagePainter
                (
                model ="https://images.unsplash.com/photo-1511415221243-04dab195b318?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=908&q=80",

                ), contentDescription = null,

            modifier = Modifier
                .clip(MaterialTheme.shapes.medium)
                .fillMaxWidth()
                .height(100.dp)
                .aspectRatio(4f / 3f)
        )
        Column(
            modifier = Modifier.padding(8.dp)
        ) {
            Text(
                text = "Reserve a court",
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "check to see all the available courts",
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(4.dp))

        }
    }
}

/*
@ExperimentalMaterial3Api
@Composable
@Preview(showBackground = true)
fun MainScreenPreview(){
    MainScreen(navController = rememberNavController())
}

*/