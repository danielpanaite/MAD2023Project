package com.example.courtreservationapplicationjetpack.firestore

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.Objects

class UserViewModel: ViewModel(){

    private val db = Firebase.firestore
    private lateinit var reg1: ListenerRegistration
    private lateinit var reg2: ListenerRegistration
    private lateinit var reg3: ListenerRegistration

    companion object{
        const val TAG = "UserViewModel"
    }

    //DATA
    private var _users = mutableStateOf<List<Users>>(emptyList())
    private var _user = mutableStateOf(Users())
    val users: State<List<Users>> = _users
    val user: MutableState<Users> = _user

    private var _sports = mutableStateOf<List<String>>(emptyList())
    val sports: State<List<String>> = _sports

    private var _sportsWithLevels = mutableStateOf<Map<String, String>>(emptyMap())
    val sportsWithLevel: State<Map<String, String>> = _sportsWithLevels



    //----------------------Methods----------------------

    @SuppressLint("SuspiciousIndentation")
    fun getSportWithLevels(email: String) {
        // Creating a reference to collection
        Log.d("user email", "${user.value.email}")
        Log.d("user email", "${email}")

        val docRef = db.collection("users").document(email)
        docRef.get().addOnSuccessListener {documentSnapshot->
            val list = mutableListOf<Sport>()
            Log.d("docuemnt snapshot .to obejct user", "${documentSnapshot.toObject(Users::class.java)}")

            val sportPreferences = documentSnapshot.toObject(Users::class.java)?.sportPreferences
                if (sportPreferences != null) {
                    _sportsWithLevels.value = sportPreferences.associateBy { it.sportName }
                        .mapValues { it.value.masteryLevel }
                }

        }
    }

    fun getSportsList() {
        // Creating a reference to collection
        val docRef = db.collection("courts")

        docRef.get().addOnSuccessListener {
            Log.d(UserViewModel.TAG, "getListSport")
            val list = mutableListOf<String>()
            for (document in it.documents) {
                val res = document.toObject(Court::class.java)
                res?.id = document.id // Map the document ID to the "id" property of the Reservation object
                res?.let { r -> list.add(r.sport)}
            }
            _sports.value = list.toSet().toList()
        }.addOnFailureListener {
            Log.d(CourtViewModel.TAG, "Error getting data", it)
        }
    }
    fun getUserByEmail(email: String) {
        // Creating a reference to document by id
        val docRef = db.document("users/$email")

        // Listen to data in real-time
        reg3 = docRef.addSnapshotListener { snapshot, e ->
            if (e != null)
                Log.d(TAG, "Error getting data", e)
            if (snapshot != null) {
                Log.d(TAG, "getUserByEmail")
                val res = snapshot.toObject(Users::class.java)
                res?.id = snapshot.id // Map the document ID to the "id" property of the Reservation object
                _user.value = res!!


            }
        }
    }


    fun updateProfile(){
        // Creating a reference to document by id
        val docRef = db.document("users/${user.value.email}")

        Log.d("inside updateProfile, user.value.id", "${user.value.id}")
        Log.d("inside updateProfile, user.value.name", "${user.value.name}")
        Log.d("inside updateProfile, user.value.nickname", "${user.value.nickname}")
        Log.d("inside updateProfile, user.value.email", "${user.value.email}")
        Log.d("inside updateProfile, user.value.address", "${user.value.address}")
        Log.d("inside updateProfile, user.value.age", "${user.value.age}")
        Log.d("inside updateProfile, user.value.phone", "${user.value.phone}")
        Log.d("inside updateProfile, user.value.imageUri", "${user.value.imageUri}")

        val hash = hashMapOf<String, Any>(
            "id" to user.value.id,
            "name" to user.value.name.toString(),
            "nickname" to user.value.nickname.toString(),
            "email" to user.value.email,
            "address" to user.value.address.toString(),
            "age" to (user.value.age?.toInt() ?: 0),
            "phone" to user.value.phone.toString(),
            "imageUri" to user.value.imageUri.toString(),
            "sportPreferences" to user.value.sportPreferences
            )


        docRef.update(hash).addOnSuccessListener {
            Log.d(TAG, "Document ${user.value.id} updated successfully")
        }.addOnFailureListener {
            Log.d(TAG, "Failed to update document ${user.value.id}")
        }
    }








    //-----------------------------------------------------

    //allows the listener to be removed when the viewModel is destroyed
    override fun onCleared() {
        super.onCleared()
        if(this::reg1.isInitialized)
            reg1.remove()
        if(this::reg2.isInitialized)
            reg2.remove()
        if(this::reg3.isInitialized)
            reg3.remove()
        Log.d(TAG, "Registration removed")
    }

}