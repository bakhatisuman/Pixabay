package com.example.pixabaylibrary.common_dto

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class PixabayResponse<T> (
    @SerializedName("totalHits")
    @Expose
    var totalHits: Int = 0,
    @SerializedName("hits")
    @Expose var hits: T,
    @SerializedName("total")
    @Expose
    var total: Long
    ) : Serializable

