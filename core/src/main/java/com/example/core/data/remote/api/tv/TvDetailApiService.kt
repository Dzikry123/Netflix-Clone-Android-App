package com.example.core.data.remote.api.tv

import com.example.core.BuildConfig
import com.example.core.data.remote.responses.tv.TvDto
import com.example.core.data.remote.responses.tv.tv_detail.TvDetailDto
import com.example.core.utils.K
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

private const val TV_ID = "tv_id"
interface TvDetailApiService {
    @GET("${K.TV_DETAIL_ENDPOINT}/{$TV_ID}")
    suspend fun fetchTvDetail(
        @Path(TV_ID) tvId: Int,
        @Query("api_key") apiKey: String = BuildConfig.apiKey,
        @Query("append_to_response") append: String = "credits, reviews"
    ): TvDetailDto

    @GET(K.TV_ENDPOINT)
    suspend fun fetchTv(
        @Query("api_key") apiKey: String = BuildConfig.apiKey,
        @Query("include_adult") includeAdult: Boolean = false,
    ): TvDto
}