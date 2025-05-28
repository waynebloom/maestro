package com.waynebloom.maestro.sheetmusic.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier

@Composable
fun SheetMusicScreen(
    viewModel: SheetMusicViewModel,
) {

    val uiState by viewModel.uiState.collectAsState()

    Scaffold {
        Column(Modifier.padding(it)) {
            Text(
                text = "Sheet music route",
                style = MaterialTheme.typography.headlineMedium
            )

            Text("Instrument: ${uiState.instrument}")
            Text("BPM: ${uiState.bpm}")
            Text("Notes: ${uiState.notes}")
        }
    }
}