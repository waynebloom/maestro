package com.waynebloom.maestro.data.repository

import com.waynebloom.maestro.data.datasource.MusicDataSource
import javax.inject.Inject

class MusicRepository @Inject constructor(
    private val dataSource: MusicDataSource
) {

    fun getNotes() = dataSource.exampleNotes
}