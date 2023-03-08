package com.jccmarcondes.filmpermits.ui.filmpermits

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AbsListView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jccmarcondes.filmpermits.R
import com.jccmarcondes.filmpermits.adapter.PermissionInfoAdapter
import com.jccmarcondes.filmpermits.data.model.FilmPermission
import com.jccmarcondes.filmpermits.databinding.FragmentAllFilmPermitsBinding
import com.jccmarcondes.filmpermits.ui.FilmPermitsActivity
import com.jccmarcondes.filmpermits.util.QUERY_PAGE_SIZE
import com.jccmarcondes.filmpermits.util.Resource
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "AllFilmPermitsFragment"
@AndroidEntryPoint
class AllFilmPermitsFragment : Fragment(R.layout.fragment_all_film_permits), PermissionInfoAdapter.OnItemClickListener {

    private val viewModel: AllFilmPermitsViewModel by viewModels()
    var isLoading = false
    var isLastPage = false
    var isScrolling = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentAllFilmPermitsBinding.bind(view)
        val permissionInfoAdapter = PermissionInfoAdapter(this)

        binding.apply {
            rvFilmPermits.apply {
                adapter = permissionInfoAdapter
                setHasFixedSize(true)
                addOnScrollListener(this@AllFilmPermitsFragment.scrollListener)
            }
        }

        viewModel.filmPermission.observe(viewLifecycleOwner) {
            when(it){
                is Resource.Success -> {
                    binding.paginationProgressBar.visibility = View.INVISIBLE
                    isLoading = false
                    it.data?.let { filmPermissionResponse ->
                        permissionInfoAdapter.submitList(filmPermissionResponse)
                        val totalPages = filmPermissionResponse.size / QUERY_PAGE_SIZE + 2
                        isLastPage = filmPermissionResponse.size == totalPages
                        if(isLastPage)
                            binding.rvFilmPermits.setPadding(0,0,0,0)
                    }
                }
                is Resource.Error -> {
                    binding.paginationProgressBar.visibility = View.INVISIBLE
                    isLoading = true
                    it.message?.let { message ->
                        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                        Log.e(TAG, "Error: $message")
                    }
                }
                is Resource.Loading -> {
                    binding.paginationProgressBar.visibility = View.VISIBLE
                }
            }
        }
    }

    private val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){ //State is scrolling
                isScrolling = true
            }
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val totalVisibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount

            val isNotLoadingAndNotLastPage = !isLoading && !isLastPage
            val isAtLastItem = firstVisibleItemPosition + totalVisibleItemCount >= totalItemCount
            val isNotAtBeginning = firstVisibleItemPosition >= 0
            val isTotalMoreThanVisible = totalItemCount >= QUERY_PAGE_SIZE
            val shouldPaginate = isNotLoadingAndNotLastPage && isAtLastItem && isNotAtBeginning && isTotalMoreThanVisible && isScrolling

            if(shouldPaginate){
                viewModel.getFilmPermission()
                isScrolling = false
            }
        }
    }

    override fun onResume() {
        super.onResume()
        (activity as FilmPermitsActivity).showBottomNavigation(true)
    }

    override fun onItemClick(filmPermission: FilmPermission) {
        val action = AllFilmPermitsFragmentDirections.actionAllFilmPermitsFragmentToFilmPermissionFragment(filmPermission)
        findNavController().navigate(action)
    }
}