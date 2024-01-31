package com.example.roomhandson.app

import android.app.Application
import com.example.roomhandson.data.local.databse.NotesDatabase
import com.example.roomhandson.data.repository.NotesRepository

class NotesApp : Application() {

    private val database by lazy { NotesDatabase.getDatabase(this) }
    val repository by lazy { NotesRepository(database.notesDao()) }
}