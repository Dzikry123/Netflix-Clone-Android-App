package com.example.tmdbapp.ui.tv.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.domain.model.tv.Tv
import com.example.core.domain.usecase.tv.TvUseCase
import com.example.core.utils.collectAndHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TvViewModel @Inject constructor(
    private val useCase: TvUseCase
) : ViewModel() {
    private val _tvState = MutableStateFlow(TvState())
    val tvState = _tvState.asStateFlow()

    init {
        fetchDiscoverTv()
        fetchTopRatedTv()
        fetchUpcomingTv()
    }

    fun onFavoriteToggle(tv: Tv, newState: Boolean) = viewModelScope.launch {
        useCase.setFavoriteTv(tv, newState)
    }

    private fun fetchDiscoverTv() = viewModelScope.launch {
        useCase.fetchDiscoverTv().collectAndHandle(onError = { error ->
            _tvState.update {
                it.copy(isLoading = false, error = error?.message)
            }
        }, onLoading = {
            _tvState.update {
                it.copy(isLoading = true, error = null)
            }
        }) { tvs ->
            _tvState.update {
                it.copy(isLoading = false, error = null, discoverTvs = tvs)
            }
        }
    }

    private fun fetchTopRatedTv() = viewModelScope.launch {
        useCase.fetchTopRatedTv().collectAndHandle(onError = { error ->
            _tvState.update {
                it.copy(isLoading = false, error = error?.message)
            }
        }, onLoading = {
            _tvState.update {
                it.copy(isLoading = true, error = null)
            }
        }) { tvs ->
            _tvState.update {
                it.copy(isLoading = false, error = null, topRatedTvs = tvs)
            }
        }
    }

    private fun fetchUpcomingTv() = viewModelScope.launch {
        useCase.fetchUpcomingTv().collectAndHandle(onError = { error ->
            _tvState.update {
                it.copy(isLoading = false, error = error?.message)
            }
        }, onLoading = {
            _tvState.update {
                it.copy(isLoading = false, error = null)
            }
        }) { tvs ->
            _tvState.update {
                it.copy(isLoading = false, error = null, upcomingTvs = tvs)
            }
        }
    }

    fun searchTv(query: String) = viewModelScope.launch {
        useCase.fetchSearchTv(query).collectAndHandle(onError = { error ->
            _tvState.update {
                it.copy(isLoading = false, error = error?.message)
            }
        }, onLoading = {
            _tvState.update {
                it.copy(isLoading = true, error = null)
            }
        }) { tvs ->
            _tvState.update {
                it.copy(
                    isLoading = false,
                    error = null,
                    searchTv = tvs,
                    searchQuery = query,
                )
            }
        }
    }

}

data class TvState(
    val discoverTvs: List<Tv> = emptyList(),
    val topRatedTvs: List<Tv> = emptyList(),
    val upcomingTvs: List<Tv> = emptyList(),

    val searchTv: List<Tv> = emptyList(),
    val searchQuery: String = "",

    val error: String? = null,
    val isLoading: Boolean = false

)