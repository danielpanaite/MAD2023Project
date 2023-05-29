package com.example.courtreservationapplicationjetpack.signIn
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.BeginSignInRequest.GoogleIdTokenRequestOptions
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.example.courtreservationapplicationjetpack.R
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.tasks.await

class GoogleAuthUiClient(
    private val context: Context,

    //oneTapClient is the client that comes from the firebase sdk
    //which will then in the end show the dialog to  sign in
    private val oneTapClient: SignInClient

){
    //private reference to our auth object, that we get from firebase.auth
    private val auth = Firebase.auth

    //signIn takes time, so in that time we want to suspend the coroutine
    // so we can wait for the result, that is why suspend
    //it will return an IntentSender: an object we can use to send an intent to fullfill something
    suspend fun signIn(): IntentSender?{

        val result = try{
            oneTapClient.beginSignIn( //oneTapCleint begin signIn that takes a sign in request built in the function below.
                //by default all this firebase functions (like .beginSignIn) will return a Task, a task is something that executes something
                //asyncronously and then we can get something using for example addOnFailureListenere... or await
                buildSignInRequest()
            ).await() //will suspend this outer coroutine and wait until the sign in is finished
        }catch(e: Exception){
            e.printStackTrace() //trow the exception
            if(e is CancellationException) throw e
            null //return null to our result because if there is an issue we don't have an IntentSender
        }
        return result?.pendingIntent?.intentSender //when we take this intentSender and actually send it what will happen is:
        //our app will get an intent back with the infomration about the user sing in
        //the firebase sdk already contains a function to deserialize that intent that we will receive from this intentSender

    }

    suspend fun signInWithIntent(intent: Intent): SignInResult {
        //we get the credentials from the resulting intent
        val credential = oneTapClient.getSignInCredentialFromIntent(intent)
        //used for authentication with this credentials
        val googleIdToken = credential.googleIdToken
        //
        val googleCredentials = GoogleAuthProvider.getCredential(googleIdToken, null)
        return  try{
            val user = auth.signInWithCredential(googleCredentials).await().user
            SignInResult(
                data = user?.run{
                    UserData(
                        userId = uid,
                        username = displayName,
                        profilePictureUrl = photoUrl?.toString()
                    )
                },
                errorMessage = null
            )

        }catch(e: Exception){
            e.printStackTrace()
            if(e is CancellationException) throw e
            SignInResult(
                data = null,
                errorMessage = e.message
            )
        }
    }

    //function to sign out
    suspend fun signOut(){
        try{
            oneTapClient.signOut().await()
            auth.signOut()
        }catch(e: Exception){
            e.printStackTrace()
            if(e is CancellationException) throw e
        }
    }

    fun getSignedInUser(): UserData? = auth.currentUser?.run {
        UserData(
            userId = uid,
            username = displayName,
            profilePictureUrl = photoUrl?.toString()
        )
    }

    //signIn request
    private fun buildSignInRequest(): BeginSignInRequest{
        return BeginSignInRequest.Builder() //we return the request
            .setGoogleIdTokenRequestOptions( //also build by a builder
                GoogleIdTokenRequestOptions.builder()
                    .setSupported(true) // this way of authenticated is supported (true)
                    .setFilterByAuthorizedAccounts(false) //if set to true it will alrady check if you are signed in with a specific account and only show that
                    .setServerClientId(context.getString(R.string.web_client_id)) //sdk id taken from firebase
                    .build()
            )
            .setAutoSelectEnabled(true) //if you only have s ingle accunt of google it will automatically select that one
            .build()
    }
}