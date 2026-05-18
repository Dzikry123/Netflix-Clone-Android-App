package com.example.core.domain.usecase.tv.detail

import com.example.core.domain.model.tv.Tv
import com.example.core.domain.model.tv.TvDetail
import com.example.core.utils.Response
import kotlinx.coroutines.flow.Flow

interface TvDetailUseCase {
    fun fetchTvDetail(tvId: Int): Flow<Response<TvDetail>>
    fun fetchTv(): Flow<Response<List<Tv>>>
    fun getTvDetailById(id: Int): Flow<Response<Tv?>>
    suspend fun setFavoriteTv(tv: Tv, state: Boolean)
}