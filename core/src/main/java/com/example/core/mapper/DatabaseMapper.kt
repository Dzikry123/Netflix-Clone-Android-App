package com.example.core.mapper

import com.example.core.data.local.entity.MovieEntity
import com.example.core.data.local.entity.TvEntity
import com.example.core.domain.model.movie.Movie
import com.example.core.domain.model.movie.MovieDetail
import com.example.core.domain.model.tv.Tv

object DatabaseMapper {
    fun mapMovieEntitiesToDomain(entity: List<MovieEntity>): List<Movie> =
        entity.map {
            Movie(
                backdropPath = it.backdropPath,
                genreIds = it.genreIds,
                id = it.id,
                originalLanguage = it.originalLanguage,
                originalTitle = it.originalTitle,
                overview = it.overview,
                popularity = it.popularity,
                posterPath = it.posterPath,
                releaseDate = it.releaseDate,
                title = it.title,
                voteAverage = it.voteAverage,
                voteAccount = it.voteAccount,
                video = it.video,
                isFavorite = it.isFavorite,
            )
        }

    fun mapTvEntitiesToDomain(entity: List<TvEntity>): List<Tv> =
        entity.map {
            Tv(
                backdropPath = it.backdropPath,
                genreIds = it.genreIds,
                id = it.id,
                originalLanguage = it.originalLanguage,
                overview = it.overview,
                popularity = it.popularity,
                posterPath = it.posterPath,
                name = it.name,
                voteAverage = it.voteAverage,
                isFavorite = it.isFavorite,
                firstAirDate = it.firstAirDate,
                originCountry = it.originCountry,
                originalName = it.originalName,
                softcore = it.softcore,
                voteCount = it.voteCount,
            )
        }

    fun mapMovieEntityToDomain(entity: MovieEntity): Movie {
        return Movie(
            backdropPath = entity.backdropPath,
            genreIds = entity.genreIds,
            id = entity.id,
            originalLanguage = entity.originalLanguage,
            originalTitle = entity.originalTitle,
            overview = entity.overview,
            popularity = entity.popularity,
            posterPath = entity.posterPath,
            releaseDate = entity.releaseDate,
            title = entity.title,
            voteAverage = entity.voteAverage,
            voteAccount = entity.voteAccount,
            video = entity.video,
            isFavorite = entity.isFavorite
        )
    }

    fun mapTvEntityToDomain(entity: TvEntity): Tv {
        return Tv(
            backdropPath = entity.backdropPath,
            genreIds = entity.genreIds,
            id = entity.id,
            originalLanguage = entity.originalLanguage,
            originalName = entity.originalName,
            overview = entity.overview,
            popularity = entity.popularity,
            posterPath = entity.posterPath,
            name = entity.name,
            voteAverage = entity.voteAverage,
            voteCount = entity.voteCount,
            isFavorite = entity.isFavorite,
            firstAirDate = entity.firstAirDate,
            originCountry = entity.originCountry,
            softcore = entity.softcore,
        )
    }

    fun MovieDetail.toMovie(isFavorite: Boolean): Movie {
        return Movie(
            id = id,
            title = title,
            posterPath = posterPath,
            backdropPath = backdropPath,
            overview = overview,
            releaseDate = releaseDate,
            voteAverage = voteAverage,
            genreIds = genreIds,
            popularity = popularity,
            originalLanguage = originalLanguage,
            originalTitle = originalTitle,
            voteAccount = voteCount,
            video = video,
            isFavorite = isFavorite
        )
    }

    fun mapDomainToEntity(domain: Movie): MovieEntity =
        MovieEntity(
            backdropPath = domain.backdropPath,
            genreIds = domain.genreIds,
            id = domain.id,
            originalLanguage = domain.originalLanguage,
            originalTitle = domain.originalTitle,
            overview = domain.overview,
            popularity = domain.popularity,
            posterPath = domain.posterPath,
            releaseDate = domain.releaseDate,
            title = domain.title,
            voteAverage = domain.voteAverage,
            voteAccount = domain.voteAccount,
            video = domain.video,
            isFavorite = domain.isFavorite,

            category = "",

            updatedAt = System.currentTimeMillis()
        )
}