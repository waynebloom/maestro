package com.waynebloom.maestro.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.waynebloom.maestro.guitar.GuitarRoute
import com.waynebloom.maestro.guitar.GuitarViewModel
import com.waynebloom.maestro.guitar.guitarSection
import com.waynebloom.maestro.guitar.navigateToGuitarSection
import com.waynebloom.maestro.piano.PianoRoute
import com.waynebloom.maestro.piano.PianoSectionRoute
import com.waynebloom.maestro.piano.PianoViewModel
import com.waynebloom.maestro.piano.navigateToPianoSection
import com.waynebloom.maestro.piano.pianoSection
import com.waynebloom.maestro.sheetmusic.navigation.SheetMusicRoute
import com.waynebloom.maestro.sheetmusic.navigation.navigateToSheetMusic
import com.waynebloom.maestro.sheetmusic.ui.SheetMusicScreen
import com.waynebloom.maestro.sheetmusic.ui.SheetMusicViewModel
import kotlinx.serialization.Serializable

@Composable
fun NavHost(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    androidx.navigation.compose.NavHost(
        navController = navController,
        startDestination = Home
    ) {

        composable<Home> {
            Row {
                Button(navController::navigateToPianoSection) {
                    Text("Play Piano")
                }
                Button(navController::navigateToGuitarSection) {
                    Text("Play Guitar")
                }
            }
        }

        pianoSection {

            composable<PianoRoute> {
                val sectionViewModel: PianoViewModel = it.sharedViewModel(navController)

                Scaffold { innerPadding ->

                    Column(Modifier.padding(innerPadding)) {
                        Text("Piano route")
                        Button(navController::navigateToSheetMusic) {
                            Text("Sheet Music")
                        }
                    }
                }
            }

            composable<SheetMusicRoute> {
                val sectionViewModel: PianoViewModel = it.sharedViewModel(navController)

                SheetMusicScreen(
                    viewModel = hiltViewModel<SheetMusicViewModel, SheetMusicViewModel.Factory> { factory ->
                        factory.create(sectionViewModel)
                    }
                )
            }
        }

        guitarSection {
            composable<GuitarRoute> {
                val sectionViewModel: PianoViewModel = it.sharedViewModel(navController)

                Scaffold { innerPadding ->

                    Column(Modifier.padding(innerPadding)) {
                        Text("Piano route")
                        Button(navController::navigateToSheetMusic) {
                            Text("Sheet Music")
                        }
                    }
                }
            }

            composable<SheetMusicRoute> {
                val sectionViewModel: GuitarViewModel = it.sharedViewModel(navController)

                SheetMusicScreen(
                    viewModel = hiltViewModel<SheetMusicViewModel, SheetMusicViewModel.Factory> { factory ->
                        factory.create(sectionViewModel)
                    }
                )
            }
        }
    }
}

@Serializable
data object Home

@Composable
inline fun <reified T : ViewModel> NavBackStackEntry.sharedViewModel(
    navController: NavController
): T {
    val navGraphRoute = destination.parent?.route ?: return hiltViewModel()
    val parentEntry = remember(this) {
        navController.getBackStackEntry(navGraphRoute)
    }
    return hiltViewModel(parentEntry)
}
