package com.example.courtreservationapplicationjetpack.signIn

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.courtreservationapplicationjetpack.R
import com.example.courtreservationapplicationjetpack.navigation.NavigationDestination
import com.example.courtreservationapplicationjetpack.views.MainScreenDestination
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
        navController = navController,
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
    navController: NavController,
    //call when we click in the sign in button
    onSignInClick: () -> Unit,

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

    val window = (context as? Activity)?.window
    window?.setFlags(
        WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
        WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
    )

//    Box(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(16.dp),
//        contentAlignment = Alignment.Center
//    ){
//        Button(onClick = onSignInClick){
//            Text(text= "Sign in")
//        }
//    }


    val composition = rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.splash_animation))
    val progress by animateLottieCompositionAsState(
        composition = composition.value,
        iterations = LottieConstants.IterateForever
    )
    Box(
        modifier = Modifier.fillMaxSize().background(Color.Black)
    ){
        LottieAnimation(
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds,
            composition = composition.value,

            progress = { progress },

            maintainOriginalImageBounds = true,

        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(horizontal = 40.dp)
                .padding(bottom = 100.dp)
        ) {
            androidx.compose.material3.Text(
                modifier = Modifier
                    .fillMaxWidth(),
                textAlign = TextAlign.Center,
                text = "Sportify",
                color = Color.White,
                fontSize = MaterialTheme.typography.displayLarge.fontSize,
                fontWeight = FontWeight.Bold
            )
            androidx.compose.material3.Text(
                modifier = Modifier
                    .fillMaxWidth(),
                textAlign = TextAlign.Center,
                text = "La prima app che ti permette di prenotare ogni maledettissimo giorno il campo stella\nEffettua il Login!",
                color = Color.White,
                fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                fontWeight = FontWeight.Normal
            )
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                modifier = Modifier.fillMaxWidth().height(54.dp),
                onClick = { navController.navigate(MainScreenDestination.route) },
                shape = CircleShape,
                colors = androidx.compose.material.ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colorScheme.primary.copy(alpha = 1f))
            ) {
                Text(
                    text = "Log-in!",
                    color = Color.Black
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
            androidx.compose.material3.Button(
                modifier = Modifier.fillMaxWidth().height(54.dp),
                onClick = { onSignInClick() },
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(containerColor = Color.White)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = painterResource(R.drawable.ic_google), // Sostituisci con l'immagine di Google
                        contentDescription = "Google",
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Login with Google",
                        color = Color.Black
                    )
                }
            }
        }
    }
}
