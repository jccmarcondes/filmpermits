package com.jccmarcondes.filmpermits.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.assertThat
import com.jccmarcondes.filmpermits.data.model.FilmPermission
import com.jccmarcondes.filmpermits.util.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class FilmPermissionDaoTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var filmPermissionDatabase: FilmPermissionDatabase
    private lateinit var filmPermissionDao: FilmPermissionDao

    //Default value
    private val filmPermissionToAdd = FilmPermission.mock()

    @Before
    fun setup() {
        filmPermissionDatabase = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            FilmPermissionDatabase::class.java
        ).allowMainThreadQueries().build()

        filmPermissionDao = filmPermissionDatabase.getFilmPermissionDao()
    }

    @After
    @Throws(IOException::class)
    fun teardown(){
        filmPermissionDatabase.close()
    }

    @Test
    @Throws(Exception::class)
    fun getAllFilmPermissions() = runTest {
        filmPermissionDao.insert(filmPermissionToAdd)

        val filmPermissionList = filmPermissionDao.getFilmPermissions().getOrAwaitValue()
        assertThat(filmPermissionList).isNotEmpty()
    }

    @Test
    @Throws(Exception::class)
    fun insertFilmPermission() = runTest {
        filmPermissionDao.insert(filmPermissionToAdd)

        val filmPermissionList = filmPermissionDao.getFilmPermissions().getOrAwaitValue()
        assertThat(filmPermissionList).contains(filmPermissionToAdd.copy(id = 1))
    }

    @Test
    @Throws(Exception::class)
    fun deleteFilmPermission() = runTest {
        filmPermissionDao.insert(filmPermissionToAdd)
        filmPermissionDao.delete(filmPermissionToAdd)

        val filmPermissionList = filmPermissionDao.getFilmPermissions().getOrAwaitValue()
        assertThat(filmPermissionList).doesNotContain(filmPermissionToAdd)
    }
}