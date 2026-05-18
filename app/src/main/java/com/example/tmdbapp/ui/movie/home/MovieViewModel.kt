package com.example.tmdbapp.ui.movie.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.domain.model.movie.Movie
import com.example.core.domain.usecase.movie.MovieUseCase
import com.example.core.utils.collectAndHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieViewModel @Inject constructor(
    private val useCase: MovieUseCase
) : ViewModel() {
    private val _movieState = MutableStateFlow(MovieState())
    val movieState = _movieState.asStateFlow()

    init {
        fetchDiscoverMovie()
        fetchTrendingMovie()
        fetchTopRatedMovie()
        fetchUpcomingMovie()
    }

    fun onFavoriteToggle(movie: Movie, newState: Boolean) = viewModelScope.launch {
        useCase.setFavoriteMovie(movie, newState)
    }

    private fun fetchDiscoverMovie() = viewModelScope.launch {
        useCase.fetchDiscoverMovie().collectAndHandle(
            onError = { error ->
                _movieState.update {
                    it.copy(isLoading = false, error = error?.message)
                }
            },
            onLoading = {
                _movieState.update {
                    it.copy(isLoading = true, error = null)
                }
            }
        ) { movies ->
            _movieState.update {
                it.copy(isLoading = false, error = null, discoverMovies = movies)
            }
        }
    }
    private fun fetchTrendingMovie() = viewModelScope.launch {
        useCase.fetchTrendingMovie().collectAndHandle(
            onError = { error ->
                _movieState.update {
                    it.copy(isLoading = false, error = error?.message)
                }
            },
            onLoading = {
                _movieState.update {
                    it.copy(isLoading = true, error = null)
                }
            }
        ) { movies ->
            _movieState.update {
                it.copy(isLoading = false, error = null, trendingMovies = movies)
            }
        }
    }
    private fun fetchTopRatedMovie() = viewModelScope.launch {
        useCase.fetchTopRatedMovie().collectAndHandle(
            onError = { error ->
                _movieState.update {
                    it.copy(isLoading = false, error = error?.message)
                }
            },
            onLoading = {
                _movieState.update {
                    it.copy(isLoading = true, error = null)
                }
            }
        ) { movies ->
            _movieState.update {
                it.copy(isLoading = false, error = null, topRatedMovies = movies)
            }
        }
    }
    private fun fetchUpcomingMovie() = viewModelScope.launch {
        useCase.fetchUpcomingMovie().collectAndHandle(
            onError = { error ->
                _movieState.update {
                    it.copy(isLoading = false, error = error?.message)
                }
            },
            onLoading = {
                _movieState.update {
                    it.copy(isLoading = true, error = null)
                }
            }
        ) { movies ->
            _movieState.update {
                it.copy(isLoading = false, error = null, upcomingMovies = movies)
            }
        }
    }
    fun searchMovie(query: String) = viewModelScope.launch {
        if (query.isBlank()) return@launch
        useCase.fetchSearchMovie(query).collectAndHandle(
            onError = { error ->
                _movieState.update {
                    it.copy(isLoading = false, error = error?.message)
                }
            },
            onLoading = {
                _movieState.update {
                    it.copy(
                        isLoading = true,
                        error = null,
                    )
                }
            }
        ) { movies ->
            _movieState.update {
                it.copy(
                    isLoading = false,
                    error = null,
                    searchMovies = movies,
                    searchQuery = query
                )
            }
        }
    }
}

data class MovieState(
    val discoverMovies: List<Movie> = emptyList(),
    val trendingMovies: List<Movie> = emptyList(),
    val topRatedMovies: List<Movie> = emptyList(),
    val upcomingMovies: List<Movie> = emptyList(),

    val searchMovies: List<Movie> = emptyList(),
    val searchQuery: String = "",

    val error: String? = null,
    val isLoading: Boolean = false

)