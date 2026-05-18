package com.example.core.di.tv

import com.example.core.data.local.LocalTvDataSource
import com.example.core.data.remote.api.tv.TvDetailApiService
import com.example.core.data.remote.responses.tv.TvDto
import com.example.core.data.remote.responses.tv.tv_detail.TvDetailDto
import com.example.core.data.repositoryImpl.tv.TvDetailRepositoryImpl
import com.example.core.domain.model.tv.Tv
import com.example.core.domain.model.tv.TvDetail
import com.example.core.domain.repository.tv.ITvDetailRepository
import com.example.core.mapper.ApiMapper
import com.example.core.mapper.tv.TvDetailMapperImpl
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
object TvDetailModule {

    private val json = Json {
        coerceInputValues = true
        ignoreUnknownKeys = true
    }

    @Provides
    @Singleton
    fun provideTvDetailRepository(
        tvDetailApiService: TvDetailApiService,
        tvDetailMapper: ApiMapper<TvDetail, TvDetailDto>,
        tvMapper: ApiMapper<List<Tv>, TvDto>,
        localTvDataSource: LocalTvDataSource
    ): ITvDetailRepository = TvDetailRepositoryImpl(
        tvDetailApiService,
        tvDetailMapper,
        tvMapper,
        localTvDataSource
    )

    @Provides
    @Singleton
    fun provideTvMapper() : ApiMapper<TvDetail, TvDetailDto> = TvDetailMapperImpl()

    @Provides
    @Singleton
    fun provideTvDetailApiService(): TvDetailApiService {
        val contentType = "application/json".toMediaType()
        return Retrofit.Builder()
            .baseUrl(K.BASE_URL)
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()
            .create(TvDetailApiService::class.java)
    }
}