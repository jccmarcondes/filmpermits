package com.jccmarcondes.filmpermits.data.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.jccmarcondes.filmpermits.data.model.FilmPermission

@Dao
interface FilmPermissionDao {

    @Query("SELECT * FROM permission_film_table")
    fun getFilmPermissions() : LiveData<List<FilmPermission>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(filmePermissionInfo: FilmPermission) : Long

    @Delete
    suspend fun delete(filmPermissionInfo: FilmPermission)

    @Query("DELETE FROM permission_film_table")
    suspend fun deleteAllFilmPermissions()
}