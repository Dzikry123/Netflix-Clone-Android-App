package com.example.favoritemovie.data

import android.content.Context
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.shrinkOut
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.startup.Initializer
import com.example.core.dynamicfeature.FavoriteFeatureApi
import com.example.core.dynamicfeature.FavoriteFeatureProvider
import com.example.favoritemovie.ui.FavoriteListScreen
import com.example.tmdbapp.ui.navigation.Route


class FavoriteFeatureImpl : FavoriteFeatureApi {

    override fun registerGraph(
        navGraphBuilder: NavGraphBuilder,
        navController: NavHostController
    ) {

        navGraphBuilder.composable(
            route = Route.Favorite.route,
            enterTransition = { fadeIn() + scaleIn() },
            exitTransition = { fadeOut() + shrinkOut() }
        ) {

            FavoriteListScreen(
                navController = navController,
                onBackClick = {
                    navController.popBackStack()
                },
                onMovieClick = { id ->
                    navController.navigate(
                        Route.DetailMovie.createRoute(id)
                    )
                },
                onTvClick = { id ->
                    navController.navigate(
                        Route.DetailTv.createRoute(id)
                    )
                },
            )
        }
    }
}

class FavoriteInitializer : Initializer<Unit> {

    override fun create(context: Context) {
        FavoriteFeatureProvider.api = FavoriteFeatureImpl()
    }

    override fun dependencies(): List<Class<out Initializer<*>>> =
        emptyList()
}