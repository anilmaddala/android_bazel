package com.example.notekeeper.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.notekeeper.ui.NoteViewModel
import com.example.notekeeper.ui.screens.AddNoteScreen
import com.example.notekeeper.ui.screens.NoteListScreen

sealed class Screen(val route: String) {
    object NoteList : Screen("note_list")
    object AddNote : Screen("add_note")
}

@Composable
fun NavGraph(
    navController: NavHostController,
    viewModel: NoteViewModel
) {
    NavHost(
        navController = navController,
        startDestination = Screen.NoteList.route
    ) {
        composable(Screen.NoteList.route) {
            NoteListScreen(
                viewModel = viewModel,
                onAddNoteClick = {
                    navController.navigate(Screen.AddNote.route)
                }
            )
        }

        composable(Screen.AddNote.route) {
            AddNoteScreen(
                viewModel = viewModel,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}
