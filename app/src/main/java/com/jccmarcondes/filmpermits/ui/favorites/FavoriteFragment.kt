package com.jccmarcondes.filmpermits.ui.favorites

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.jccmarcondes.filmpermits.R
import com.jccmarcondes.filmpermits.adapter.PermissionInfoAdapter
import com.jccmarcondes.filmpermits.data.model.FilmPermission
import com.jccmarcondes.filmpermits.databinding.FragmentFavoriteBinding
import com.jccmarcondes.filmpermits.ui.FilmPermitsActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteFragment : Fragment(R.layout.fragment_favorite),
    PermissionInfoAdapter.OnItemClickListener {

    private val viewModel: FavoriteViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentFavoriteBinding.bind(view)
        val permissionInfoAdapter = PermissionInfoAdapter(this)
        binding.apply {
            rvFavorites.apply {
                adapter = permissionInfoAdapter
                setHasFixedSize(true)
            }

            ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
                0,
                ItemTouchHelper.LEFT
            ) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    return false
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val favorite = permissionInfoAdapter.currentList[viewHolder.adapterPosition]
                    viewModel.onArticleSwiped(favorite)
                }
            }).attachToRecyclerView(rvFavorites)
        }

        viewModel.getAllFavorites().observe(viewLifecycleOwner) {
            permissionInfoAdapter.submitList(it)
        }

        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.favoriteEvent.collect { event ->
                when (event) {
                    is FavoriteViewModel.FavoriteEvent.ShowUndoDeleteFavoriteMessage -> {
                        Snackbar.make(requireView(), "Favorite Item Deleted!",Snackbar.LENGTH_LONG)
                            .setAction("UNDO"){
                                viewModel.onUndoDeleteClick(event.filmPermission)
                            }.show()
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        (activity as FilmPermitsActivity).showBottomNavigation(true)
    }

    override fun onItemClick(filmPermission: FilmPermission) {
        val action = FavoriteFragmentDirections.actionFavoriteFragmentToFilmPermissionFragment(filmPermission)
        findNavController().navigate(action)
    }
}