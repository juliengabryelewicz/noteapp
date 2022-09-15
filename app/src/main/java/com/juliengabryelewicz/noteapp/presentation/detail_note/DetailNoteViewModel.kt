package com.juliengabryelewicz.noteapp.presentation.detail_note

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.juliengabryelewicz.noteapp.domain.model.InvalidNoteException
import com.juliengabryelewicz.noteapp.domain.model.Note
import com.juliengabryelewicz.noteapp.domain.usecases.NoteUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailNoteViewModel @Inject constructor(
    private val noteUseCases: NoteUseCases,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _noteTitle = mutableStateOf(NoteTextFieldState(
        hint = "Ajouter un titre"
    ))
    val noteTitle: State<NoteTextFieldState> = _noteTitle

    private val _noteDescription = mutableStateOf(NoteTextFieldState(
        hint = "Ajouter une description"
    ))
    val noteDescription: State<NoteTextFieldState> = _noteDescription

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private var currentNoteId: Int? = null

    init {
        savedStateHandle.get<Int>("noteId")?.let { noteId ->
            if(noteId != -1) {
                viewModelScope.launch {
                    noteUseCases.getNote(noteId)?.also { note ->
                        currentNoteId = note.id
                        _noteTitle.value = noteTitle.value.copy(
                            text = note.title,
                            isHintVisible = false
                        )
                        _noteDescription.value = _noteDescription.value.copy(
                            text = note.description,
                            isHintVisible = false
                        )
                    }
                }
            }
        }
    }

    fun onEvent(event: DetailNoteEvent) {
        when(event) {
            is DetailNoteEvent.EnteredTitle -> {
                _noteTitle.value = noteTitle.value.copy(
                    text = event.value
                )
            }
            is DetailNoteEvent.ChangeTitleFocus -> {
                _noteTitle.value = noteTitle.value.copy(
                    isHintVisible = !event.focusState.isFocused &&
                            noteTitle.value.text.isBlank()
                )
            }
            is DetailNoteEvent.EnteredContent -> {
                _noteDescription.value = _noteDescription.value.copy(
                    text = event.value
                )
            }
            is DetailNoteEvent.ChangeContentFocus -> {
                _noteDescription.value = _noteDescription.value.copy(
                    isHintVisible = !event.focusState.isFocused &&
                            _noteDescription.value.text.isBlank()
                )
            }

            is DetailNoteEvent.SaveNote -> {
                viewModelScope.launch {
                    try {
                        noteUseCases.addNote(
                            Note(
                                title = noteTitle.value.text,
                                description = noteDescription.value.text,
                                timestamp = System.currentTimeMillis(),
                                id = currentNoteId
                            )
                        )
                        _eventFlow.emit(UiEvent.SaveNote)
                    } catch(e: InvalidNoteException) {
                        _eventFlow.emit(
                            UiEvent.ShowSnackbar(
                                message = e.message ?: "Note non enregistrée"
                            )
                        )
                    }
                }
            }
        }
    }

    sealed class UiEvent {
        data class ShowSnackbar(val message: String): UiEvent()
        object SaveNote: UiEvent()
    }
}