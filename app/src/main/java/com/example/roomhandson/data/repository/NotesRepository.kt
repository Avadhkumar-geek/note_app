package com.example.roomhandson.data.repository

import androidx.lifecycle.LiveData
import com.example.roomhandson.data.local.dao.NotesDao
import com.example.roomhandson.data.local.enitities.Note

class NotesRepository(private val notesDao: NotesDao) {

    val allNote: LiveData<List<Note>> = notesDao.getAllNotes()

    suspend fun getNoteById(id:Int):Note{
        return notesDao.getNoteById(id)
    }

    suspend fun createNote(note: Note) {
        notesDao.createNote(note)
    }

    suspend fun updateNote(note: Note) {
        notesDao.updateNote(note)
    }

    suspend fun deleteNote(note: Note) {
        notesDao.deleteNote(note)
    }
}