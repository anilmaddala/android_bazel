package com.example.notekeeper.data.repository

import com.example.notekeeper.data.model.Note
import kotlinx.coroutines.flow.Flow

interface NoteRepository {
    fun getAllNotes(): Flow<List<Note>>
    suspend fun getNoteById(id: String): Note?
    suspend fun insertNote(note: Note)
    suspend fun deleteNote(id: String)
}
