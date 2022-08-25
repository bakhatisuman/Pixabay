package com.example.pixabaylibrary.repo

import android.content.Context
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.pixabaylibrary.R
import com.example.pixabaylibrary.common_dto.PixabayResponse
import com.example.pixabaylibrary.features.pixabay.dto.Filter
import com.example.pixabaylibrary.features.pixabay.dto.PixabayImage
import com.example.pixabaylibrary.features.pixabay.dto.PixabayPagingData
import com.example.pixabaylibrary.response_listener.Resources
import com.example.pixabaylibrary.response_listener.RetroCallBackFlow
import com.example.pixabaylibrary.service.PixabayPictureService
import com.example.pixabaylibrary.utils.NetworkUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.HttpException
import timber.log.Timber
import java.io.IOException
import java.net.SocketTimeoutException
import javax.inject.Inject

class PixabayImageRepo @Inject constructor(
    private val retroService : PixabayPictureService,
    private val context : Context
) {

    suspend fun getPixabayImagesList(query : String): Flow<Resources<PixabayResponse<List<PixabayImage>>>> {
        try {
            /*if (!NetworkUtils.hasInternetConnection(context)) {
                return flow {
                    emit(Resources.NoInternetAvailable(error = context.resources.getString(R.string.no_internet)))
                }
            }*/



            val response = retroService.sendPixabayImageListRequest(query)

            return RetroCallBackFlow(
                response,
                context
            ).getResponseAsFlow().flowOn(Dispatchers.IO)
        } catch (ex: SocketTimeoutException) {
            ex.printStackTrace()
            return flow {
                emit(Resources.OnException(exception = context.resources.getString(R.string.no_internet)))
            }
        } catch (ex: IOException) {
            ex.printStackTrace()
            return flow {
                emit(Resources.OnException(exception = context.resources.getString(R.string.io_exception)))
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
            return flow {
                emit(Resources.OnException(exception = context.resources.getString(R.string.internal_server_error)))
            }
        }

//        val response =geoKrishiService.getMarketPriceList(marketId, version)

    }



    fun getPixabayImagePageSource(query: String): PixabayImagePgSource {
        return PixabayImagePgSource(retroService, query)
    }


    class PixabayImagePgSource(
        private val apiService: PixabayPictureService,
        val query: String
    ) : PagingSource<Int, PixabayPagingData.Hit>() {

        init {
            Timber.v("inside PixabayPgSource")
        }

//        override val keyReuseSupported: Boolean
//            get() = super.keyReuseSupported

        override fun getRefreshKey(state: PagingState<Int, PixabayPagingData.Hit>): Int {
            return 1
        }


        override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PixabayPagingData.Hit> {
            Timber.v("inside load")
            val position = params.key ?: 1
            try {
                val response =
                    apiService.sendPaginationPixabayImageRequest(
                        position,
                        query
                    )
                val repos = response.hits
                //  Logger.log("${repos?.size}")
                Timber.v("$repos")

                val nextKey = if (repos.isEmpty()) {
                    // flow.value = FLWResponse.empty("No record found.")
                    null
                } else {

                    Timber.v("loadsize paging ${params.loadSize} key ${params.key}")
                    position+1
                }

                val result = repos.let {
                    LoadResult.Page<Int, PixabayPagingData.Hit>(
                        data = it,
                        prevKey = if (position == 1) null else position - 1,
                        nextKey = nextKey
                    )
                }

                return result
            } catch (exception: IOException) {
                Timber.v("" + exception.localizedMessage)
                return LoadResult.Error(exception)
            } catch (exception: HttpException) {
                Timber.v("" + exception.localizedMessage)
                return LoadResult.Error(exception)
            } catch (exception: Exception) {
                Timber.v("" + exception.localizedMessage)
                return LoadResult.Error(exception)
            }

        }
    }




}