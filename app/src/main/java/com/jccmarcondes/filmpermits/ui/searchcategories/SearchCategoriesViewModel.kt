package com.jccmarcondes.filmpermits.ui.searchcategories

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jccmarcondes.filmpermits.data.model.FilmPermission
import com.jccmarcondes.filmpermits.repository.FilmPermitsRepository
import com.jccmarcondes.filmpermits.util.NetworkUtil.Companion.hasInternetConnection
import com.jccmarcondes.filmpermits.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class SearchCategoriesViewModel @Inject constructor(
    private val filmPermitsRepository: FilmPermitsRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {

    val searchCategories: MutableLiveData<Resource<List<FilmPermission>>> = MutableLiveData()

    fun searchCategory(searchQuery: String) = viewModelScope.launch {
        safeSearchCategoriesCall(searchQuery)
    }

    private suspend fun safeSearchCategoriesCall(searchQuery: String){
        searchCategories.postValue(Resource.Loading())
        try{
            if(hasInternetConnection(context)){
                val response = filmPermitsRepository.searchCategory(searchQuery)
                searchCategories.postValue(handleSearchCategoriesResponse(response))
            }
            else
                searchCategories.postValue(Resource.Error("No Internet Connection"))
        }
        catch (ex: Exception){
            when(ex){
                is IOException -> searchCategories.postValue(Resource.Error("Network Failure"))
                else -> searchCategories.postValue(Resource.Error("Conversion Error"))
            }
        }
    }

    private fun handleSearchCategoriesResponse(response: Response<List<FilmPermission>>): Resource<List<FilmPermission>> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }
}