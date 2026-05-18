package com.example.core.data.remote.api.movie

import com.example.core.BuildConfig
import com.example.core.data.remote.responses.movie.MovieDto
import com.example.core.utils.K
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieApiService {
    @GET(K.MOVIE_ENDPOINT)
    suspend fun fetchDiscoverMovie(
        @Query("api_key") apiKey: String = BuildConfig.apiKey,
        @Query("include_adult") includeAdult: Boolean = false
    ): MovieDto

    @GET(K.TRENDING_MOVIE_ENDPOINT)
    suspend fun fetchTrendingMovie(
        @Query("api_key") apiKey: String = BuildConfig.apiKey,
        @Query("include_adult") includeAdult: Boolean = false
    ): MovieDto

    @GET(K.TOP_RATED_MOVIE_ENDPOINT)
    suspend fun fetchTopRatedMovie(
        @Query("api_key") apiKey: String = BuildConfig.apiKey,
        @Query("include_adult") includeAdult: Boolean = false
    ): MovieDto
    @GET(K.UPCOMING_MOVIE_ENDPOINT)
    suspend fun fetchUpcomingMovie(
        @Query("api_key") apiKey: String = BuildConfig.apiKey,
        @Query("include_adult") includeAdult: Boolean = false
    ): MovieDto
    @GET(K.SEARCH_MOVIE_ENDPOINT)
    suspend fun fetchSearchMovie(
        @Query("query") query: String,
        @Query("api_key") apiKey: String = BuildConfig.apiKey,
        @Query("include_adult") includeAdult: Boolean = false
    ): MovieDto
}
