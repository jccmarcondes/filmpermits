package com.jccmarcondes.filmpermits.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.jccmarcondes.filmpermits.R
import com.jccmarcondes.filmpermits.databinding.ActivityFilmPermitsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FilmPermitsActivity : FragmentActivity() {

    lateinit var binding: ActivityFilmPermitsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFilmPermitsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.filmPermitsNavHostFragment) as NavHostFragment
        val navController = navHostFragment.navController

        binding.apply {
            bottomNavigationView.setupWithNavController(navController)
        }
    }

    fun showBottomNavigation(shouldShow: Boolean) {
        if (shouldShow) binding.bottomNavigationView.visibility = View.VISIBLE
        else binding.bottomNavigationView.visibility = View.GONE
    }
}