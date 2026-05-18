package com.example.core.mapper.movie

import com.example.core.data.remote.responses.movie.movie_detail.CastMovieDto
import com.example.core.data.remote.responses.movie.movie_detail.MovieDetailDto
import com.example.core.domain.model.movie.CastMovie
import com.example.core.domain.model.movie.MovieDetail
import com.example.core.domain.model.movie.Review
import com.example.core.mapper.ApiMapper
import java.text.SimpleDateFormat
import java.util.Locale

class MovieDetailMapperImp : ApiMapper<MovieDetail, MovieDetailDto> {
    override fun mapToDomain(apiDto: MovieDetailDto): MovieDetail {
        return MovieDetail(
            backdropPath = formatEmptyValue(apiDto.backdropPath),
            genreIds = apiDto.genres
                ?.mapNotNull { it?.id }
                ?: emptyList(),
            id = apiDto.id ?: 0,
            originalLanguage = formatEmptyValue(apiDto.originalLanguage, "language"),
            originalTitle = formatEmptyValue(apiDto.originalTitle, "title"),
            overview = formatEmptyValue(apiDto.overview, "overview"),
            popularity = apiDto.popularity ?: 0.0,
            posterPath = formatEmptyValue(apiDto.posterPath),
            releaseDate = formatEmptyValue(apiDto.releaseDate, "date"),
            title = formatEmptyValue(apiDto.title, "title"),
            voteAverage = apiDto.voteAverage ?: 0.0,
            voteCount = apiDto.voteCount ?: 0,
            video = apiDto.video ?: false,

            castMovie = formatCast(apiDto.credits?.cast),

            language = apiDto.spokenLanguages
                ?.map { formatEmptyValue(it?.englishName) }
                ?: emptyList(),

            productionCountry = apiDto.productionCountries
                ?.map { formatEmptyValue(it?.name) }
                ?: emptyList(),

            reviews = apiDto.reviews?.results
                ?.map { result ->
                    Review(
                        author = formatEmptyValue(result?.author),
                        content = formatEmptyValue(result?.content),
                        createdAt = formatTimeStamp(time = result?.createdAt ?: "0"),
                        id = formatEmptyValue(result?.id),
                        rating = result?.authorDetails?.rating ?: 0.0
                    )
                } ?: emptyList(),

            runTime = convertMinutesToHours(apiDto.runtime ?: 0)
        )
    }

    private fun formatTimeStamp(pattern: String = "dd MMM yyyy", time: String): String {
        return try {
            val inputFormatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss z", Locale.US)
            val outputFormatter = SimpleDateFormat(pattern, Locale.getDefault())

            val date = inputFormatter.parse(time)
            date?.let { outputFormatter.format(it) } ?: time

        } catch (e: Exception) {
            e.printStackTrace()
            time // fallback biar app tidak crash
        }
    }

    private fun convertMinutesToHours(minutes: Int): String {
        val hours = minutes / 60
        val remainingMinutes = minutes % 60
        return "${hours}h:${remainingMinutes}"
    }

    private fun formatEmptyValue(value: String?, default: String = ""): String {
        if (value.isNullOrEmpty()) return "Unknown $default"
        return value
    }

    private fun formatCast(castMovieDto: List<CastMovieDto?>?): List<CastMovie> {
        return castMovieDto?.map {
            val genderRole = if (it?.gender == 2) "Actor" else "Actress"
            CastMovie(
                id = it?.id ?: 0,
                name = formatEmptyValue(it?.name),
                genderRole = genderRole,
                character = formatEmptyValue(it?.character),
                profilePath = it?.profilePath
            )
        } ?: emptyList()
    }
}