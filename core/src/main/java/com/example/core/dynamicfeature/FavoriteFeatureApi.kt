package com.example.core.dynamicfeature

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.example.core.domain.usecase.movie.MovieUseCase
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

interface FavoriteFeatureApi {

    fun registerGraph(
        navGraphBuilder: NavGraphBuilder,
        navController: NavHostController
    )
}

object FavoriteFeatureProvider {
    var api: FavoriteFeatureApi? = null
}