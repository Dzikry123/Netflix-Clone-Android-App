package com.example.core.data.remote.responses.tv.tv_detail


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NextEpisodeToAir(
    @SerialName("air_date")
    val airDate: String = "",
    @SerialName("episode_number")
    val episodeNumber: Int = 0,
    @SerialName("episode_type")
    val episodeType: String = "",
    @SerialName("id")
    val id: Int = 0,
    @SerialName("name")
    val name: String = "",
    @SerialName("overview")
    val overview: String = "",
    @SerialName("production_code")
    val productionCode: String = "",
    @SerialName("runtime")
    val runtime: Int = 0,
    @SerialName("season_number")
    val seasonNumber: Int = 0,
    @SerialName("show_id")
    val showId: Int = 0,
    @SerialName("still_path")
    val stillPath: String = "",
    @SerialName("vote_average")
    val voteAverage: Double = 0.0,
    @SerialName("vote_count")
    val voteCount: Int = 0
)