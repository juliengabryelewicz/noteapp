package com.juliengabryelewicz.noteapp.domain.usecases

import com.juliengabryelewicz.noteapp.domain.model.InvalidNoteException
import com.juliengabryelewicz.noteapp.domain.model.Note
import com.juliengabryelewicz.noteapp.domain.repository.NoteRepository

class AddNote(
    private val repository: NoteRepository
) {

    @Throws(InvalidNoteException::class)
    suspend operator fun invoke(note: Note) {
        if(note.title.isBlank()) {
            throw InvalidNoteException("Le titre est obligatoire.")
        }
        if(note.description.isBlank()) {
            throw InvalidNoteException("La description est obligatoire.")
        }
        repository.insertNote(note)
    }
}