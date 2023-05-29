package com.example.courtreservationapplicationjetpack.firestore

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class CourtViewModel: ViewModel() {

    private val db = Firebase.firestore
    private lateinit var regc: ListenerRegistration

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
        val docRef = db.collection("oldcourts").whereEqualTo("sport", sport)

        // Listen to data in real-time
        regc = docRef.addSnapshotListener { snapshot, e ->
            if (e != null)
                Log.d(TAG, "Error getting data", e)
            if (snapshot != null) {
                val list = mutableListOf<Court>()
                for (document in snapshot.documents) {
                    val res = document.toObject(Court::class.java)
                    res?.id = document.id // Map the document ID to the "id" property of the Reservation object
                    res?.let { list.add(it) }
                }
                _courts.value = list
            }
        }
    }

    fun getCourtById(id: String) {
        // Creating a reference to document by id
        val docRef = db.document("oldcourts/$id")

        // Listen to data in real-time
        regc = docRef.addSnapshotListener { snapshot, e ->
            if (e != null)
                Log.d(TAG, "Error getting data", e)
            if (snapshot != null) {
                val res = snapshot.toObject(Court::class.java)
                res?.id = snapshot.id // Map the document ID to the "id" property of the Reservation object
                _court.value = res!!
            }
        }
    }

    //-----------------------------------------------------

    //allows the listener to be removed when the viewModel is destroyed
    override fun onCleared() {
        super.onCleared()
        regc.remove()
        Log.d(TAG, "Registration removed")
    }

}