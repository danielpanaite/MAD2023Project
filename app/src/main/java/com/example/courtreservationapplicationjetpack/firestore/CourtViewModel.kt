package com.example.courtreservationapplicationjetpack.firestore

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FieldPath
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
    private var _reservationcourts = mutableStateOf<List<Court>>(emptyList())
    val courts: State<List<Court>> = _courts
    val court: MutableState<Court> = _court
    val reservationcourts: State<List<Court>> = _reservationcourts

    //----------------------Methods----------------------

    fun getCourtsBySport(sport: String) {
        // Creating a reference to collection
        val docRef = db.collection("courts").whereEqualTo("sport", sport).whereEqualTo("citta", "Torino")

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

        docRef.get().addOnSuccessListener { documentSnapshot ->
            Log.d(TAG, "getCourtById")
            val court = documentSnapshot.toObject(Court::class.java)
            court?.id = documentSnapshot.id
            _court.value = court!!
        }.addOnFailureListener { exception ->
            Log.d(TAG, "Error getting data", exception)
        }
    }

    fun getReservationCourts(courts: List<String>) {
        // Creating a reference to document by id
        val docRef = db.collection("courts").whereIn(FieldPath.documentId(), courts)

        docRef.get().addOnSuccessListener {
            Log.d(TAG, "getReservationCourts")
            val list = mutableListOf<Court>()
            for (document in it.documents) {
                val res = document.toObject(Court::class.java)
                res?.id = document.id // Map the document ID to the "id" property of the Reservation object
                res?.let { r -> list.add(r) }
            }
            _reservationcourts.value = list
        }.addOnFailureListener {
            Log.d(TAG, "Error getting data", it)
        }
    }

    //BinHexs

}