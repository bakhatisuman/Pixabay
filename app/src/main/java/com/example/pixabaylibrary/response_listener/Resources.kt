package com.example.pixabaylibrary.response_listener

sealed class Resources<T>(
    var data: T? = null,
    var message: String? = null,
    var error: String? = null,
    var exception: String? = null,
    var isTokenExpired: Boolean = false
) {
    class Success<T>(data: T) : Resources<T>(data = data)

    class Loading<T>() : Resources<T>()

    class Error<T>(
        message: String?, data: T? = null
    ) : Resources<T>(data = data, message = message)

    class TokenExpired<T>(
        error: String?, isTokenExpired: Boolean
    ) : Resources<T>( error = error, isTokenExpired = isTokenExpired)

    class NoInternetAvailable<T>(
        error: String?
    ) : Resources<T>(error = error)

    class OnException<T>(
        exception: String?
    ) : Resources<T>(exception = exception)

    class OnSocketTimeoutException<T>(
        exception: String?
    ) : Resources<T>(exception = exception)

}