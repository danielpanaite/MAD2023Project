package com.example.courtreservationapplicationjetpack.navigation

import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Interface to describe the navigation destinations for the app
 */
interface NavigationDestination {
    /**
     * Unique name to define the path for a composable
     */
    val route: String
    /*
     * String resource id to that contains title to be displayed for the bottom nav .
     */
    val titleRes: String
    val icon: ImageVector
}