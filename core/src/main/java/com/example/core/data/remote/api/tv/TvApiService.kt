package com.example.core.data.remote.api.tv

import com.example.core.BuildConfig
import com.example.core.data.remote.responses.tv.TvDto
import com.example.core.utils.K
import retrofit2.http.GET
import retrofit2.http.Query

interface TvApiService {
    @GET(K.TV_ENDPOINT)
    suspend fun fetchDiscoverTv(
        @Query("api_key") apiKey: String = BuildConfig.apiKey,
        @Query("include_adult") includeAdult: Boolean = false,
    ): TvDto

    @GET(K.TOP_RATED_TV_ENDPOINT)
    suspend fun fetchTopRatedTv(
        @Query("api_key") apiKey: String = BuildConfig.apiKey,
        @Query("include_adult") includeAdult: Boolean = false,
    ): TvDto

    @GET(K.UPCOMING_TV_ENDPOINT)
    suspend fun fetchUpcomingTv(
        @Query("api_key") apiKey: String = BuildConfig.apiKey,
        @Query("include_adult") includeAdult: Boolean = false,
    ): TvDto
    @GET(K.SEARCH_TV_ENDPOINT)
    suspend fun fetchSearchTv(
        @Query("query") query: String,
        @Query("api_key") apiKey: String = BuildConfig.apiKey,
        @Query("include_adult") includeAdult: Boolean = false,
    ): TvDto
}