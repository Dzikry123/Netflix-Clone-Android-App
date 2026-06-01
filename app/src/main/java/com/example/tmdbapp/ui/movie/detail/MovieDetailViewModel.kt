package com.example.tmdbapp.ui.movie.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.domain.model.movie.Movie
import com.example.core.domain.model.movie.MovieDetail
import com.example.core.domain.usecase.movie.MovieUseCase
import com.example.core.domain.usecase.movie.detail.MovieDetailUseCase
import com.example.core.mapper.DatabaseMapper.toMovie
import com.example.core.utils.K
import com.example.core.utils.collectAndHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieDetailViewModel @Inject constructor(
    private val useCase: MovieDetailUseCase, private val useCaseTwo: MovieUseCase, savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _detailState = MutableStateFlow(MovieDetailState())
    val detailState = _detailState.asStateFlow()
    val movie = savedStateHandle.get<Movie>("movie")
    val id: Int = savedStateHandle.get<Int>(K.MOVIE_ID) ?: -1

    init {
        fetchMovieDetailById()
        observeFavoriteStatus()
    }

    fun onFavoriteToggle(
        movieDetail: MovieDetail,
        newState: Boolean
    ) = viewModelScope.launch {

        useCaseTwo.setFavoriteMovie(
            movieDetail.toMovie(newState),
            newState
        )

        _detailState.update {
            it.copy(
                isFavorite = newState
            )
        }
    }

    fun onMovieFavoriteToggle(
        movie: Movie,
        newState: Boolean
    ) = viewModelScope.launch {

        useCaseTwo.setFavoriteMovie(
            movie.copy(isFavorite = newState),
            newState
        )
    }

    private fun observeFavoriteStatus() = viewModelScope.launch {

        useCaseTwo.getFavoriteMovieById(id).collectAndHandle(
            onError = { e ->
                _detailState.update {
                    it.copy(
                        isLoading = false,
                        error = e?.message
                    )
                }
            },
            onLoading = {
                _detailState.update {
                    it.copy(
                        isLoading = true
                    )
                }
            }
        ) { movie ->

            _detailState.update {
                it.copy(
                    isLoading = false,
                    currentMovie = movie,
                    isFavorite = movie?.isFavorite ?: false
                )
            }
        }
    }

    private fun fetchMovieDetailById() = viewModelScope.launch {
        if (id == -1) {
            _detailState.update {
                it.copy(
                    isLoading = false, error = "Movie not found"
                )
            }
        } else {
            useCase.fetchMovieDetail(id).collectAndHandle(onError = { error ->
                _detailState.update {
                    it.copy(
                        isLoading = false, error = error?.message ?: "Movie not found"
                    )
                }
            }, onLoading = {
                _detailState.update {
                    it.copy(
                        isLoading = true, error = null
                    )
                }
            }) { movieDetail ->
                _detailState.update {
                    it.copy(
                        isLoading = false, error = null, movieDetail = movieDetail
                    )
                }
            }
        }
    }

    fun fetchMovie() = viewModelScope.launch {
        if (id == -1) {
            _detailState.update {
                it.copy(
                    isLoading = false, error = "Movie not found"
                )
            }
        } else {
            useCase.fetchMovie().collectAndHandle(onError = { error ->
                _detailState.update {
                    it.copy(
                        isMovieLoading = false, error = error?.message ?: "Movie not found"
                    )
                }
            }, onLoading = {
                _detailState.update {
                    it.copy(
                        isMovieLoading = true, error = null
                    )
                }
            }) { movies ->
                _detailState.update {
                    it.copy(
                        isMovieLoading = false,
                        error = null,
                        movies = movies
                    )
                }
            }
        }
    }

}

data class MovieDetailState(
    val movieDetail: MovieDetail? = null,
    val currentMovie: Movie? = null,
    val movies: List<Movie> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val isMovieLoading: Boolean = false,
    val isFavorite: Boolean = false,
)