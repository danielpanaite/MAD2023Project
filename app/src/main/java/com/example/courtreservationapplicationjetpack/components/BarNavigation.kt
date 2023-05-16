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
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.courtreservationapplicationjetpack.views.MainScreenDestination
import com.example.courtreservationapplicationjetpack.navigation.NavigationDestination
import com.example.courtreservationapplicationjetpack.navigation.NavigationGraph
import com.example.courtreservationapplicationjetpack.views.reservations.MyReservationsDestination
import com.example.courtreservationapplicationjetpack.views.profile.ProfileDestination

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
        MainScreenDestination,
        ProfileDestination,
        MyReservationsDestination
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
    screen: NavigationDestination,
    currentDestination: NavDestination?,
    navController: NavHostController

){
    NavigationBarItem(
        label ={
            Text(text = screen.titleRes)
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