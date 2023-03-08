package com.jccmarcondes.filmpermits.data.remote

import com.jccmarcondes.filmpermits.data.model.FilmPermission
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface FilmPermitsApi {

    @GET("resource/tg4x-b46p.json/")
    suspend fun getFilmPermits(): Response<List<FilmPermission>>

    @GET("resource/tg4x-b46p.json")
    suspend fun searchForCategory(
        @Query("category") category: String
    ): Response<List<FilmPermission>>

}