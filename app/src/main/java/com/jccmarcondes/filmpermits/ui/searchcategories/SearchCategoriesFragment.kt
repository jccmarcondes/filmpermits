package com.jccmarcondes.filmpermits.ui.searchcategories

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AbsListView
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jccmarcondes.filmpermits.R
import com.jccmarcondes.filmpermits.adapter.PermissionInfoAdapter
import com.jccmarcondes.filmpermits.data.model.FilmPermission
import com.jccmarcondes.filmpermits.databinding.FragmentSearchCategoriesBinding
import com.jccmarcondes.filmpermits.ui.FilmPermitsActivity
import com.jccmarcondes.filmpermits.util.QUERY_PAGE_SIZE
import com.jccmarcondes.filmpermits.util.Resource
import com.jccmarcondes.filmpermits.util.SEARCH_FILM_PERMISSIONS_TIME_DELAY
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private const val TAG = "SearchCategoriesFragment"

@AndroidEntryPoint
class SearchCategoriesFragment : Fragment(R.layout.fragment_search_categories),
    PermissionInfoAdapter.OnItemClickListener {

    private val viewModel: SearchCategoriesViewModel by viewModels()
    var isLoading = false
    var isLastPage = false
    var isScrolling = false

    lateinit var binding: FragmentSearchCategoriesBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentSearchCategoriesBinding.bind(view)
        val permissionInfoAdapter = PermissionInfoAdapter(this)

        binding.apply {
            rvSearchCategories.apply {
                adapter = permissionInfoAdapter
                setHasFixedSize(true)
                addOnScrollListener(this@SearchCategoriesFragment.scrollListener)
            }
        }

        var job: Job? = null
        binding.etSearch.addTextChangedListener { editable ->
            job?.cancel()
            job = MainScope().launch {
                delay(SEARCH_FILM_PERMISSIONS_TIME_DELAY)
                editable?.let {
                    if (editable.toString().isNotEmpty()) {
                        viewModel.searchCategory(editable.toString())
                    }
                }
            }
        }

        viewModel.searchCategories.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Success -> {
                    isLoading = false
                    binding.paginationProgressBar.visibility = View.INVISIBLE
                    it.data?.let { agenciesResponse ->
                        permissionInfoAdapter.submitList(agenciesResponse)
                        val totalPages = agenciesResponse.size / QUERY_PAGE_SIZE + 2
                        isLastPage = agenciesResponse.size == totalPages
                        if(isLastPage)
                            binding.rvSearchCategories.setPadding(0,0,0,0)
                    }
                }
                is Resource.Error -> {
                    isLoading = true
                    binding.paginationProgressBar.visibility = View.INVISIBLE
                    it.message?.let { message ->
                        Log.e(TAG, "Error: $message")
                        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
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
                viewModel.searchCategory(binding.etSearch.text.toString())
                isScrolling = false
            }
        }
    }

    override fun onResume() {
        super.onResume()
        (activity as FilmPermitsActivity).showBottomNavigation(true)
    }

    override fun onItemClick(filmPermission: FilmPermission) {
        val action = SearchCategoriesFragmentDirections.actionSearchCategoriesFragmentToFilmPermissionFragment(filmPermission)
        findNavController().navigate(action)
    }
}