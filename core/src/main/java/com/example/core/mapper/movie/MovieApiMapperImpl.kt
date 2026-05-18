package com.example.core.mapper.movie

import com.example.core.data.remote.responses.movie.MovieDto
import com.example.core.domain.model.movie.Movie
import com.example.core.mapper.ApiMapper

class MovieApiMapperImpl: ApiMapper<List<Movie>, MovieDto> {
    override fun mapToDomain(apiDto: MovieDto): List<Movie> {
        return apiDto.results?.map { result ->
            Movie(
                backdropPath = formatEmptyValue(result?.backdropPath,  "backdropPath"),
                genreIds = formatGenre(result?.genreIds),
                id = result?.id ?: 0,
                originalLanguage = formatEmptyValue(result?.originalLanguage, "en"),
                originalTitle = formatEmptyValue(result?.originalTitle, "title"),
                overview = formatEmptyValue(result?.overview, "overview"),
                popularity = result?.popularity ?: 0.0,
                posterPath = formatEmptyValue(result?.posterPath, "posterPath"),
                releaseDate = formatEmptyValue(result?.releaseDate, "date"),
                title = formatEmptyValue(result?.title, "title"),
                voteAverage = result?.voteAverage ?: 0.0,
                voteAccount = result?.voteCount ?: 0,
                video = result?.video ?: false,
                isFavorite = false
            )
        } ?: emptyList()
    }

    private fun formatEmptyValue(value: String?, default: String = ""): String {
        if (value.isNullOrEmpty()) return "Unknown $default"
        return value
    }

    private fun formatGenre( genreIds: List<Int?>? ): List<Int> {
        return genreIds?.filterNotNull() ?: emptyList()
    }

}