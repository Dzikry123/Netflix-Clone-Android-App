package com.example.core.domain.usecase.movie

import com.example.core.domain.model.movie.Movie
import com.example.core.utils.Response
import kotlinx.coroutines.flow.Flow

interface MovieUseCase {
    fun fetchDiscoverMovie(): Flow<Response<List<Movie>>>
    fun fetchTrendingMovie(): Flow<Response<List<Movie>>>
    fun fetchTopRatedMovie(): Flow<Response<List<Movie>>>
    fun fetchUpcomingMovie(): Flow<Response<List<Movie>>>
    fun fetchSearchMovie(query: String): Flow<Response<List<Movie>>>
    fun getFavoriteMovie(): Flow<Response<List<Movie>>>
    fun getFavoriteMovieById(id: Int): Flow<Response<Movie?>>
    suspend fun setFavoriteMovie(movie: Movie, state: Boolean)
}