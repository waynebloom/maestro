package com.waynebloom.maestro.guitar

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation
import kotlinx.serialization.Serializable

@Serializable
data object GuitarSectionRoute

@Serializable
data object GuitarRoute

fun NavController.navigateToGuitarSection() {
    navigate(GuitarSectionRoute)
}

fun NavGraphBuilder.guitarSection(builder: NavGraphBuilder.() -> Unit) {
    navigation<GuitarSectionRoute>(startDestination = GuitarRoute, builder = builder)
}
