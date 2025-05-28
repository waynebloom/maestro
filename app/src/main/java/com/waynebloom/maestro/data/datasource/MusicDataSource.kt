package com.waynebloom.maestro.data.datasource

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MusicDataSource @Inject constructor() {

    val exampleNotes: List<Char> = listOf(
        'a',
        'c',
        'f',
        'b'
    )
}