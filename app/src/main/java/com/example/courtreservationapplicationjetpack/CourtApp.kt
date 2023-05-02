package com.example.courtreservationapplicationjetpack


import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.courtreservationapplicationjetpack.navigation.NavigationGraph
import com.example.courtreservationapplicationjetpack.R.string


/**
 * Top level composable that represents screens for the application.
 */
@Composable
fun CourtApp(navController: NavHostController = rememberNavController()) {
    NavigationGraph(navController = navController)
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CourtTopAppBar(
    canNavigateBack: Boolean,
    modifier: Modifier = Modifier,
    navigateUp: () -> Unit = {}
) {
    if (canNavigateBack) {
        CenterAlignedTopAppBar(
            title = {
                Text(
                    "Time to play",
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            },
            modifier = modifier,
            navigationIcon = {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = stringResource(string.back_button)
                    )
                }
            }
        )
    } else {
        CenterAlignedTopAppBar(title = {
            Text(
                "Time to play",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }, modifier = modifier)
    }
}
