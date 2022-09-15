package com.juliengabryelewicz.noteapp.presentation.notes

import com.juliengabryelewicz.noteapp.domain.model.Note

sealed class NotesEvent {
    data class DeleteNote(val note: Note): NotesEvent()
    object RestoreNote: NotesEvent()
    object ToggleOrderSection: NotesEvent()
}
