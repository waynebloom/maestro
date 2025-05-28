package com.waynebloom.maestro.piano

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation
import kotlinx.serialization.Serializable

@Serializable
data object PianoSectionRoute

@Serializable
data object PianoRoute

fun NavController.navigateToPianoSection() {
    navigate(PianoSectionRoute)
}

fun NavGraphBuilder.pianoSection(builder: NavGraphBuilder.() -> Unit) {
    navigation<PianoSectionRoute>(startDestination = PianoRoute, builder = builder)
}
