package com.example.core.data.remote.responses.tv.tv_detail


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Credits(
    @SerialName("cast")
    val castTvDto: List<CastTvDto> = listOf(),
    @SerialName("crew")
    val crew: List<Crew> = listOf()
)