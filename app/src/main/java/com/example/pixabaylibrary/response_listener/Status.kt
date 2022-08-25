package com.example.pixabaylibrary.response_listener

data class Status<T>(
    var isLoading: Boolean = false,
    var data: T? = null,
    var error: String?= "",
    var noInternetConnection: String? = "",
    var isTokenExpired: Boolean = false,
)