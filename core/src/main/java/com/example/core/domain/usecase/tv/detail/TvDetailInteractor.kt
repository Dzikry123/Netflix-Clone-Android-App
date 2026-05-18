package com.example.core.domain.usecase.tv.detail

import com.example.core.domain.model.tv.Tv
import com.example.core.domain.model.tv.TvDetail
import com.example.core.domain.repository.tv.ITvDetailRepository
import com.example.core.utils.Response
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TvDetailInteractor @Inject constructor(private val repository: ITvDetailRepository): TvDetailUseCase {
    override fun fetchTvDetail(tvId: Int): Flow<Response<TvDetail>> {
        return repository.fetchTvDetail(tvId)
    }

    override fun fetchTv(): Flow<Response<List<Tv>>> {
        return repository.fetchTv()
    }

    override fun getTvDetailById(id: Int): Flow<Response<Tv?>> {
        return repository.getTvDetailById(id)
    }

    override suspend fun setFavoriteTv(
        tv: Tv,
        state: Boolean
    ) {
        return repository.setFavoriteMovie(tv, state)
    }

}