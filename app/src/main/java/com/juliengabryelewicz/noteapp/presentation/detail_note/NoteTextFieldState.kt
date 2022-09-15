package com.juliengabryelewicz.noteapp.presentation.detail_note

data class NoteTextFieldState(
    val text: String = "",
    val hint: String = "",
    val isHintVisible: Boolean = true
)
