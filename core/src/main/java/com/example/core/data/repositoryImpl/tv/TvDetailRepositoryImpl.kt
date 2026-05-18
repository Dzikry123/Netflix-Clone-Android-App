package com.example.core.data.repositoryImpl.tv

import android.util.Log
import com.example.core.data.local.LocalTvDataSource
import com.example.core.data.remote.api.tv.TvDetailApiService
import com.example.core.data.remote.responses.tv.TvDto
import com.example.core.data.remote.responses.tv.tv_detail.TvDetailDto
import com.example.core.domain.model.tv.Tv
import com.example.core.domain.model.tv.TvDetail
import com.example.core.domain.repository.tv.ITvDetailRepository
import com.example.core.mapper.ApiMapper
import com.example.core.mapper.DatabaseMapper
import com.example.core.utils.Response
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

class TvDetailRepositoryImpl(
    private val tvDetailApiService: TvDetailApiService,
    private val apiDetailMapper: ApiMapper<TvDetail, TvDetailDto>,
    private val apiMapper: ApiMapper<List<Tv>, TvDto>,
    private val localTvDataSource: LocalTvDataSource
): ITvDetailRepository {
    override fun fetchTvDetail(tvId: Int): Flow<Response<TvDetail>> = flow{
        Log.d("TV_DETAIL", "REPOSITORY CALLED ID = $tvId")
        emit(Response.Loading())
        val tvDetailDto = tvDetailApiService.fetchTvDetail(tvId)
        apiDetailMapper.mapToDomain(tvDetailDto).apply {
            emit(Response.Success(this))
        }
    }.catch { e ->
        Log.d("TV_DETAIL", "ERROR = ${e.message}")
        emit(Response.Error(e))
    }

    override fun fetchTv(): Flow<Response<List<Tv>>> = flow{
        emit(Response.Loading())
        val tvDto = tvDetailApiService.fetchTv()
        apiMapper.mapToDomain(tvDto).apply {
            emit(Response.Success(this))
        }
    }.catch { e ->
        emit(Response.Error(e))
    }

    override fun getTvDetailById(id: Int): Flow<Response<Tv?>> = flow {

        emit(Response.Loading())

        localTvDataSource.getFavoriteTvById(id).collect { entity ->

            val movie = entity?.let {
                DatabaseMapper.mapTvEntityToDomain(it)
            }

            emit(Response.Success(movie))
        }

    }.catch { e ->
        emit(Response.Error(e))
    }

    override suspend fun setFavoriteMovie(
        tv: Tv,
        state: Boolean
    ) {
        localTvDataSource.setFavoriteTv(
            tvId = tv.id,
            newState = state
        )
    }

}