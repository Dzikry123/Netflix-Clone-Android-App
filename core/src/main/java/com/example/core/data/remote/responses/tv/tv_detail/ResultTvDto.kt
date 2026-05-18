package com.example.core.data.remote.responses.tv.tv_detail


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResultTvDto(
    @SerialName("author")
    val author: String = "",
    @SerialName("author_details")
    val authorDetails: AuthorDetails = AuthorDetails(),
    @SerialName("content")
    val content: String = "",
    @SerialName("created_at")
    val createdAt: String = "",
    @SerialName("id")
    val id: String = "",
    @SerialName("updated_at")
    val updatedAt: String = "",
    @SerialName("url")
    val url: String = ""
)