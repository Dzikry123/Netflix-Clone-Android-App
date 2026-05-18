package com.example.core.data.local

import com.example.core.data.local.entity.MovieEntity
import com.example.core.data.local.room.MovieDao
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

    suspend fun setFavoriteMovie(movieId: Int, newState: Boolean) {

        val movie = movieDao.getMovieById(movieId)
            ?: return

        movie.isFavorite = newState
        movie.updatedAt = System.currentTimeMillis()

        movieDao.updateFavoriteMovie(movie)
    }
}