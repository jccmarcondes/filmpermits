package com.jccmarcondes.filmpermits.ui.favorites

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
class FavoriteViewModel @Inject constructor(
    private val filmPermissionRepository: FilmPermitsRepository
) : ViewModel() {

    private val favoriteEventChannel = Channel<FavoriteEvent>()
    val favoriteEvent = favoriteEventChannel.receiveAsFlow()

    fun getAllFavorites() = filmPermissionRepository.getAllStoredPermissions()

    fun onArticleSwiped(filmPermission: FilmPermission) {
        viewModelScope.launch {
            filmPermissionRepository.deleteFilmPermission(filmPermission)
            favoriteEventChannel.send(FavoriteEvent.ShowUndoDeleteFavoriteMessage(filmPermission))
        }
    }

    fun onUndoDeleteClick(filmPermission: FilmPermission) {
        viewModelScope.launch {
            filmPermissionRepository.insertFilmPermission(filmPermission)
        }
    }

    sealed class FavoriteEvent{
        data class ShowUndoDeleteFavoriteMessage(val filmPermission: FilmPermission): FavoriteEvent()
    }
}