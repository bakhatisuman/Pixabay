package com.example.pixabaylibrary.response_listener

import android.content.Context
import com.example.pixabaylibrary.R
import com.example.pixabaylibrary.utils.NetworkUtils
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import org.json.JSONObject
import retrofit2.Response
import timber.log.Timber
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class RetroCallBackFlow<T>(private val response: Response<T>, val context: Context) {

    fun getResponseAsFlow(): Flow<Resources<T>> = flow {
            try {
                emit(Resources.Loading())
                 if (response.isSuccessful || response.code() == 201 || response.code() == 200) {
                    emit(Resources.Loading())
                    emit(Resources.Success(data = response.body()))
                } else {
                    emit(Resources.Loading())
                    val responseBody = response.errorBody()
                    if (response.code() in 400..499) {
                        try {
                            val jsonObject = responseBody?.string()?.let { JSONObject(it) }
                            val message = jsonObject?.getString("message")
                            if (response.code() == 401) {
                                emit(
                                    Resources.TokenExpired(
                                        error = context.getString(R.string.token_expired),
                                        isTokenExpired = true
                                    )
                                )
                            } else if (response.code() == 404) {
                                emit(Resources.Error(message = context.getString(R.string.not_found)))
                            } else {
                                emit(Resources.Error(message = message))
                            }
                        } catch (ex: SocketTimeoutException) {
                            emit(
                                Resources.OnSocketTimeoutException(exception = context.getString(R.string.socket_time_out_exception))
                            )
                        } catch (ex: Exception) {
                            emit(Resources.OnException(exception = context.getString(R.string.internal_server_error)))
                        }
                    } else if (response.code() >= 500) {
                        if (!NetworkUtils.hasInternetConnection(context)){
                            emit(Resources.NoInternetAvailable(error = context.getString(R.string.no_internet)))
                        }else{
                            emit(Resources.OnException(exception = context.getString(R.string.server_error)))
                        }

                    }

                }
            } catch (ex: UnknownHostException) {
                Timber.v("come in try catch UnknownHostException 2")
                emit(
                    Resources.NoInternetAvailable(error = context.getString(R.string.no_internet))
                )
            } catch (ex: SocketTimeoutException) {
                emit(
                    Resources.OnSocketTimeoutException(exception = context.getString(R.string.socket_time_out_exception))
                )
            } catch (ex: IOException) {
                emit(Resources.OnException(exception = context.getString(R.string.io_exception)))
            } catch (ex: IllegalStateException) {
                emit(Resources.OnException(exception = context.getString(R.string.internal_server_error)))
            } catch (ex: NullPointerException) {
                emit(Resources.OnException(exception = context.getString(R.string.internal_server_error)))
            } catch (ex: Exception) {
                emit(Resources.OnException(exception = context.getString(R.string.internal_server_error)))
            }
//        }


    }.catch {
        Timber.v("come in flow catch block")
        emit(Resources.OnException(exception = context.getString(R.string.internal_server_error)))

    } as Flow<Resources<T>>
}