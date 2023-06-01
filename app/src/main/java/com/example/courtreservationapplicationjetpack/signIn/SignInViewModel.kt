package com.example.courtreservationapplicationjetpack.signIn

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.courtreservationapplicationjetpack.firestore.Users
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update



class SignInViewModel(

    ): ViewModel(

) {


    private val db = Firebase.firestore
    private lateinit var reg3: ListenerRegistration


    companion object {
        const val TAG = "SignInViewModel"
    }


    fun hasUser(): Boolean = Firebase.auth !== null

    private val _state = MutableStateFlow(SignInState())

    //we don't want to expose the mutable version to the Ui
    val state = _state.asStateFlow()


    //we call this after we get the result of the sign in
    fun onSignInResult(result: SignInResult) {
        _state.update {
            it.copy(
                isSignInSuccessful = result.data != null,
                signInError = result.errorMessage
            )
        }
    }

    fun resetState() {
        _state.update { SignInState() }
    }


    fun addUser(googleAuthUiClient: GoogleAuthUiClient) {
        val UserData = googleAuthUiClient.getSignedInUser()

        //val querySnapshot = await getDocs( query(collection(db, "Users"), where("UId", "==", user.uid)) ); return !querySnapshot.empty;

        val email = UserData?.email
        Log.d("email", "$email")



        if (email != null) {
            Firebase.firestore.collection("users").document(email).get().addOnCompleteListener {task->
                if(task.isSuccessful){
                    val document = task.result
                    if(document!= null){
                        if (document.exists()) {
                            Log.d("TAG", "Document already exists.")
                        } else {
                            Log.d("TAG", "Document doesn't exist.")
                            //do stuff
                            if (hasUser()) {

                                Log.d("dentro has user", "${UserData?.email}")

                                val newUser = hashMapOf(
                                    "email" to (UserData?.email ?: ""),
                                    "name" to (UserData?.username ?: ""),
                                    "nickname" to "",
                                    "address" to "",
                                    "age" to 1,
                                    "phone" to "",
                                    "imageUri" to (UserData?.profilePictureUrl ?: ""),
                                    "sportPreferences" to "" // probabilmente da cambiare dovrebbe essere un array vuoto

                                )

                                UserData?.email?.let {
                                    db.collection("users").document(it)
                                        .set(newUser)
                                        .addOnSuccessListener {
                                            Log.d(
                                                TAG,
                                                "DocumentSnapshot successfully written!"
                                            )
                                        }
                                        .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) }
                                }


                            }

                        }

                    }
                }


                }
            }
        }


}

