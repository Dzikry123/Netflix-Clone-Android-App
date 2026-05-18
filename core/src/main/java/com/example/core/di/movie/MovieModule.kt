package com.example.core.di.movie

import com.example.core.data.local.LocalMovieDataSource
import com.example.core.data.remote.api.movie.MovieApiService
import com.example.core.data.remote.responses.movie.MovieDto
import com.example.core.data.repositoryImpl.movie.MovieRepositoryImpl
import com.example.core.domain.model.movie.Movie
import com.example.core.domain.repository.movie.IMovieRepository
import com.example.core.mapper.ApiMapper
import com.example.core.mapper.movie.MovieApiMapperImpl
import com.example.core.utils.K
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MovieModule {

    private val json = Json {
        coerceInputValues = true
        ignoreUnknownKeys = true
    }

    private val loggingInterceptor =
        HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

    private val client = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    @Provides
    @Singleton
    fun provideMovieRepository(
        movieApiService: MovieApiService,
        mapper: ApiMapper<List<Movie>, MovieDto>,
        localMovieDataSource: LocalMovieDataSource
    ): IMovieRepository = MovieRepositoryImpl(
        movieApiService, mapper,  localMovieDataSource
    )

    @Provides
    @Singleton
    fun provideMovieMapper(): ApiMapper<List<Movie>, MovieDto> = MovieApiMapperImpl()

    @Provides
    @Singleton
    fun provideMovieApiService(): MovieApiService {
        val contentType = "application/json".toMediaType()
        return Retrofit.Builder()
            .baseUrl(K.BASE_URL)
            .addConverterFactory(json.asConverterFactory(contentType))
            .client(client)
            .build()
            .create(MovieApiService::class.java)
    }

}