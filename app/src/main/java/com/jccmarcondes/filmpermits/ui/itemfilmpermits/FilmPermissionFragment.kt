package com.jccmarcondes.filmpermits.ui.itemfilmpermits

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.jccmarcondes.filmpermits.R
import com.jccmarcondes.filmpermits.databinding.FragmentFilmPermissionBinding
import com.jccmarcondes.filmpermits.ui.FilmPermitsActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FilmPermissionFragment : Fragment(R.layout.fragment_film_permission) {

    private val viewModel: FilmPermissionViewModel by viewModels()
    private val args by navArgs<FilmPermissionFragmentArgs>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as FilmPermitsActivity).showBottomNavigation(false)

        val binding = FragmentFilmPermissionBinding.bind(view)
        binding.apply {
            val filmPermission = args.filmpermits
            filmPermission.category?.let {
                val urlImageType = getUrlImageType(it)
                Glide.with(this@FilmPermissionFragment)
                    .load(urlImageType)
                    .into(ivEventImage)
            }
            tvCategory.text = filmPermission.category
            tvEventType.text = filmPermission.eventType
            tvPublishedAt.text = "${filmPermission.formattedStartDateTimeAt} - ${filmPermission.formattedEndDateTimeAt}"
            tvAgency.text = filmPermission.eventAgency
            tvParkingHeld.text = filmPermission.parkingHeld
            tvBorough.text = filmPermission.borough
            tvCountry.text = filmPermission.country
            tvZipCode.text = filmPermission.zipcode

            fab.setOnClickListener {
                viewModel.saveFavorite(filmPermission)
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.filmPermissionEvent.collect { event ->
                when (event) {
                    is FilmPermissionViewModel.FilmPermissionEvent.ShowFilmPermissionSavedMessage -> {
                        Snackbar.make(requireView(), event.message, Snackbar.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun getUrlImageType(category: String): String {
        return when (category) {
            THEATER -> THEATER_IMAGE_URL
            TELEVISION -> TELEVISION_IMAGE_URL
            PHOTOGRAPHY -> PHOTOGRAPHY_IMAGE_URL
            COMMERCIAL -> COMMERCIAL_IMAGE_URL
            WEB -> WEB_IMAGE_URL
            else -> MULTIMEDIA_IMAGE_URL
        }
    }

    companion object {
        private const val THEATER = "Theater"
        private const val TELEVISION = "Television"
        private const val PHOTOGRAPHY = "Still Photography"
        private const val COMMERCIAL = "Commercial"
        private const val WEB = "WEB"

        private const val THEATER_IMAGE_URL = "https://st.depositphotos.com/2656329/4008/v/450/depositphotos_40089595-stock-illustration-theater-stage-vector-illustration.jpg"
        private const val TELEVISION_IMAGE_URL = "https://images.freeimages.com/images/large-previews/c36/tv-camera-1517392.jpg"
        private const val PHOTOGRAPHY_IMAGE_URL = "https://res.cloudinary.com/dte7upwcr/image/upload/v1657219907/blog/blog2/cameras-fotograficas/image_8-camera-fotografica.jpg"
        private const val COMMERCIAL_IMAGE_URL = "https://neilpatel.com/wp-content/uploads/2019/06/ilustracao-de-area-comercial-em-meio-urbano.jpeg"
        private const val WEB_IMAGE_URL = "https://img.freepik.com/vetores-gratis/os-designers-estao-trabalhando-no-design-da-pagina-da-web-web-design-interface-do-usuario-e-organizacao-de-conteudo-de-experiencia-do-usuario_335657-4403.jpg"
        private const val MULTIMEDIA_IMAGE_URL = "https://static.javatpoint.com/computer/images/what-is-multimedia.jpg"
    }
}