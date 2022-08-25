package com.example.pixabaylibrary.features.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.pixabaylibrary.common_dto.PixabayResponse
import com.example.pixabaylibrary.features.pixabay.dto.Filter
import com.example.pixabaylibrary.features.pixabay.dto.PixabayImage
import com.example.pixabaylibrary.features.pixabay.dto.PixabayPagingData
import com.example.pixabaylibrary.repo.PixabayImageRepo
import com.example.pixabaylibrary.response_listener.Resources
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class PixabayViewModel @Inject constructor(
    private val repo: PixabayImageRepo) : ViewModel() {
    var query = "fruits"


    /*suspend fun getPixabayData(query : String): Flow<Resources<PixabayResponse<List<PixabayImage>>>> {
        return repo.getPixabayImagesList(query)
    }*/


     fun getPixabayPagingImages(): Flow<PagingData<PixabayPagingData.Hit>> {

         /*val pager= Pager(
            config = PagingConfig(
                pageSize = 25,
                enablePlaceholders = false,
                prefetchDistance = 20,
            ), pagingSourceFactory = {
                Timber.v("paging factory setup...")
                repo.getPixabayImagePageSource(query)
            }).flow

         pager.collectLatest {

         }*/
        return Pager(
            config = PagingConfig(
                pageSize = 25,
                enablePlaceholders = false,
                prefetchDistance = 20,
            ), pagingSourceFactory = {
                Timber.v("paging factory setup...")
                repo.getPixabayImagePageSource(query)
            }).flow.cachedIn(viewModelScope)

    }

    override fun onCleared() {
        super.onCleared()
        Timber.v("Pixabay viewmodel is cleared")
    }

}