package com.example.courtreservationapplicationjetpack.signIn

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Place
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.courtreservationapplicationjetpack.navigation.NavigationDestination
import com.google.android.gms.auth.api.identity.Identity
import kotlinx.coroutines.launch

object SignInDestination : NavigationDestination {
    override val route  = "sign_in"
    override val titleRes = "SignIn"
    override val icon = Icons.Default.Place

}


@Composable
fun SignIn(
    modifier: Modifier = Modifier,
    navController: NavController,
    context: Context,
    googleAuthUiClient: GoogleAuthUiClient

) {

    val viewModel = viewModel<SignInViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = Unit) {
        if (googleAuthUiClient.getSignedInUser() != null) {
            navController.navigate("home")
        }
    }

    val lifecycle = LocalLifecycleOwner.current



    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult(),
        onResult = { result ->
            if (result.resultCode == ComponentActivity.RESULT_OK) {
                lifecycle.lifecycleScope.launch {
                    val signInResult = googleAuthUiClient.signInWithIntent(
                        intent = result.data ?: return@launch
                    )
                    viewModel.onSignInResult(signInResult)
                }
            }
        }
    )

    LaunchedEffect(key1 = state.isSignInSuccessful) {
        if (state.isSignInSuccessful) {
            Toast.makeText(
                context,
                "Sign in successful",
                Toast.LENGTH_LONG
            ).show()

            //qua mettere che se non esiste un user con quella mail se ne crea uno con questi dati
            viewModel.addUser(googleAuthUiClient)
            viewModel.resetState()

            navController.navigate("home")

        }
    }

    SignInScreen(
        state = state,
        onSignInClick = {
            lifecycle.lifecycleScope.launch {
                Log.d("signInScreen", "$state")
                val signInIntentSender = googleAuthUiClient.signIn()
                Log.d("signInScreenintent", "$signInIntentSender")

                launcher.launch(
                    IntentSenderRequest.Builder(
                        signInIntentSender ?: return@launch
                    ).build()
                )
            }
        }
    )
}
@Composable
fun SignInScreen(
    //so we can update the UI
    state: SignInState,
    //call when we click in the sign in button
    onSignInClick : () -> Unit
){
    val context = LocalContext.current
    LaunchedEffect(key1 = state.signInError){
        state.signInError?.let { error->
            Toast.makeText(
                context,
                error,
                Toast.LENGTH_LONG
            ).show()
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ){
        Button(onClick = onSignInClick){
            Text(text= "Sign in")
        }
    }


}