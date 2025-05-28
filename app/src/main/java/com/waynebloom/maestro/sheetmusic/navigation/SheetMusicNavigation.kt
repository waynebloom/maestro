package com.waynebloom.maestro.sheetmusic.navigation

import androidx.navigation.NavController
import kotlinx.serialization.Serializable

@Serializable
data object SheetMusicRoute

fun NavController.navigateToSheetMusic() {
    navigate(SheetMusicRoute)
}
