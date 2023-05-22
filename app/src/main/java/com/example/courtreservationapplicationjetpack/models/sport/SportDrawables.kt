package com.example.courtreservationapplicationjetpack.models.sport

import com.example.courtreservationapplicationjetpack.R

class SportDrawables() {
    companion object{
        fun getDrawable(sport: String): Int?{
            val d = when (sport){
                "tennis" -> R.drawable.baseline_sports_tennis_24
                "pallamano" -> R.drawable.pallamano
                "beach volley" -> R.drawable.ic_beachvolley
                "basket" -> R.drawable.ic_basket
                "calcio" -> R.drawable.baseline_sports_soccer_24
                else -> null
            }
            return d
        }
    }
}