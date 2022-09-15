package com.juliengabryelewicz.noteapp.domain.usecases

import com.juliengabryelewicz.noteapp.domain.model.Note
import com.juliengabryelewicz.noteapp.domain.repository.NoteRepository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetNotes(
    private val repository: NoteRepository
) {

    operator fun invoke(): Flow<List<Note>> {
        return repository.getNotes().map { notes -> notes.sortedByDescending { it.timestamp } }
    }
}