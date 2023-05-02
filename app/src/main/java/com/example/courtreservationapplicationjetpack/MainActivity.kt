package com.example.courtreservationapplicationjetpack

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.courtreservationapplicationjetpack.ui.theme.CourtReservationApplicationJetpackTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CourtReservationApplicationJetpackTheme {

                    CourtApp()


            }
        }
    }
}
