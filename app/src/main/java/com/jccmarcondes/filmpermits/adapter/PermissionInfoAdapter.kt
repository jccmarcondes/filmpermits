package com.jccmarcondes.filmpermits.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.jccmarcondes.filmpermits.data.model.FilmPermission
import com.jccmarcondes.filmpermits.databinding.ItemFilmPermissionPreviewBinding

class PermissionInfoAdapter(private val listener: OnItemClickListener): ListAdapter<FilmPermission, PermissionInfoAdapter.PermissionViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PermissionViewHolder {
        val binding = ItemFilmPermissionPreviewBinding.inflate(LayoutInflater.from(parent.context), parent,false)
        return PermissionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PermissionViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }

    inner class PermissionViewHolder(private val binding: ItemFilmPermissionPreviewBinding): RecyclerView.ViewHolder(binding.root) {
        init {
            binding.apply {
                root.setOnClickListener {
                    val position = adapterPosition
                    if(position != RecyclerView.NO_POSITION){
                        val filmPermission = getItem(position)
                        listener.onItemClick(filmPermission)
                    }
                }
            }
        }

        fun bind(filmPermission: FilmPermission){
            binding.apply {
                filmPermission.category?.let {
                    val urlImageType = getUrlImageType(it)
                    Glide.with(itemView)
                        .load(urlImageType)
                        .into(ivEventImage)
                }
                tvCategory.text = filmPermission.category
                tvEventType.text = filmPermission.eventType
                tvPublishedAt.text = "${filmPermission.formattedStartDateTimeAt} - ${filmPermission.formattedEndDateTimeAt}"
                tvAgency.text = filmPermission.eventAgency
            }
        }
    }

    private fun getUrlImageType(category: String): String {
        return when (category) {
            THEATER -> THEATER_IMAGE_URL
            TELEVISION -> TELEVISION_IMAGE_URL
            PHOTOGRAPHY -> PHOTOGRAPHY_IMAGE_URL
            COMMERCIAL ->  COMMERCIAL_IMAGE_URL
            WEB -> WEB_IMAGE_URL
            else -> MULTIMEDIA_IMAGE_URL
        }
    }

    interface OnItemClickListener{
        fun onItemClick(filmPermission: FilmPermission)
    }


    class DiffCallback : DiffUtil.ItemCallback<FilmPermission>(){
        override fun areItemsTheSame(oldItem: FilmPermission, newItem: FilmPermission): Boolean {
            return oldItem.eventId == newItem.eventId
        }

        override fun areContentsTheSame(oldItem: FilmPermission, newItem: FilmPermission): Boolean {
            return oldItem == newItem
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