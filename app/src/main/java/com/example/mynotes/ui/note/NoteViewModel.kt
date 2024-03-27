package com.example.mynotes.ui.note

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.mynotes.data.model.Note
import com.example.mynotes.data.database.NoteDatabase
import com.example.mynotes.data.repository.NoteRepository
import kotlinx.coroutines.launch

class NoteViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: NoteRepository
    val allNotes: LiveData<List<Note>>

    init {
        val notesDao = NoteDatabase.getDatabase(application).noteDao()
        repository = NoteRepository(notesDao)
        allNotes = repository.allNotes
    }

    fun insert(note: Note) = viewModelScope.launch {
        repository.insert(note)
    }

    fun update(note: Note) = viewModelScope.launch {
        repository.update(note)
    }

    fun delete(note: Note) = viewModelScope.launch {
        repository.delete(note)
    }
}

