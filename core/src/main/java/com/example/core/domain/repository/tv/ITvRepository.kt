package com.example.core.domain.repository.tv

import com.example.core.domain.model.tv.Tv
import com.example.core.utils.Response
import kotlinx.coroutines.flow.Flow

interface ITvRepository {
    fun fetchDiscoverTv(): Flow<Response<List<Tv>>>
    fun fetchTopRatedTv(): Flow<Response<List<Tv>>>
    fun fetchUpcomingTv(): Flow<Response<List<Tv>>>
    fun fetchSearchTv(query: String): Flow<Response<List<Tv>>>
    fun getFavoriteTv(): Flow<Response<List<Tv>>>
    fun getFavoriteTvById(id: Int): Flow<Response<Tv?>>
    suspend fun setFavoriteTv(tv: Tv, state: Boolean)
}