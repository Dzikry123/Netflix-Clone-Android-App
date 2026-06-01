package com.example.core.data.local

import com.example.core.data.local.entity.MovieEntity
import com.example.core.data.local.room.MovieDao
import com.example.core.domain.model.movie.Movie
import com.example.core.mapper.DatabaseMapper
import com.example.core.utils.MovieCategory
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalMovieDataSource @Inject constructor(
    private val movieDao: MovieDao
) {

    fun getAllMovie(): Flow<List<MovieEntity>> =
        movieDao.getAllMovie()

    fun getMoviesByCategory(category: String): Flow<List<MovieEntity>> =
        movieDao.getMoviesByCategory(category)

    fun getFavoriteMovie(): Flow<List<MovieEntity>> =
        movieDao.getFavoriteMovie()

    suspend fun getMovieById(id: Int): MovieEntity? =
        movieDao.getMovieById(id)

    fun getFavoriteMovieById(id: Int): Flow<MovieEntity?> =
        movieDao.getFavoriteMovieById(id)

    suspend fun insertMovie(movieList: List<MovieEntity>) =
        movieDao.insertMovie(movieList)

    suspend fun setFavoriteMovie(
        movie: Movie,
        newState: Boolean
    ) {

        val localMovie = movieDao.getMovieById(movie.id)

        if (localMovie == null) {

            movieDao.insertMovie(
                listOf(
                    DatabaseMapper.mapDomainToMovieEntity(
                        movie,
                        MovieCategory.SEARCH
                    )
                )
            )

            return
        }

        localMovie.isFavorite = newState
        localMovie.updatedAt = System.currentTimeMillis()

        movieDao.updateFavoriteMovie(localMovie)
    }
}