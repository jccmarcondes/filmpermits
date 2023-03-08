package com.jccmarcondes.filmpermits.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.jccmarcondes.filmpermits.data.model.FilmPermission
import com.jccmarcondes.filmpermits.di.ApplicationScope
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject
import javax.inject.Provider

@Database(entities = [FilmPermission::class], version = 1)
abstract class FilmPermissionDatabase : RoomDatabase() {

    abstract fun getFilmPermissionDao(): FilmPermissionDao

    class Callback @Inject constructor(
        private val database: Provider<FilmPermissionDatabase>,
        @ApplicationScope private val applicationScope: CoroutineScope
    ) : RoomDatabase.Callback()
}