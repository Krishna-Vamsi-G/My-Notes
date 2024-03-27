package com.example.mynotes.data.repository

import androidx.lifecycle.LiveData
import com.example.mynotes.data.database.NoteDao
import com.example.mynotes.data.model.Note

class NoteRepository(private val noteDao: NoteDao) {
    val allNotes: LiveData<List<Note>> = noteDao.getAllNotes()

    suspend fun insert(note: Note) {
        noteDao.insert(note)
    }

    suspend fun update(note: Note) {
        noteDao.update(note)
    }

    suspend fun delete(note: Note) {
        noteDao.delete(note)
    }
}
