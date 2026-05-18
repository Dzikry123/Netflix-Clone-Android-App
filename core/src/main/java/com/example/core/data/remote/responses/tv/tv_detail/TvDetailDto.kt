package com.example.core.data.remote.responses.tv.tv_detail


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TvDetailDto(
    @SerialName("adult")
    val adult: Boolean = false,
    @SerialName("backdrop_path")
    val backdropPath: String = "",
    @SerialName("created_by")
    val createdBy: List<CreatedBy> = listOf(),
    @SerialName("credits")
    val credits: Credits = Credits(),
    @SerialName("episode_run_time")
    val episodeRunTime: List<Int> = listOf(),
    @SerialName("first_air_date")
    val firstAirDate: String = "",
    @SerialName("genres")
    val genres: List<Genre> = listOf(),
    @SerialName("homepage")
    val homepage: String = "",
    @SerialName("id")
    val id: Int = 0,
    @SerialName("in_production")
    val inProduction: Boolean = false,
    @SerialName("languages")
    val languages: List<String> = listOf(),
    @SerialName("last_air_date")
    val lastAirDate: String = "",
    @SerialName("last_episode_to_air")
    val lastEpisodeToAir: LastEpisodeToAir = LastEpisodeToAir(),
    @SerialName("name")
    val name: String = "",
    @SerialName("networks")
    val networks: List<Network> = listOf(),
    @SerialName("next_episode_to_air")
    val nextEpisodeToAir: NextEpisodeToAir = NextEpisodeToAir(),
    @SerialName("number_of_episodes")
    val numberOfEpisodes: Int = 0,
    @SerialName("number_of_seasons")
    val numberOfSeasons: Int = 0,
    @SerialName("origin_country")
    val originCountry: List<String> = listOf(),
    @SerialName("original_language")
    val originalLanguage: String = "",
    @SerialName("original_name")
    val originalName: String = "",
    @SerialName("overview")
    val overview: String = "",
    @SerialName("popularity")
    val popularity: Double = 0.0,
    @SerialName("poster_path")
    val posterPath: String = "",
    @SerialName("production_companies")
    val productionCompanies: List<ProductionCompany> = listOf(),
    @SerialName("production_countries")
    val productionCountries: List<ProductionCountry> = listOf(),
    @SerialName("reviews")
    val reviews: Reviews = Reviews(),
    @SerialName("seasons")
    val seasons: List<Season> = listOf(),
    @SerialName("softcore")
    val softcore: Boolean = false,
    @SerialName("spoken_languages")
    val spokenLanguages: List<SpokenLanguage> = listOf(),
    @SerialName("status")
    val status: String = "",
    @SerialName("tagline")
    val tagline: String = "",
    @SerialName("type")
    val type: String = "",
    @SerialName("vote_average")
    val voteAverage: Double = 0.0,
    @SerialName("vote_count")
    val voteCount: Int = 0
)