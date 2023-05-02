package com.example.courtreservationapplicationjetpack.components

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.courtreservationapplicationjetpack.navigation.NavigationGraph
import com.example.courtreservationapplicationjetpack.navigation.Screens

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BarNavigation(
    navController: NavController
){
    Scaffold(
        bottomBar = { BottomBar(navController = navController as NavHostController)}
    ) {
        NavigationGraph(navController = navController as NavHostController)
    }
}

@Composable
fun BottomBar(navController: NavHostController){
    val screens = listOf(
        Screens.MainScreen,
        Screens.Profile,
        Screens.MyReservations
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    BottomAppBar {
        screens.forEach{
            screen ->
                AddItem(screen = screen, currentDestination = currentDestination, navController = navController)

        }
    }
}

@Composable
fun RowScope.AddItem(
    screen: Screens,
    currentDestination: NavDestination?,
    navController: NavHostController

){
    NavigationBarItem(
        label ={
            Text(text = screen.title)
        },
        icon ={
            Icon(
                imageVector= screen.icon,
                contentDescription = "Navigation Icon"
            )
        },
        selected = currentDestination?.hierarchy?.any {
             it.route== screen.route
        }==true,
        onClick = {
            navController.navigate(screen.route)
        }


    )

}