package com.juliengabryelewicz.noteapp.presentation.helper

sealed class Screen(val route: String) {
    object NotesScreen: Screen("notes_screen")
    object DetailNoteScreen: Screen("detail_note_screen")
}
