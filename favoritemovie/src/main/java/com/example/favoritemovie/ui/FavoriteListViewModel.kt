package com.example.favoritemovie.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.core.domain.model.movie.Movie
import com.example.core.domain.model.tv.Tv
import com.example.core.domain.usecase.movie.MovieUseCase
import com.example.core.domain.usecase.tv.TvUseCase
import com.example.core.utils.collectAndHandle
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class FavoriteListViewModel(
    private val useCase: MovieUseCase,
    private val useCaseTwo: TvUseCase,
) : ViewModel() {

    private val _favoriteState = MutableStateFlow(FavoriteMovieState())
    val favoriteState = _favoriteState.asStateFlow()

    init {
        getAllFavoriteMovies()
        getAllFavoriteTvs()
    }


    fun onFavoriteToggle(movie: Movie, newState: Boolean) = viewModelScope.launch {
        useCase.setFavoriteMovie(movie, newState)
    }

    private fun getAllFavoriteMovies() = viewModelScope.launch {
        useCase.getFavoriteMovie().collectAndHandle(
            onError = { error ->
                _favoriteState.update {
                    it.copy(isLoading = false, error = error?.message)
                }
            },
            onLoading = {
                _favoriteState.update {
                    it.copy(isLoading = true, error = null)
                }
            }
        ) { favMovies ->
            _favoriteState.update {
                it.copy(
                    isLoading = false,
                    error = null,
                    favoriteMovies = favMovies
                )
            }
        }
    }
    private fun getAllFavoriteTvs() = viewModelScope.launch {
        useCaseTwo.getFavoriteTvs().collectAndHandle(
            onError = { error ->
                _favoriteState.update {
                    it.copy(isLoading = false, error = error?.message)
                }
            },
            onLoading = {
                _favoriteState.update {
                    it.copy(isLoading = true, error = null)
                }
            }
        ) { favTvs ->
            _favoriteState.update {
                it.copy(
                    isLoading = false,
                    error = null,
                    favoriteTvs = favTvs
                )
            }
        }
    }
}

class FavoriteViewModelFactory(
    private val useCase: MovieUseCase,
    private val useCaseTwo: TvUseCase
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return FavoriteListViewModel(useCase, useCaseTwo) as T
    }
}


data class FavoriteMovieState(
    val favoriteMovies: List<Movie> = emptyList(),
    val favoriteTvs: List<Tv> = emptyList(),

    val searchMovies: List<Movie> = emptyList(),
    val searchQuery: String = "",

    val error: String? = null,
    val isLoading: Boolean = false

)