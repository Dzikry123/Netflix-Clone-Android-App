package com.example.core.domain.repository.tv

import com.example.core.domain.model.tv.Tv
import com.example.core.domain.model.tv.TvDetail
import com.example.core.utils.Response
import kotlinx.coroutines.flow.Flow

interface ITvDetailRepository {
    fun fetchTvDetail(tvId: Int): Flow<Response<TvDetail>>
    fun fetchTv(): Flow<Response<List<Tv>>>
    fun getTvDetailById(id: Int): Flow<Response<Tv?>>
    suspend fun setFavoriteMovie(tv: Tv, state: Boolean)
}