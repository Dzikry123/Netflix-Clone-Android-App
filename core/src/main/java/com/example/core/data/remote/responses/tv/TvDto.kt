package com.example.core.data.remote.responses.tv


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TvDto(
    @SerialName("page")
    val page: Int = 0,
    @SerialName("results")
    val resultTvs: List<ResultTv?>? = listOf(),
    @SerialName("total_pages")
    val totalPages: Int = 0,
    @SerialName("total_results")
    val totalResults: Int = 0
)