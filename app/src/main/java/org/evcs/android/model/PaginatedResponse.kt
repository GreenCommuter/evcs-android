package org.evcs.android.model

import com.google.gson.annotations.SerializedName

class PaginatedResponse<T> {
    private val currentPage = 0
    private val perPage = 0
    val totalPages = 0
    private val totalCount = 0

    @SerializedName("page")
    val page: List<T>? = null
}