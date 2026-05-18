package com.example.core.domain.usecase.movie.detail

import com.example.core.domain.model.movie.Movie
import com.example.core.domain.model.movie.MovieDetail
import com.example.core.domain.repository.movie.IMovieDetailRepository
import com.example.core.utils.Response
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MovieDetailInteractor @Inject constructor(private val repository: IMovieDetailRepository): MovieDetailUseCase {
    override fun fetchMovieDetail(movieId: Int): Flow<Response<MovieDetail>> {
        return repository.fetchMovieDetail(movieId)
    }

    override fun fetchMovie(): Flow<Response<List<Movie>>> {
        return repository.fetchMovie()
    }

    override fun getMovieDetailById(id: Int): Flow<Response<Movie?>> {
        return repository.getMovieDetailById(id)
    }

    override suspend fun setFavoriteMovie(
        movie: Movie,
        state: Boolean
    ) {
        return repository.setFavoriteMovie(movie, state)
    }
}