package com.example.core.data.repositoryImpl.movie

import com.example.core.data.local.LocalMovieDataSource
import com.example.core.data.remote.api.movie.MovieDetailApiService
import com.example.core.data.remote.responses.movie.MovieDto
import com.example.core.data.remote.responses.movie.movie_detail.MovieDetailDto
import com.example.core.domain.model.movie.Movie
import com.example.core.domain.model.movie.MovieDetail
import com.example.core.domain.repository.movie.IMovieDetailRepository
import com.example.core.mapper.ApiMapper
import com.example.core.mapper.DatabaseMapper
import com.example.core.utils.Response
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

class MovieDetailRepositoryImpl(
    private val movieDetailApiService: MovieDetailApiService,
    private val apiDetailMapper: ApiMapper<MovieDetail, MovieDetailDto>,
    private val apiMovieMapper: ApiMapper<List<Movie>, MovieDto>,
    private val localMovieDataSource: LocalMovieDataSource

): IMovieDetailRepository {
    override fun fetchMovieDetail(movieId: Int): Flow<Response<MovieDetail>> = flow {
        emit(Response.Loading())
        val movieDetailDto = movieDetailApiService.fetchMovieDetail(movieId)
        apiDetailMapper.mapToDomain(movieDetailDto).apply {
            emit(Response.Success(this))
        }
    }

    override fun fetchMovie(): Flow<Response<List<Movie>>> = flow {
        emit(Response.Loading())
        val movieDto = movieDetailApiService.fetchMovie()
        apiMovieMapper.mapToDomain(movieDto).apply {
            emit(Response.Success(this))
        }
    }

    override fun getMovieDetailById(id: Int): Flow<Response<Movie?>> = flow {

        emit(Response.Loading())

        localMovieDataSource.getFavoriteMovieById(id).collect { entity ->

            val movie = entity?.let {
                DatabaseMapper.mapMovieEntityToDomain(it)
            }

            emit(Response.Success(movie))
        }

    }.catch { e ->
        emit(Response.Error(e))
    }

    override suspend fun setFavoriteMovie(
        movie: Movie,
        state: Boolean
    ) {
        localMovieDataSource.setFavoriteMovie(
            movieId = movie.id,
            newState = state
        )
    }
}