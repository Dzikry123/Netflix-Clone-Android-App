package com.example.core.data.remote.responses.movie.movie_detail


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Credits(
    @SerialName("cast")
    val cast: List<CastMovieDto> = listOf(),
    @SerialName("crew")
    val crew: List<Crew> = listOf()
)