package com.example.courtreservationapplicationjetpack.models.sport

import com.example.courtreservationapplicationjetpack.R

class SportDrawables() {
    companion object{
        fun getDrawable(sport: String): Int?{
            val d = when (sport){
                "Tennis" -> R.drawable.baseline_sports_tennis_24
                "Pallamano" -> R.drawable.pallamano
                "Beach volley" -> R.drawable.ic_beachvolley
                "Basket" -> R.drawable.ic_basket
                "Calcio" -> R.drawable.baseline_sports_soccer_24
                else -> null
            }
            return d
        }
    }
}