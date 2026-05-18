package com.example.core.mapper.tv

import com.example.core.data.remote.responses.tv.tv_detail.CastTvDto
import com.example.core.data.remote.responses.tv.tv_detail.LastEpisodeToAir
import com.example.core.data.remote.responses.tv.tv_detail.Network
import com.example.core.data.remote.responses.tv.tv_detail.NextEpisodeToAir
import com.example.core.data.remote.responses.tv.tv_detail.ProductionCompany
import com.example.core.data.remote.responses.tv.tv_detail.Season
import com.example.core.data.remote.responses.tv.tv_detail.TvDetailDto
import com.example.core.domain.model.tv.CastTv
import com.example.core.domain.model.tv.LastEpsToAir
import com.example.core.domain.model.tv.NetworkModel
import com.example.core.domain.model.tv.NextEpsToAir
import com.example.core.domain.model.tv.ProductionCompanyModel
import com.example.core.domain.model.tv.ReviewTv
import com.example.core.domain.model.tv.SeasonModel
import com.example.core.domain.model.tv.TvDetail
import com.example.core.mapper.ApiMapper
import java.text.SimpleDateFormat
import java.util.Locale

class TvDetailMapperImpl: ApiMapper<TvDetail, TvDetailDto> {
    override fun mapToDomain(apiDto: TvDetailDto): TvDetail {
        return TvDetail(
            id = apiDto?.id ?: 0,
            backdropPath = formatEmptyValue(apiDto.backdropPath),
            episodeRuntime = apiDto?.episodeRunTime?.map { it } ?: emptyList(),
            firstAirDate = formatEmptyValue(apiDto?.firstAirDate),
            genreIds = apiDto?.genres?.map { formatEmptyValue(it?.name) } ?: emptyList(),
            homepage = formatEmptyValue(apiDto?.homepage),
            inProduction = apiDto?.inProduction ?: false,
            languages = apiDto.languages?.map {
                formatEmptyValue(it)
            } ?: emptyList(),
            lastAirDate = formatEmptyValue(apiDto?.lastAirDate),
            lastEpsToAir = formatLastEpisodeToAir(apiDto?.lastEpisodeToAir),
            name = formatEmptyValue(apiDto?.name),
            network = formatNetwork(apiDto?.networks),
            nextEpsToAir = formatNextEpisodeToAir(apiDto?.nextEpisodeToAir),
            numberOfEpisodes = apiDto.numberOfEpisodes,
            numberOfSeason = apiDto.numberOfSeasons,
            originalCountry = apiDto.originCountry
                ?.joinToString(", ")
                ?: "Unknown Country",
            originalLanguage = formatEmptyValue(apiDto?.originalLanguage),
            originalName = formatEmptyValue(apiDto?.originalName),
            overview = formatEmptyValue(apiDto?.overview),
            popularity = apiDto?.popularity ?: 0.0,
            posterPath = formatEmptyValue(apiDto?.posterPath),
            seasons = formatSeason(apiDto?.seasons),
            softcore = apiDto?.softcore ?: false,
            status = formatEmptyValue(apiDto?.status),
            tagline = formatEmptyValue(apiDto?.tagline),
            type = formatEmptyValue(apiDto?.type),
            voteAverage = apiDto?.voteAverage ?: 0.0,
            voteCount = apiDto?.voteCount ?: 0,
            productionCountry = apiDto.productionCountries?.map {
                formatEmptyValue(it?.name)
            } ?: emptyList(),
            productionCompanies = formatProductionCompany(apiDto?.productionCompanies),
            castTv = formatCast(apiDto?.credits?.castTvDto),
            reviews = apiDto.reviews?.resultTvDtos
                ?.map { resultTvDto ->
                    ReviewTv(
                        author = formatEmptyValue(resultTvDto?.author),
                        content = formatEmptyValue(resultTvDto?.content),
                        createdAt = formatTimeStamp(time = resultTvDto?.createdAt ?: "0"),
                        id = formatEmptyValue(resultTvDto?.id),
                        rating = resultTvDto?.authorDetails?.rating ?: 0.0
                    )
                } ?: emptyList(),
        )
    }
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

private fun formatCast(castTvDto: List<CastTvDto?>?): List<CastTv> {
    return castTvDto?.map {
        val genderRole = if (it?.gender == 2) "Actor" else "Actress"
        CastTv(
            id = it?.id ?: 0,
            name = formatEmptyValue(it?.name),
            genderRole = genderRole,
            character = formatEmptyValue(it?.character),
            profilePath = formatEmptyValue(it?.profilePath)
        )
    } ?: emptyList()
}

private fun formatLastEpisodeToAir(lastEps: LastEpisodeToAir?): LastEpsToAir {
    return LastEpsToAir(
        airDate = formatEmptyValue(lastEps?.airDate),
        episodeNumber = lastEps?.episodeNumber ?: 0,
        episodeType = formatEmptyValue(lastEps?.episodeType),
        id = lastEps?.id ?: 0,
        name = formatEmptyValue(lastEps?.name),
        overview = formatEmptyValue(lastEps?.overview),
        productionCode = formatEmptyValue(lastEps?.productionCode),
        runtime = convertMinutesToHours(lastEps?.runtime ?: 0),
        seasonNumber = lastEps?.seasonNumber ?: 0,
        showId = lastEps?.showId ?: 0,
        stillPath = formatEmptyValue(lastEps?.stillPath),
        voteAverage = lastEps?.voteAverage ?: 0.0,
        voteCount = lastEps?.voteCount ?: 0
    )
}

private fun formatNextEpisodeToAir(nextEps: NextEpisodeToAir?): NextEpsToAir {
    return NextEpsToAir(
        airDate = formatEmptyValue(nextEps?.airDate),
        episodeNumber = nextEps?.episodeNumber ?: 0,
        episodeType = formatEmptyValue(nextEps?.episodeType),
        id = nextEps?.id ?: 0,
        name = formatEmptyValue(nextEps?.name),
        overview = formatEmptyValue(nextEps?.overview),
        productionCode = formatEmptyValue(nextEps?.productionCode),
        runtime = convertMinutesToHours(nextEps?.runtime ?: 0),
        seasonNumber = nextEps?.seasonNumber ?: 0,
        showId = nextEps?.showId ?: 0,
        stillPath = formatEmptyValue(nextEps?.stillPath),
        voteAverage = nextEps?.voteAverage ?: 0.0,
        voteCount = nextEps?.voteCount ?: 0
    )
}

private fun formatNetwork(network: List<Network?>?): List<NetworkModel> {
    return network?.map {
        NetworkModel(
            id = it?.id ?: 0,
            logoPath = formatEmptyValue(it?.logoPath),
            name = formatEmptyValue(it?.name),
            originCountry = formatEmptyValue(it?.originCountry)
        )
    } ?: emptyList()
}

private fun formatProductionCompany(prodCompany: List<ProductionCompany?>?): List<ProductionCompanyModel> {
    return prodCompany?.map {
        ProductionCompanyModel(
            id = it?.id ?: 0,
            logoPath = formatEmptyValue(it?.logoPath),
            name = formatEmptyValue(it?.name),
            originCountry = formatEmptyValue(it?.originCountry)
        )
    } ?: emptyList()
}

private fun formatSeason(season: List<Season?>?): List<SeasonModel> {
    return season?.map {
        SeasonModel(
            id = it?.id ?: 0,
            airDate = formatEmptyValue(it?.airDate),
            episodeCount = it?.episodeCount ?: 0,
            name = formatEmptyValue(it?.name),
            overview = formatEmptyValue(it?.overview),
            posterPath = formatEmptyValue(it?.posterPath),
            seasonNumber = it?.seasonNumber ?: 0,
            voteAverage = it?.voteAverage ?: 0.0
        )
    } ?: emptyList()
}