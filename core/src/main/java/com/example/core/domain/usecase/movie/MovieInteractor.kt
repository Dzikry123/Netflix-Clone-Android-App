package com.example.core.domain.usecase.movie

import com.example.core.domain.model.movie.Movie
import com.example.core.domain.repository.movie.IMovieRepository
import com.example.core.utils.Response
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MovieInteractor @Inject constructor(private val repository: IMovieRepository) : MovieUseCase {
    override fun fetchDiscoverMovie(): Flow<Response<List<Movie>>> {
        return repository.fetchDiscoverMovie()
    }

    override fun fetchTrendingMovie(): Flow<Response<List<Movie>>> {
        return repository.fetchTrendingMovie()
    }

    override fun fetchTopRatedMovie(): Flow<Response<List<Movie>>> {
        return repository.fetchTopRatedMovie()
    }

    override fun fetchUpcomingMovie(): Flow<Response<List<Movie>>> {
        return repository.fetchUpcomingMovie()
    }

    override fun fetchSearchMovie(query: String): Flow<Response<List<Movie>>> {
        return repository.fetchSearchMovie(query)
    }

    override fun getFavoriteMovie(): Flow<Response<List<Movie>>> {
        return repository.getFavoriteMovie()
    }

    override fun getFavoriteMovieById(id: Int): Flow<Response<Movie?>> {
        return repository.getFavoriteMovieById(id)
    }

    override suspend fun setFavoriteMovie(
        movie: Movie,
        state: Boolean
    ) {
        return repository.setFavoriteMovie(movie, state)
    }

}