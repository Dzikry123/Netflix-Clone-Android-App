package com.example.core.di.tv

import com.example.core.data.local.LocalTvDataSource
import com.example.core.data.remote.api.tv.TvApiService
import com.example.core.data.remote.responses.tv.TvDto
import com.example.core.data.repositoryImpl.tv.TvRepositoryImpl
import com.example.core.domain.model.tv.Tv
import com.example.core.domain.repository.tv.ITvRepository
import com.example.core.mapper.ApiMapper
import com.example.core.mapper.tv.TvApiMapperImpl
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
object TvModule {

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
    fun provideTvRepository(
        tvApiService: TvApiService,
        mapper: ApiMapper<List<Tv>, TvDto>,
        localTvDataSource: LocalTvDataSource
    ): ITvRepository = TvRepositoryImpl(
        tvApiService, mapper, localTvDataSource
    )

    @Provides
    @Singleton
    fun provideTvMapper(): ApiMapper<List<Tv>, TvDto> = TvApiMapperImpl()

    @Provides
    @Singleton
    fun provideTvApiService(): TvApiService {
        val contentType = "application/json".toMediaType()
        return Retrofit.Builder()
            .baseUrl(K.BASE_URL)
            .addConverterFactory(json.asConverterFactory(contentType))
            .client(client)
            .build()
            .create(TvApiService::class.java)
    }
}