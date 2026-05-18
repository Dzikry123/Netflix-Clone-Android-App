package com.example.core.data.remote.responses.tv.tv_detail


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AuthorDetails(
    @SerialName("avatar_path")
    val avatarPath: String = "",
    @SerialName("name")
    val name: String = "",
    @SerialName("rating")
    val rating: Double = 0.0,
    @SerialName("username")
    val username: String = ""
)