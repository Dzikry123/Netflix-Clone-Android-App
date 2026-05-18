package com.example.core.utils

object K {
    const val BASE_URL = "https://api.themoviedb.org/3/"

    //movie
    const val BASE_IMAGE_URL = "https://image.tmdb.org/t/p/original/"
    const val MOVIE_ENDPOINT ="discover/movie"
    const val MOVIE_DETAIL_ENDPOINT ="movie"
    const val TRENDING_MOVIE_ENDPOINT ="trending/movie/week"
    const val TOP_RATED_MOVIE_ENDPOINT ="movie/top_rated"
    const val UPCOMING_MOVIE_ENDPOINT ="movie/upcoming"
    const val SEARCH_MOVIE_ENDPOINT ="search/movie"
    const val MOVIE_ID ="id"


    // tv
    const val TV_ENDPOINT ="discover/tv"
    const val TV_DETAIL_ENDPOINT ="tv"
    const val TOP_RATED_TV_ENDPOINT ="tv/top_rated"
    const val SEARCH_TV_ENDPOINT ="search/tv"
    const val UPCOMING_TV_ENDPOINT ="tv/airing_today"
    const val TV_ID ="id"



    // people
    const val MOVIE_ACTOR_ENDPOINT ="person"
    const val ACTOR_ID ="id"
}