package com.example.core.di.movie

import com.example.core.data.local.LocalMovieDataSource
import com.example.core.data.remote.api.movie.MovieDetailApiService
import com.example.core.data.remote.responses.movie.MovieDto
import com.example.core.data.remote.responses.movie.movie_detail.MovieDetailDto
import com.example.core.data.repositoryImpl.movie.MovieDetailRepositoryImpl
import com.example.core.domain.model.movie.Movie
import com.example.core.domain.model.movie.MovieDetail
import com.example.core.domain.repository.movie.IMovieDetailRepository
import com.example.core.mapper.ApiMapper
import com.example.core.mapper.movie.MovieDetailMapperImp
import com.example.core.utils.K
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MovieDetailModule {
    private val json = Json {
        coerceInputValues = true
        ignoreUnknownKeys = true
    }

    @Provides
    @Singleton
    fun provideMovieDetailRepository(
        movieDetailApiService: MovieDetailApiService,
        mapper: ApiMapper<MovieDetail, MovieDetailDto>,
        movieMapper: ApiMapper<List<Movie>, MovieDto>,
        localMovieDataSource: LocalMovieDataSource
    ): IMovieDetailRepository = MovieDetailRepositoryImpl(
        movieDetailApiService = movieDetailApiService,
        apiDetailMapper = mapper,
        apiMovieMapper = movieMapper,
        localMovieDataSource = localMovieDataSource
    )

    @Provides
    @Singleton
    fun provideMovieMapper(): ApiMapper<MovieDetail, MovieDetailDto> = MovieDetailMapperImp()


    @Provides
    @Singleton
    fun provideMovieDetailApiService(): MovieDetailApiService {
        val contentType = "application/json". toMediaType()
        return Retrofit.Builder()
            .baseUrl(K.BASE_URL)
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()
            .create(MovieDetailApiService:: class.java)
    }
}