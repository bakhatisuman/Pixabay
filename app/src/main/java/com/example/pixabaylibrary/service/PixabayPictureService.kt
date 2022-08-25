package com.example.pixabaylibrary.service

import com.example.pixabaylibrary.common_dto.PixabayResponse
import com.example.pixabaylibrary.features.pixabay.dto.PixabayImage
import com.example.pixabaylibrary.features.pixabay.dto.PixabayPagingData
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface PixabayPictureService {
//    @Headers("Cache-Control")
    @GET("api/?key=29340359-05a70a56b164c21da47516544")
    suspend fun sendPixabayImageListRequest(@Query("q") query: String,) : Response<PixabayResponse<List<PixabayImage>>>



    @GET("api/?key=29340359-05a70a56b164c21da47516544")
    suspend fun sendPaginationPixabayImageRequest(
        @Query("page") page: Int,
        @Query("q") search: String = "",
    ) : PixabayPagingData

}