package com.example.core.data.repositoryImpl.movie

import com.example.core.data.local.LocalMovieDataSource
import com.example.core.data.local.entity.MovieEntity
import com.example.core.data.remote.ApiResponse
import com.example.core.data.remote.NetworkBoundResource
import com.example.core.data.remote.Resource
import com.example.core.data.remote.api.movie.MovieApiService
import com.example.core.data.remote.responses.movie.MovieDto
import com.example.core.domain.model.movie.Movie
import com.example.core.domain.repository.movie.IMovieRepository
import com.example.core.mapper.ApiMapper
import com.example.core.mapper.DatabaseMapper
import com.example.core.utils.MovieCategory
import com.example.core.utils.Response
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

class MovieRepositoryImpl(
    private val movieApiService: MovieApiService,
    private val apiMapper: ApiMapper<List<Movie>, MovieDto>,
    private val localMovieDataSource: LocalMovieDataSource
) : IMovieRepository {
    override fun fetchDiscoverMovie() =
        fetchMovieByCategory(
            category = MovieCategory.DISCOVER,
            apiCall = { movieApiService.fetchDiscoverMovie() }
        )

    override fun fetchTrendingMovie() =
        fetchMovieByCategory(
            category = MovieCategory.TRENDING,
            apiCall = { movieApiService.fetchTrendingMovie() }
        )

    override fun fetchTopRatedMovie(): Flow<Response<List<Movie>>> =
        fetchMovieByCategory(
            category = MovieCategory.TOP_RATED,
            apiCall = { movieApiService.fetchTopRatedMovie() }
        )

    override fun fetchUpcomingMovie() =
        fetchMovieByCategory(
            category = MovieCategory.UPCOMING,
            apiCall = { movieApiService.fetchUpcomingMovie() }
        )

    override fun fetchSearchMovie(query: String): Flow<Response<List<Movie>>> = flow {
        emit(Response.Loading())
        val movieDto = movieApiService.fetchSearchMovie(query)
        apiMapper.mapToDomain(movieDto).apply {
            emit(Response.Success(this))
        }
    }


//    override fun fetchDiscoverMovie(): Flow<Response<List<Movie>>> = flow {
//        emit(Response.Loading())
//        val movieDto = movieApiService.fetchDiscoverMovie()
//        apiMapper.mapToDomain(movieDto).apply {
//            emit(Response.Success(this))
//        }
//    }.catch { e ->
//        emit(Response.Error(e))
//    }
//
//    override fun fetchTrendingMovie(): Flow<Response<List<Movie>>> = flow {
//        emit(Response.Loading())
//        val movieDto = movieApiService.fetchTrendingMovie()
//        apiMapper.mapToDomain(movieDto).apply {
//            emit(Response.Success(this))
//        }
//    }.catch { e ->
//        emit(Response.Error(e))
//    }
//
//    override fun fetchTopRatedMovie(): Flow<Response<List<Movie>>> = flow {
//        emit(Response.Loading())
//        val movieDto = movieApiService.fetchTopRatedMovie()
//        apiMapper.mapToDomain(movieDto).apply {
//            emit(Response.Success(this))
//        }
//    }.catch { e ->
//        emit(Response.Error(e))
//    }
//
//    override fun fetchUpcomingMovie(): Flow<Response<List<Movie>>> = flow {
//        emit(Response.Loading())
//        val movieDto = movieApiService.fetchUpcomingMovie()
//        apiMapper.mapToDomain(movieDto).apply {
//            emit(Response.Success(this))
//        }
//    }.catch { e ->
//        emit(Response.Error(e))
//    }
//

//    }.catch { e ->
//        emit(Response.Error(e))
//    }

    override fun getFavoriteMovie(): Flow<Response<List<Movie>>> = flow {
        emit(Response.Loading())

        localMovieDataSource.getFavoriteMovie().collect { entities ->
            val domainList = DatabaseMapper.mapMovieEntitiesToDomain(entities)
            emit(Response.Success(domainList))
        }
    }.catch { e ->
        emit(Response.Error(e))
    }

    override fun getFavoriteMovieById(id: Int): Flow<Response<Movie?>> {

        return localMovieDataSource
            .getFavoriteMovieById(id)
            .map { entity: MovieEntity? ->

                val movie = entity?.let {
                    DatabaseMapper.mapMovieEntityToDomain(it)
                }

                Response.Success(movie) as Response<Movie?>
            }
            .onStart {
                emit(Response.Loading())
            }
            .catch { e ->
                emit(Response.Error(e))
            }
    }

    override suspend fun setFavoriteMovie(
        movie: Movie,
        state: Boolean
    ) {
        localMovieDataSource.setFavoriteMovie(
            movieId = movie.id,
            newState = state
        )
    }

    private fun fetchMovieByCategory(
        category: String,
        apiCall: suspend () -> MovieDto
    ): Flow<Response<List<Movie>>> {

        return object : NetworkBoundResource<List<Movie>, MovieDto>() {

            override fun loadFromDB(): Flow<List<Movie>> {
                return localMovieDataSource
                    .getMoviesByCategory(category)
                    .map { DatabaseMapper.mapMovieEntitiesToDomain(it) }
            }

            override fun shouldFetch(data: List<Movie>?): Boolean {
                return data.isNullOrEmpty()
            }

            override suspend fun createCall() = flow<ApiResponse<MovieDto>> {
                emit(ApiResponse.Success(apiCall()))
            }.catch { e ->
                emit(ApiResponse.Error(e.message ?: "Unknown Error"))
            }

            override suspend fun saveCallResult(data: MovieDto) {

                val entities = data.results?.map { dto ->

                    val existingMovie =
                        localMovieDataSource.getMovieById(dto?.id!!)

                    MovieEntity(
                        id = dto.id ?: 0,
                        backdropPath = dto.backdropPath.orEmpty(),
                        genreIds = dto.genreIds
                            ?.filterNotNull()
                            ?: emptyList(),
                        originalLanguage = dto.originalLanguage.orEmpty(),
                        originalTitle = dto.originalTitle.orEmpty(),
                        overview = dto.overview.orEmpty(),
                        popularity = dto.popularity ?: 0.0,
                        posterPath = dto.posterPath.orEmpty(),
                        releaseDate = dto.releaseDate.orEmpty(),
                        title = dto.title.orEmpty(),
                        voteAverage = dto.voteAverage ?: 0.0,
                        voteAccount = dto.voteCount ?: 0,
                        video = dto.video ?: false,

                        category = category,

                        isFavorite = existingMovie?.isFavorite ?: false,

                        updatedAt = existingMovie?.updatedAt
                            ?: System.currentTimeMillis()
                    )
                } ?: emptyList()


                localMovieDataSource.insertMovie(entities)
            }

        }.asFlow().map { resource ->

            when(resource) {
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

