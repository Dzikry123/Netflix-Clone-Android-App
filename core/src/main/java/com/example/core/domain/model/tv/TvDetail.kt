package com.example.core.domain.model.tv


data class TvDetail(
    val id: Int,
    val backdropPath: String,
    val episodeRuntime: List<Int>,
    val firstAirDate: String,
    val genreIds: List<Int>,
    val homepage: String,
    val inProduction: Boolean,
    val languages: List<String>,
    val lastAirDate: String,
    val lastEpsToAir: LastEpsToAir,
    val name: String,
    val network: List<NetworkModel>,
    val nextEpsToAir: NextEpsToAir,
    val numberOfEpisodes: Int,
    val numberOfSeason: Int,
    val originalCountry: String,
    val originalLanguage: String,
    val originalName: String,
    val overview: String,
    val popularity: Double,
    val posterPath: String,
    val seasons: List<SeasonModel>,
    val softcore: Boolean,
    val status: String,
    val tagline: String,
    val type: String,
    val voteAverage: Double,
    val voteCount: Int,
    val productionCountry: List<String>,
    val productionCompanies: List<ProductionCompanyModel>,
    val castTv: List<CastTv>,
    val reviews: List<ReviewTv>
)

data class CastTv(
    val id: Int,
    val name: String,
    val genderRole: String,
    val character: String,
    val profilePath: String?,
) {
    // Agar tidak error jika nama cuma 1 kata
    private val nameParts = name.split(" ")
    val firstName = nameParts.getOrNull(0) ?: ""
    val lastName = if (nameParts.size > 1) nameParts.drop(1).joinToString(" ") else ""
}

data class ReviewTv(
    val author: String,
    val content: String,
    val id: String,
    val createdAt: String,
    val rating: Double
)

data class LastEpsToAir(
    val airDate: String,
    val episodeNumber: Int,
    val episodeType: String,
    val id: Int,
    val name: String,
    val overview: String,
    val productionCode: String,
    val runtime: String,
    val seasonNumber: Int,
    val showId: Int,
    val stillPath: String,
    val voteAverage: Double,
    val voteCount: Int
)

data class NextEpsToAir(
    val airDate: String,
    val episodeNumber: Int,
    val episodeType: String,
    val id: Int,
    val name: String,
    val overview: String,
    val productionCode: String,
    val runtime: String,
    val seasonNumber: Int,
    val showId: Int,
    val stillPath: String,
    val voteAverage: Double,
    val voteCount: Int
)

data class NetworkModel(
    val id: Int,
    val logoPath: String,
    val name: String,
    val originCountry: String
)

data class SeasonModel(
    val airDate: String,
    val episodeCount: Int,
    val id: Int,
    val name: String,
    val overview: String,
    val posterPath: String,
    val seasonNumber: Int,
    val voteAverage: Double
)

data class ProductionCompanyModel(
    val id: Int,
    val logoPath: String,
    val name: String,
    val originCountry: String
)

