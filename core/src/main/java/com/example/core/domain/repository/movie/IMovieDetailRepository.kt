package com.example.core.domain.repository.movie

import com.example.core.domain.model.movie.Movie
import com.example.core.domain.model.movie.MovieDetail
import com.example.core.utils.Response
import kotlinx.coroutines.flow.Flow

interface IMovieDetailRepository {
    fun fetchMovieDetail(movieId: Int): Flow<Response<MovieDetail>>
    fun fetchMovie(): Flow<Response<List<Movie>>>
    fun getMovieDetailById(id: Int): Flow<Response<Movie?>>
    suspend fun setFavoriteMovie(movie: Movie, state: Boolean)
}