package com.example.notekeeper.data.repository

import com.example.notekeeper.data.model.Note
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NoteRepositoryImpl @Inject constructor() : NoteRepository {

    private val _notes = MutableStateFlow<List<Note>>(emptyList())

    override fun getAllNotes(): Flow<List<Note>> = _notes.asStateFlow()

    override suspend fun getNoteById(id: String): Note? {
        return _notes.value.find { it.id == id }
    }

    override suspend fun insertNote(note: Note) {
        _notes.update { currentNotes ->
            (currentNotes + note).sortedByDescending { it.timestamp }
        }
    }

    override suspend fun deleteNote(id: String) {
        _notes.update { currentNotes ->
            currentNotes.filter { it.id != id }
        }
    }
}
