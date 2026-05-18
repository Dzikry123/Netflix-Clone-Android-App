package com.example.core.mapper.tv

import com.example.core.data.remote.responses.tv.TvDto
import com.example.core.domain.model.tv.Tv
import com.example.core.mapper.ApiMapper

class TvApiMapperImpl(): ApiMapper<List<Tv>, TvDto> {
    override fun mapToDomain(apiDto: TvDto): List<Tv> {
        return apiDto.resultTvs?.map { resultTv ->
            Tv(
                backdropPath = formatEmptyValue(resultTv?.backdropPath, "backdropPath"),
                firstAirDate = formatEmptyValue(resultTv?.firstAirDate, "firstAirDate"),
                genreIds = formatGenre(resultTv?.genreIds),
                id = resultTv?.id ?: 0,
                name = formatEmptyValue(resultTv?.name, "name"),
                originCountry = formatEmptyListValue(resultTv?.originCountry, "originalCountry"),
                originalLanguage = formatEmptyValue(resultTv?.originalLanguage, "originalLanguage"),
                originalName = formatEmptyValue(resultTv?.originalName, "originalName"),
                overview = formatEmptyValue(resultTv?.overview, "overview"),
                popularity = resultTv?.popularity ?: 0.0,
                posterPath = formatEmptyValue(resultTv?.posterPath, "posterPath"),
                softcore = resultTv?.softcore ?: false,
                voteAverage = resultTv?.voteAverage ?: 0.0,
                voteCount = resultTv?.voteCount ?: 0,
                isFavorite = false
            )
        }?: emptyList()
    }

    private fun formatEmptyValue(value: String?, default: String = ""): String {
        if (value.isNullOrEmpty()) return "Unknown $default"

        return value
    }

    private fun formatGenre( genreIds: List<Int?>? ): List<Int> {
        return genreIds?.filterNotNull() ?: emptyList()
    }

    private fun formatEmptyListValue(value: List<String>?, default: String = ""): String {
        return if (value.isNullOrEmpty()) {
            "Unknown $default"
        } else {
            // This converts ["US", "ID"] into "US, ID"
            value.joinToString(", ")
        }
    }


}