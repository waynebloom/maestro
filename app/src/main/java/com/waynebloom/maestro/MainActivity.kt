package com.waynebloom.maestro

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.waynebloom.maestro.navigation.NavHost
import com.waynebloom.maestro.ui.theme.SharedInjectedViewModelExampleTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SharedInjectedViewModelExampleTheme {
                NavHost()
            }
        }
    }
}
