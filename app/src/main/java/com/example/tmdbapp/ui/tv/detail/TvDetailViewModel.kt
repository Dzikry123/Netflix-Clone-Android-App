package com.example.tmdbapp.ui.tv.detail

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.domain.model.tv.Tv
import com.example.core.domain.model.tv.TvDetail
import com.example.core.domain.usecase.tv.TvUseCase
import com.example.core.domain.usecase.tv.detail.TvDetailUseCase
import com.example.core.utils.K
import com.example.core.utils.collectAndHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TvDetailViewModel @Inject constructor(
    private val useCase: TvDetailUseCase,
    private val useCaseTwo: TvUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _detailState = MutableStateFlow(TvDetailState())
    val detailState = _detailState.asStateFlow()
    val tv = savedStateHandle.get<Tv>("tv")
    val id: Int = savedStateHandle.get<Int>(K.TV_ID) ?: -1

    init {
        Log.d("TV_DETAIL", "FETCHING ID = $id")
        fetchDetailTv(id)
        observeFavoriteStatus()
    }

    fun onFavoriteToggle(newState: Boolean) = viewModelScope.launch {

        val currentMovie = detailState.value.currentTv ?: return@launch

        useCaseTwo.setFavoriteTv(
            currentMovie.copy(isFavorite = newState),
            newState
        )

        _detailState.update {
            it.copy(
                isFavorite = newState,
                currentTv = currentMovie.copy(
                    isFavorite = newState
                )
            )
        }
    }

    private fun observeFavoriteStatus() = viewModelScope.launch {

        useCaseTwo.getFavoriteTvById(id).collectAndHandle(
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
                    currentTv = movie,
                    isFavorite = movie?.isFavorite ?: false
                )
            }
        }
    }

    private fun fetchDetailTv(id: Int) = viewModelScope.launch {
        Log.d("TV_DETAIL", "FUNCTION CALLED")
        if (id == -1) {
            _detailState.update {
                it.copy(isLoading = false, error = "Tv Series Not Found")
            }
        } else {
            useCase.fetchTvDetail(id).collectAndHandle(
                onError = { e ->
                    _detailState.update {
                        it.copy(isLoading = false, error = e?.message)
                    }
                },
                onLoading = {
                    _detailState.update {
                        it.copy(isLoading = true, error = null)
                    }
                }
            ) { tvDetail ->
                _detailState.update {
                    it.copy(isLoading = false, error = null, tvDetail = tvDetail)
                }
            }
        }
    }

    fun fetchTv() = viewModelScope.launch {
        if (id == -1) {
            _detailState.update {
                it.copy(
                    isLoading = false,
                    error = "Cannot get the similiar series because ID Not Found"
                )
            }
        } else {
            useCase.fetchTv().collectAndHandle(
                onError = { e ->
                    _detailState.update {
                        it.copy(isTvLoading = false, error = e?.message)
                    }
                },
                onLoading = {
                    _detailState.update {
                        it.copy(isTvLoading = true, error = null)
                    }
                }
            ) { tvs ->
                _detailState.update {
                    it.copy(isTvLoading = false, error = null, tv = tvs)
                }
            }
        }
    }
}

data class TvDetailState(
    val tvDetail: TvDetail? = null,
    val currentTv: Tv? = null,
    val tv: List<Tv> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val isTvLoading: Boolean = false,
    val isFavorite: Boolean = false,
)