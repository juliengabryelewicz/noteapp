package com.juliengabryelewicz.noteapp.domain.usecases

import com.juliengabryelewicz.noteapp.domain.model.Note
import com.juliengabryelewicz.noteapp.domain.repository.NoteRepository

class GetNote(
    private val repository: NoteRepository
) {

    suspend operator fun invoke(id: Int): Note? {
        return repository.getNoteById(id)
    }
}