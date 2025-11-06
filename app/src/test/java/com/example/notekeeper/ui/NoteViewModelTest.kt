package com.example.notekeeper.ui

import com.example.notekeeper.data.model.Note
import com.example.notekeeper.data.repository.NoteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class NoteViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var fakeRepository: FakeNoteRepository
    private lateinit var viewModel: NoteViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        fakeRepository = FakeNoteRepository()
        viewModel = NoteViewModel(fakeRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `notes flow emits initial empty list`() = runTest(testDispatcher) {
        advanceUntilIdle()
        val notes = viewModel.notes.first()
        assertEquals(emptyList<Note>(), notes)
    }

    @Test
    fun `addNote with valid data adds note to repository`() = runTest(testDispatcher) {
        viewModel.addNote("Test Title", "Test Content")
        advanceUntilIdle()

        val notes = fakeRepository.getAllNotes().first()
        assertEquals(1, notes.size)
        assertEquals("Test Title", notes[0].title)
        assertEquals("Test Content", notes[0].content)
    }

    @Test
    fun `addNote with blank title and content does not add note`() = runTest(testDispatcher) {
        viewModel.addNote("", "")
        advanceUntilIdle()

        val notes = fakeRepository.getAllNotes().first()
        assertEquals(0, notes.size)
    }

    @Test
    fun `addNote with blank title but valid content adds note`() = runTest(testDispatcher) {
        viewModel.addNote("", "Valid Content")
        advanceUntilIdle()

        val notes = fakeRepository.getAllNotes().first()
        assertEquals(1, notes.size)
        assertEquals("", notes[0].title)
        assertEquals("Valid Content", notes[0].content)
    }

    @Test
    fun `addNote with valid title but blank content adds note`() = runTest(testDispatcher) {
        viewModel.addNote("Valid Title", "")
        advanceUntilIdle()

        val notes = fakeRepository.getAllNotes().first()
        assertEquals(1, notes.size)
        assertEquals("Valid Title", notes[0].title)
        assertEquals("", notes[0].content)
    }

    @Test
    fun `deleteNote removes note from repository`() = runTest(testDispatcher) {
        val note = Note(id = "test-id", title = "Test", content = "Content")
        fakeRepository.insertNote(note)
        advanceUntilIdle()

        viewModel.deleteNote("test-id")
        advanceUntilIdle()

        val notes = fakeRepository.getAllNotes().first()
        assertEquals(0, notes.size)
    }

    @Test
    fun `multiple operations maintain correct state`() = runTest(testDispatcher) {
        viewModel.addNote("Note 1", "Content 1")
        viewModel.addNote("Note 2", "Content 2")
        viewModel.addNote("Note 3", "Content 3")
        advanceUntilIdle()

        var notes = fakeRepository.getAllNotes().first()
        assertEquals(3, notes.size)

        viewModel.deleteNote(notes[1].id)
        advanceUntilIdle()

        notes = fakeRepository.getAllNotes().first()
        assertEquals(2, notes.size)
    }

    @Test
    fun `viewModel notes flow reflects repository changes`() = runTest(testDispatcher) {
        advanceUntilIdle()

        viewModel.addNote("Test Note", "Test Content")
        advanceUntilIdle()

        val notes = viewModel.notes.value
        assertEquals(1, notes.size)
        assertEquals("Test Note", notes[0].title)
    }

    // Fake implementation of NoteRepository for testing
    private class FakeNoteRepository : NoteRepository {
        private val _notes = MutableStateFlow<List<Note>>(emptyList())

        override fun getAllNotes(): Flow<List<Note>> = _notes

        override suspend fun getNoteById(id: String): Note? {
            return _notes.value.find { it.id == id }
        }

        override suspend fun insertNote(note: Note) {
            _notes.value = _notes.value + note
        }

        override suspend fun deleteNote(id: String) {
            _notes.value = _notes.value.filter { it.id != id }
        }
    }
}
