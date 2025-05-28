package com.waynebloom.maestro.guitar

import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import com.waynebloom.maestro.data.repository.MusicRepository
import com.waynebloom.maestro.sheetmusic.ui.SheetMusicOwner
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class GuitarViewModel @Inject constructor(
    private val musicRepository: MusicRepository
): SheetMusicOwner, ViewModel() {

    override val notes: SnapshotStateList<Char>
        get() = musicRepository.getNotes().toMutableStateList()

    override val instrument: String
        get() = "Guitar"

    override val bpm: Int
        get() = 144

    fun getStrings() = listOf(
        'e',
        'b',
        'g',
        'd',
        'a',
        'e'
    )

    fun getCapoPosition() = 2
}
