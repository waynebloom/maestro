package com.waynebloom.maestro.sheetmusic.ui

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

@HiltViewModel(assistedFactory = SheetMusicViewModel.Factory::class)
class SheetMusicViewModel @AssistedInject constructor(
    @Assisted private val owner: SheetMusicOwner,
): SheetMusicOwner by owner, ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(owner: SheetMusicOwner): SheetMusicViewModel
    }

    private val _uiState = MutableStateFlow(SheetMusicUiState())
    val uiState = _uiState
        .onStart {
            _uiState.update {
                it.copy(
                    notes = notes,
                    instrument = instrument,
                    bpm = bpm,
                )
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = SheetMusicUiState()
        )

    fun onZoom(value: Int) {
        _uiState.update {
            it.copy(zoomLevel = value)
        }
    }
}

data class SheetMusicUiState(
    val notes: SnapshotStateList<Char> = mutableStateListOf(),
    val instrument: String = "",
    val bpm: Int = 100,
    val zoomLevel: Int = 100,
)