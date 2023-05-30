package com.example.courtreservationapplicationjetpack.firestore

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class CourtViewModel: ViewModel() {

    private val db = Firebase.firestore

    companion object{
        const val TAG = "CourtViewModel"
    }

    //DATA
    private var _courts = mutableStateOf<List<Court>>(emptyList())
    private var _court = mutableStateOf(Court())
    val courts: State<List<Court>> = _courts
    val court: MutableState<Court> = _court

    //----------------------Methods----------------------

    fun getCourtsBySport(sport: String) {
        // Creating a reference to collection
        val docRef = db.collection("courts").whereEqualTo("sport", sport)

        docRef.get().addOnSuccessListener {
            Log.d(TAG, "getCourtsBySport")
            val list = mutableListOf<Court>()
            for (document in it.documents) {
                val res = document.toObject(Court::class.java)
                res?.id = document.id // Map the document ID to the "id" property of the Reservation object
                res?.let { r -> list.add(r) }
            }
            _courts.value = list
        }.addOnFailureListener {
            Log.d(TAG, "Error getting data", it)
        }
    }

    fun getCourtById(id: String) {
        // Creating a reference to document by id
        val docRef = db.document("courts/$id")

        docRef.get().addOnSuccessListener {
            Log.d(TAG, "getCourtById")
            val res = it.toObject(Court::class.java)
            res?.id = it.id // Map the document ID to the "id" property of the Reservation object
            _court.value = res!!
        }.addOnFailureListener {
            Log.d(TAG, "Error getting data", it)
        }
    }

}