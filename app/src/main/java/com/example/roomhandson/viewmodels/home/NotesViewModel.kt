package com.example.roomhandson.viewmodels.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.roomhandson.data.local.enitities.Note
import com.example.roomhandson.data.repository.NotesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class NotesViewModel(private val repository: NotesRepository) : ViewModel() {

    val allNote: LiveData<List<Note>> = repository.allNote

    private var deletedNote: Note? = null
    suspend fun getNoteById(id: Int): Note {
        return viewModelScope.async {
            return@async repository.getNoteById(id)
        }.await()
    }

    fun createNote(note: Note) = viewModelScope.launch(Dispatchers.IO) {
        repository.createNote(note)
    }

    fun updateNote(note: Note) = viewModelScope.launch(Dispatchers.IO) {
        repository.updateNote(note)
    }

    fun deleteNote(note: Note) = viewModelScope.launch(Dispatchers.IO) {
        deletedNote = note
        repository.deleteNote(note)
    }

    fun restoreNote() = viewModelScope.launch(Dispatchers.IO) {
        repository.createNote(deletedNote!!)
        deletedNote = null
    }
}

class NotesViewModelFactory(private val repository: NotesRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NotesViewModel::class.java)) {
            return NotesViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown viewmodel found")
    }
}