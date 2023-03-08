package com.jccmarcondes.filmpermits.repository

import com.jccmarcondes.filmpermits.data.local.FilmPermissionDao
import com.jccmarcondes.filmpermits.data.model.FilmPermission
import com.jccmarcondes.filmpermits.data.remote.FilmPermitsApi
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FilmPermitsRepository @Inject constructor(
    private val filmPermitsApi: FilmPermitsApi,
    private val filmPermissionDao: FilmPermissionDao
) {

    suspend fun getFilmPermits(): Response<List<FilmPermission>> {
        return filmPermitsApi.getFilmPermits()
    }

    suspend fun searchCategory(searchCategory: String): Response<List<FilmPermission>>{
        return filmPermitsApi.searchForCategory(searchCategory)
    }

    fun getAllStoredPermissions() = filmPermissionDao.getFilmPermissions()

    suspend fun insertFilmPermission(filmPerssion: FilmPermission) = filmPermissionDao.insert(filmPerssion)

    suspend fun deleteFilmPermission(filmPerssion: FilmPermission) = filmPermissionDao.delete(filmPerssion)
}