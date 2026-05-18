package com.example.tmdbapp.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LiveTv
import androidx.compose.material.icons.filled.Movie
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Route(val route: String) {

    object Movie : Route("movie")
    object SearchMovie : Route("searchMovie")

    object DetailMovie : Route("movie_detail") {
        const val ARG_ID = "id"

        val routeWithArgs = "$route/{$ARG_ID}"

        fun createRoute(id: Int) = "$route/$id"
    }

    // TV
    object Tv : Route("tv")
    object SearchTv : Route("searchTv")

    object DetailTv : Route("tv_detail") {
        const val ARG_ID = "id"

        val routeWithArgs = "$route/{$ARG_ID}"

        fun createRoute(id: Int) = "$route/$id"
    }

    // People
    object Person : Route("person")
    object SearchPerson : Route("searchPerson")

    object DetailPerson : Route("person_detail") {
        const val ARG_ID = "id"

        val routeWithArgs = "$route/{$ARG_ID}"

        fun createRoute(id: Int) = "$route/$id"
    }

    // Profile
    object Profile : Route("profile")

    // Favorite
    object Favorite : Route("favorite")

}

sealed class BottomNavItem(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    data object Movie: BottomNavItem(
        route = Route.Movie.route,
        title = "Movies",
        icon = Icons.Default.Movie
    )
    data object Tv: BottomNavItem(
        route = Route.Tv.route,
        title = "TV Shows",
        icon = Icons.Default.LiveTv
    )
}