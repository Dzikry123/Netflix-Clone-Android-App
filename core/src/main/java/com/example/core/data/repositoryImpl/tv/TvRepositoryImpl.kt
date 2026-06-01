package com.example.core.data.repositoryImpl.tv

import com.example.core.data.local.LocalTvDataSource
import com.example.core.data.local.entity.TvEntity
import com.example.core.data.remote.ApiResponse
import com.example.core.data.remote.NetworkBoundResource
import com.example.core.data.remote.Resource
import com.example.core.data.remote.api.tv.TvApiService
import com.example.core.data.remote.responses.tv.TvDto
import com.example.core.domain.model.tv.Tv
import com.example.core.domain.repository.tv.ITvRepository
import com.example.core.mapper.ApiMapper
import com.example.core.mapper.DatabaseMapper
import com.example.core.utils.Response
import com.example.core.utils.TvCategory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

class TvRepositoryImpl(
    private val tvApiService: TvApiService,
    private val apiMapper: ApiMapper<List<Tv>, TvDto>,
    private val localTvDataSource: LocalTvDataSource
) : ITvRepository {
    override fun fetchDiscoverTv(): Flow<Response<List<Tv>>> =
        fetchTvByCategory(
            category = TvCategory.DISCOVER,
            apiCall = { tvApiService.fetchDiscoverTv() }
        )

    override fun fetchTopRatedTv(): Flow<Response<List<Tv>>> =
        fetchTvByCategory(
            category = TvCategory.TOP_RATED,
            apiCall = { tvApiService.fetchTopRatedTv() }
        )

    override fun fetchUpcomingTv(): Flow<Response<List<Tv>>> =
        fetchTvByCategory(
            category = TvCategory.UPCOMING,
            apiCall = { tvApiService.fetchUpcomingTv() }
        )

    override fun fetchSearchTv(query: String): Flow<Response<List<Tv>>> = flow {
        emit(Response.Loading())
        val tvDto = tvApiService.fetchSearchTv(query)
        apiMapper.mapToDomain(tvDto).apply {
            emit(Response.Success(this))
        }
    }.catch { e ->
        emit(Response.Error(e))
    }

    override fun getFavoriteTv(): Flow<Response<List<Tv>>> = flow {
        emit(Response.Loading())

        localTvDataSource.getFavoriteTv().collect { entities ->
            val domainList = DatabaseMapper.mapTvEntitiesToDomain(entities)
            emit(Response.Success(domainList))
        }
    }.catch { e ->
        emit(Response.Error(e))
    }

    override fun getFavoriteTvById(id: Int): Flow<Response<Tv?>> {
        return localTvDataSource
            .getFavoriteTvById(id)
            .map { entity: TvEntity? ->

                val movie = entity?.let {
                    DatabaseMapper.mapTvEntityToDomain(it)
                }

                Response.Success(movie) as Response<Tv?>
            }
            .onStart {
                emit(Response.Loading())
            }
            .catch { e ->
                emit(Response.Error(e))
            }
    }

    override suspend fun setFavoriteTv(
        tv: Tv,
        state: Boolean
    ) {
        localTvDataSource.setFavoriteTv(
            tv= tv,
            newState = state
        )
    }

    private fun fetchTvByCategory(
        category: String,
        apiCall: suspend () -> TvDto
    ): Flow<Response<List<Tv>>> {

        return object : NetworkBoundResource<List<Tv>, TvDto>() {

            override fun loadFromDB(): Flow<List<Tv>> {
                return localTvDataSource
                    .getTvsByCategory(category)
                    .map { DatabaseMapper.mapTvEntitiesToDomain(it) }
            }

            override fun shouldFetch(data: List<Tv>?): Boolean {
                return data.isNullOrEmpty()
            }

            override suspend fun createCall() = flow<ApiResponse<TvDto>> {
                emit(ApiResponse.Success(apiCall()))
            }.catch { e ->
                emit(ApiResponse.Error(e.message ?: "Unknown Error"))
            }

            override suspend fun saveCallResult(data: TvDto) {

                val entities = data.resultTvs?.map { dto ->

                    val existingTv =
                        localTvDataSource.getTvById(dto?.id!!)

                    TvEntity(
                        id = dto.id ?: 0,
                        backdropPath = dto.backdropPath.orEmpty(),
                        genreIds = dto.genreIds
                            ?.filterNotNull()
                            ?: emptyList(),
                        originalLanguage = dto.originalLanguage.orEmpty(),
                        overview = dto.overview.orEmpty(),
                        popularity = dto.popularity ?: 0.0,
                        posterPath = dto.posterPath.orEmpty(),
                        name = dto.name.orEmpty(),
                        voteAverage = dto.voteAverage ?: 0.0,
                        firstAirDate = dto.firstAirDate,
                        originCountry = dto.originCountry
                            ?.joinToString(", ")
                            .orEmpty(),
                        originalName = dto.originalName,
                        softcore = dto.softcore,
                        voteCount = dto.voteCount,
                        category = category,

                        isFavorite = existingTv?.isFavorite ?: false,

                        updatedAt = existingTv?.updatedAt
                            ?: System.currentTimeMillis(),

                        )
                } ?: emptyList()


                localTvDataSource.insertTv(entities)
            }

        }.asFlow().map { resource ->

            when (resource) {
                is Resource.Success ->
                    Response.Success(resource.data ?: emptyList())

                is Resource.Loading ->
                    Response.Loading()

                is Resource.Error ->
                    Response.Error(Throwable(resource.message))
            }

        }
    }
}