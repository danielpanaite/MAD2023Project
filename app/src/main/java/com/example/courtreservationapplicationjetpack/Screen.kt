package com.example.courtreservationapplicationjetpack

import android.app.ActionBar
import android.media.MediaPlayer
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.navigation.NavHostController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.courtreservationapplicationjetpack.signIn.SignInDestination
import kotlinx.coroutines.delay


sealed class Screen(val route: String) {
    object Splash : Screen("splash_screen")
}

@Composable
fun SplashScreen(navController: NavHostController) {


    Box(
        modifier = Modifier
            .fillMaxSize()
            //.background(MaterialTheme.colorScheme.primary)
            .background(Color.White)
    ) {
        val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.logo_splash))
        val logoAnimationState =
            animateLottieCompositionAsState(composition = composition)
        val context = LocalContext.current
        val mediaPlayer = remember { MediaPlayer.create(context, R.raw.splash_sound) }
        DisposableEffect(Unit) {
            onDispose {
                mediaPlayer.release()
            }
        }

        LaunchedEffect(Unit) {
            delay(1000)
            mediaPlayer.start()
            delay(1000)
            mediaPlayer.stop()
        }

        LottieAnimation(
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.Center).background(Color.White).padding(0.dp),
            composition = composition,
            progress = { logoAnimationState.progress }
        )
        if (logoAnimationState.isAtEnd && logoAnimationState.isPlaying) {
            navController.navigate(SignInDestination.route)
        }

        //val alpha by animateFloatAsState(targetValue = 1f)

//        Text(
//            text = "Sportify",
//            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
//            color = Color.White.copy(alpha = alpha),
//            modifier = Modifier.align(Alignment.Center)
//                .padding(top = 90.dp)
//                .padding(horizontal = 16.dp),
//            textAlign = TextAlign.Center,
//        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .align(Alignment.BottomCenter)
                .offset(y = (-50).dp) // sposta di 16.dp in alto
                .background(Color.White)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SplashScreenPreview() {
    SplashScreen(navController = NavHostController(LocalContext.current))
}
