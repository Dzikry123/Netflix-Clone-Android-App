package com.example.core.domain.usecase.tv

import com.example.core.domain.model.tv.Tv
import com.example.core.domain.repository.tv.ITvRepository
import com.example.core.utils.Response
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TvInteractor @Inject constructor(private val tvRepository: ITvRepository): TvUseCase {
    override fun fetchDiscoverTv(): Flow<Response<List<Tv>>> {
        return tvRepository.fetchDiscoverTv()
    }

    override fun fetchTopRatedTv(): Flow<Response<List<Tv>>> {
        return tvRepository.fetchTopRatedTv()
    }

    override fun fetchUpcomingTv(): Flow<Response<List<Tv>>> {
        return tvRepository.fetchUpcomingTv()
    }

    override fun fetchSearchTv(query: String): Flow<Response<List<Tv>>> {
        return tvRepository.fetchSearchTv(query)
    }

    override fun getFavoriteTvs(): Flow<Response<List<Tv>>> {
        return tvRepository.getFavoriteTv()
    }

    override fun getFavoriteTvById(id: Int): Flow<Response<Tv?>> {
        return tvRepository.getFavoriteTvById(id)
    }

    override suspend fun setFavoriteTv(
        movie: Tv,
        state: Boolean
    ) {
        return tvRepository.setFavoriteTv(movie, state)
    }

}