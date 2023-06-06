package com.example.courtreservationapplicationjetpack.firestore

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class CityViewModel: ViewModel() {
    private val db = Firebase.firestore


    private var _cities = mutableStateOf<List<City>>(emptyList())
    val cities: State<List<City>> = _cities

    companion object {
        const val TAG = "CityViewModel"
    }

    fun getCities() {
        // Creating a reference to collection
        val docRef = db.collection("cities")

        docRef.get().addOnSuccessListener {
            Log.d(CourtViewModel.TAG, "getCities")
            val list = mutableListOf<City>()
            for (document in it.documents) {
                val res = document.toObject(City::class.java)
                res?.id = document.id // Map the document ID to the "id" property of the City object
                res?.let { r -> list.add(r) }
            }
            _cities.value = list
        }.addOnFailureListener {
            Log.d(CourtViewModel.TAG, "Error getting data", it)
        }
    }
}