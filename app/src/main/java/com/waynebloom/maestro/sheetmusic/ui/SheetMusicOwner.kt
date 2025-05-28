package com.waynebloom.maestro.sheetmusic.ui

import androidx.compose.runtime.snapshots.SnapshotStateList

interface SheetMusicOwner {
    val notes: SnapshotStateList<Char>
    val bpm: Int
    val instrument: String

    fun appendNotes(notes: List<Char>) {
        this.notes.addAll(notes)
    }
}