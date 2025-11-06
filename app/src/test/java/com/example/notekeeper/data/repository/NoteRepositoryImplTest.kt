package com.example.notekeeper.data.repository

import com.example.notekeeper.data.model.Note
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

class NoteRepositoryImplTest {

    private lateinit var repository: NoteRepositoryImpl

    @Before
    fun setup() {
        repository = NoteRepositoryImpl()
    }

    @Test
    fun `getAllNotes returns empty list initially`() = runTest {
        val notes = repository.getAllNotes().first()
        assertEquals(emptyList<Note>(), notes)
    }

    @Test
    fun `insertNote adds note to repository`() = runTest {
        val note = Note(
            id = "test-id",
            title = "Test Title",
            content = "Test Content",
            timestamp = 1234567890L
        )

        repository.insertNote(note)

        val notes = repository.getAllNotes().first()
        assertEquals(1, notes.size)
        assertEquals(note, notes[0])
    }

    @Test
    fun `insertNote sorts notes by timestamp descending`() = runTest {
        val note1 = Note(
            id = "1",
            title = "First",
            content = "Content 1",
            timestamp = 1000L
        )
        val note2 = Note(
            id = "2",
            title = "Second",
            content = "Content 2",
            timestamp = 2000L
        )
        val note3 = Note(
            id = "3",
            title = "Third",
            content = "Content 3",
            timestamp = 1500L
        )

        repository.insertNote(note1)
        repository.insertNote(note2)
        repository.insertNote(note3)

        val notes = repository.getAllNotes().first()
        assertEquals(3, notes.size)
        assertEquals("2", notes[0].id) // Most recent
        assertEquals("3", notes[1].id) // Middle
        assertEquals("1", notes[2].id) // Oldest
    }

    @Test
    fun `getNoteById returns correct note`() = runTest {
        val note1 = Note(id = "1", title = "Note 1", content = "Content 1")
        val note2 = Note(id = "2", title = "Note 2", content = "Content 2")

        repository.insertNote(note1)
        repository.insertNote(note2)

        val retrieved = repository.getNoteById("1")
        assertEquals(note1, retrieved)
    }

    @Test
    fun `getNoteById returns null for non-existent id`() = runTest {
        val note = Note(id = "1", title = "Note", content = "Content")
        repository.insertNote(note)

        val retrieved = repository.getNoteById("non-existent")
        assertNull(retrieved)
    }

    @Test
    fun `deleteNote removes note from repository`() = runTest {
        val note1 = Note(id = "1", title = "Note 1", content = "Content 1")
        val note2 = Note(id = "2", title = "Note 2", content = "Content 2")

        repository.insertNote(note1)
        repository.insertNote(note2)

        repository.deleteNote("1")

        val notes = repository.getAllNotes().first()
        assertEquals(1, notes.size)
        assertEquals("2", notes[0].id)
    }

    @Test
    fun `deleteNote with non-existent id does nothing`() = runTest {
        val note = Note(id = "1", title = "Note", content = "Content")
        repository.insertNote(note)

        repository.deleteNote("non-existent")

        val notes = repository.getAllNotes().first()
        assertEquals(1, notes.size)
    }

    @Test
    fun `multiple operations maintain correct state`() = runTest {
        val note1 = Note(id = "1", title = "Note 1", content = "Content 1", timestamp = 1000L)
        val note2 = Note(id = "2", title = "Note 2", content = "Content 2", timestamp = 2000L)
        val note3 = Note(id = "3", title = "Note 3", content = "Content 3", timestamp = 3000L)

        repository.insertNote(note1)
        repository.insertNote(note2)
        repository.insertNote(note3)

        repository.deleteNote("2")

        val notes = repository.getAllNotes().first()
        assertEquals(2, notes.size)
        assertEquals("3", notes[0].id)
        assertEquals("1", notes[1].id)

        val retrieved = repository.getNoteById("2")
        assertNull(retrieved)
    }
}
