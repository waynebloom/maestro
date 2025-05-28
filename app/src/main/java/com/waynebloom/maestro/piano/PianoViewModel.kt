package com.waynebloom.maestro.piano

import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import com.waynebloom.maestro.data.repository.MusicRepository
import com.waynebloom.maestro.sheetmusic.ui.SheetMusicOwner
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PianoViewModel @Inject constructor(
    private val musicRepository: MusicRepository
): SheetMusicOwner, ViewModel() {

    override val notes: SnapshotStateList<Char>
        get() = musicRepository.getNotes().toMutableStateList()

    override val instrument: String
        get() = "Piano"

    override val bpm: Int
        get() = 100

    fun getKeyboardSize() = 88

    fun isSustaining() = false
}