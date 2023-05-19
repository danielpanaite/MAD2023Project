package com.example.courtreservationapplicationjetpack.views.courts

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Place
import com.example.courtreservationapplicationjetpack.navigation.NavigationDestination

object CourtReservationSummaryDestination : NavigationDestination {
    override val route  = "court_summary"
    override val titleRes = "Court Reservation Summary"
    override val icon = Icons.Default.Place

}