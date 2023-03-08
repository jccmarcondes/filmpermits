package com.jccmarcondes.filmpermits.ui.itemfilmpermits

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jccmarcondes.filmpermits.data.model.FilmPermission
import com.jccmarcondes.filmpermits.repository.FilmPermitsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FilmPermissionViewModel @Inject constructor(
    private val filmPermissionRepository: FilmPermitsRepository
) : ViewModel() {

    private val filmPermissionEventChannel = Channel<FilmPermissionEvent>()
    val filmPermissionEvent = filmPermissionEventChannel.receiveAsFlow()

    fun saveFavorite(filmPermission: FilmPermission) {
        viewModelScope.launch {
            filmPermissionRepository.insertFilmPermission(filmPermission)
            filmPermissionEventChannel.send(FilmPermissionEvent.ShowFilmPermissionSavedMessage("Event saved as favorite!"))
        }
    }

    sealed class FilmPermissionEvent{
        data class ShowFilmPermissionSavedMessage(val message: String): FilmPermissionEvent()
    }
}