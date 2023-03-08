package com.jccmarcondes.filmpermits.di

import android.app.Application
import androidx.room.Room
import com.jccmarcondes.filmpermits.data.local.FilmPermissionDao
import com.jccmarcondes.filmpermits.data.local.FilmPermissionDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(application: Application, callback: FilmPermissionDatabase.Callback): FilmPermissionDatabase{
        return Room.databaseBuilder(application, FilmPermissionDatabase::class.java, "film_permits_database")
            .fallbackToDestructiveMigration()
            .addCallback(callback)
            .build()
    }

    @Provides
    fun provideFilmePermitsDao(db: FilmPermissionDatabase): FilmPermissionDao{
        return db.getFilmPermissionDao()
    }
}