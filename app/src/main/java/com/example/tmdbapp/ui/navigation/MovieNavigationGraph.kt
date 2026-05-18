package com.example.tmdbapp.ui.navigation

import android.util.Log
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.shrinkOut
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.core.dynamicfeature.FavoriteFeatureProvider
import com.example.tmdbapp.ui.movie.detail.MovieDetailScreen
import com.example.tmdbapp.ui.movie.home.MovieScreen
import com.example.tmdbapp.ui.movie.search.SearchMovieScreen
import com.example.tmdbapp.ui.profile.ProfileScreen
import com.example.tmdbapp.ui.tv.detail.TvDetailScreen
import com.example.tmdbapp.ui.tv.home.TvScreen
import com.example.tmdbapp.ui.tv.search.SearchTvScreen

@Composable
fun MovieNavigationGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
    NavHost(
        modifier = modifier.fillMaxSize(),
        navController = navController,
        startDestination = Route.Movie.route
    ) {

        // MOVIE MAIN
        composable(
            route = Route.Movie.route,
            enterTransition = { fadeIn() + scaleIn() },
            exitTransition = { fadeOut() + shrinkOut() }
        ) {
            MovieScreen(
                navController = navController,
                onMovieClick = { id ->
                    navController.navigate(
                        Route.DetailMovie.createRoute(id)
                    ) {
                        launchSingleTop = true
                        popUpTo(Route.Movie.route) {
                            inclusive = false
                        }
                    }
                },
                onSearchClick = {
                    navController.navigate(
                        Route.SearchMovie.route
                    )
                },
                onProfileClick = {
                    navController.navigate(
                        Route.Profile.route
                    )
                }
            )
        }

        // TV MAIN
        composable(
            route = Route.Tv.route,
            enterTransition = { fadeIn() + scaleIn() },
            exitTransition = { fadeOut() + shrinkOut() }
        ) {
            TvScreen(
                navController = navController,
                onTvClick = {
                    id ->
                    Log.d("TV_ID_DEBUG", "Navigate to = ${Route.DetailTv.createRoute(id)}")
                    navController.navigate(
                        Route.DetailTv.createRoute(id)
                    ) {
                        launchSingleTop = true
                        popUpTo(Route.Tv.route) {
                            inclusive = false
                        }
                    }
                },
                onSearchClick = {
                    navController.navigate(
                        Route.SearchTv.route
                    )
                },
                onProfileClick = {
                    navController.navigate(
                        Route.Profile.route
                    )
                }
            )
        }

        // PROFILE MAIN
        composable(
            route = Route.Profile.route,
            enterTransition = { fadeIn() + scaleIn() },
            exitTransition = { fadeOut() + shrinkOut() }
        ) {
            ProfileScreen(
                onLogout = {},
                navController = navController
            )
        }

        // FAVORITE MAIN
        FavoriteFeatureProvider.api?.registerGraph(
            navGraphBuilder = this,
            navController = navController
        )

        // SEARCH MOVIE
        composable(
            route = Route.SearchMovie.route,
            enterTransition = { fadeIn() + scaleIn() },
            exitTransition = { fadeOut() + shrinkOut() }
        ) {
            SearchMovieScreen(
                navController = navController,
                onMovieClick = { id ->
                    navController.navigate(
                        Route.DetailMovie.createRoute(id)
                    ) {
                        launchSingleTop = true
                        popUpTo(Route.Movie.route) {
                            inclusive = false
                        }
                    }
                }
            )
        }

        // SEARCH TV
        composable(
            route = Route.SearchTv.route,
            enterTransition = { fadeIn() + scaleIn() },
            exitTransition = { fadeOut() + shrinkOut() }
        ) {
            SearchTvScreen(
                navController = navController,
                onTvClick = {
                    id ->
                    navController.navigate(
                        Route.DetailTv.createRoute(id)
                    ) {
                        launchSingleTop = true
                        popUpTo(Route.Tv.route) {
                            inclusive = false
                        }
                    }
                }
            )
        }

        // DETAIL MOVIE
        composable(
            route = Route.DetailMovie.routeWithArgs,
            arguments = listOf(
                navArgument(Route.DetailMovie.ARG_ID) {
                    type = NavType.IntType
                }
            )
        ) {
            MovieDetailScreen(
                navController = navController,
                onNavigateUp = {
                    navController.navigateUp()
                },
                onMovieClick = { id ->
                    navController.navigate(
                        Route.DetailMovie.createRoute(id)
                    ) {
                        launchSingleTop = true
                    }
                },
                onActorClick = {}
            )
        }

        // DETAIL TV
        composable(
            route = Route.DetailTv.routeWithArgs,
            arguments = listOf(
                navArgument(Route.DetailTv.ARG_ID) {
                    type = NavType.IntType
                }
            )
        ) {
            TvDetailScreen(
                navController = navController,
                onNavigateUp = {
                    navController.navigateUp()
                },
                onTvClick = { id ->
                    navController.navigate(
                        Route.DetailTv.createRoute(id)
                    ) {
                        launchSingleTop = true
                    }
                },
                onActorClick = {}
            )
        }
    }
}
