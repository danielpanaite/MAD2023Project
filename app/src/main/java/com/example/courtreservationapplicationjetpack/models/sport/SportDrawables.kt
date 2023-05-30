package com.example.courtreservationapplicationjetpack.models.sport

import com.example.courtreservationapplicationjetpack.R

class SportDrawables() {
    companion object{
        fun getDrawable(sport: String): Int{
            val d = when (sport){
                "calcio" -> R.drawable.ic_calcio5
                "basket" -> R.drawable.ic_basket
                "beach volley" -> R.drawable.ic_beachvolley
                "pallavolo" -> R.drawable.ic_volley
                "tennis" -> R.drawable.ic_tennis
                "pallamano" -> R.drawable.pallamano
                "rugby" -> R.drawable.ic_rugby
                "softball" -> R.drawable.ic_softball
                else ->  R.drawable.ic_question_mark
            }
            return d
        }
    }
}