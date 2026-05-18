package com.example.core.domain.model.movie

data class MovieDetail(
    val backdropPath: String,
    val genreIds: List<Int>,
    val id: Int,
    val originalLanguage: String,
    val originalTitle: String,
    val overview: String,
    val popularity: Double,
    val posterPath: String,
    val releaseDate: String,
    val title: String,
    val voteAverage: Double,
    val voteCount: Int,
    val video: Boolean,
    val castMovie: List<CastMovie>,
    val language: List<String>,
    val productionCountry: List<String>,
    val reviews: List<Review>,
    val runTime: String
)

data class CastMovie(
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

data class Review(
    val author: String,
    val content: String,
    val id: String,
    val createdAt: String,
    val rating: Double
)