package com.juliengabryelewicz.noteapp.presentation.notes

import com.juliengabryelewicz.noteapp.domain.model.Note

data class NotesState(
    val notes: List<Note> = emptyList(),
    val isOrderSectionVisible: Boolean = false
)
