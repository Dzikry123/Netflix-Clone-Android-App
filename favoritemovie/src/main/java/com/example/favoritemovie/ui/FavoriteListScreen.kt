package com.example.favoritemovie.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.core.domain.model.movie.Movie
import com.example.core.domain.model.tv.Tv
import com.example.core.dynamicfeature.FavoriteEntryPoint
import com.example.core.utils.K
import dagger.hilt.android.EntryPointAccessors


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoriteListScreen(
    navController: NavController,
    onBackClick: () -> Unit,
    onMovieClick: (Int) -> Unit,
    onTvClick: (Int) -> Unit,
) {
    val context = LocalContext.current

    val useCase = remember {
        EntryPointAccessors.fromApplication(
            context.applicationContext,
            FavoriteEntryPoint::class.java
        ).movieUseCase()
    }

    val useCaseTwo = remember {
        EntryPointAccessors.fromApplication(
            context.applicationContext,
            FavoriteEntryPoint::class.java
        ).tvUseCase()
    }

    val viewModel: FavoriteListViewModel = viewModel(
        factory = FavoriteViewModelFactory(useCase, useCaseTwo)
    )

    val state by viewModel.favoriteState.collectAsStateWithLifecycle()

    // State untuk mengontrol tab
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val tabs = listOf("Movies", "TV Shows")

    Scaffold(
        containerColor = Color.Black,
        topBar = {
            Column {
                TopAppBar(
                    title = {
                        Text(
                            "My List",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.titleLarge
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = onBackClick) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Back",
                                tint = Color.White
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Black
                    )
                )

                // TAB kategori
                PrimaryTabRow(
                    selectedTabIndex = selectedTabIndex,
                    containerColor = Color.Black,
                    contentColor = Color.White,
                    indicator = {
                        TabRowDefaults.PrimaryIndicator(
                            modifier = Modifier.tabIndicatorOffset(selectedTabIndex),
                            width = 60.dp,
                            color = Color.Red
                        )
                    },
                    divider = {} // Menghilangkan garis pemisah bawah agar lebih clean
                ) {
                    tabs.forEachIndexed { index, title ->
                        Tab(
                            selected = selectedTabIndex == index,
                            onClick = { selectedTabIndex = index },
                            text = {
                                Text(
                                    text = title,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = if (selectedTabIndex == index) FontWeight.Bold else FontWeight.Normal,
                                    color = if (selectedTabIndex == index) Color.White else Color.Gray
                                )
                            }
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // tampilan berdasarkan tab yang dipilih
            val currentData = if (selectedTabIndex == 0) {
                state.favoriteMovies
            } else {
                state.favoriteTvs
            }

            when {
                state.isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = Color.Red
                    )
                }

                currentData.isEmpty() -> {
                    EmptyStateView(categoryName = tabs[selectedTabIndex])
                }

                else -> {
                    if (selectedTabIndex == 0) {
                        FavoriteMovieGrid(
                            movies = state.favoriteMovies,
                            navController = navController,
                            onMovieClick = onMovieClick
                        )
                    } else {
                        FavoriteTvGrid(
                            tvs = state.favoriteTvs,
                            navController = navController,
                            onTvClick = onTvClick
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun FavoriteMovieGrid(
    movies: List<Movie>,
    navController: NavController,
    onMovieClick: (Int) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(movies, key = { it.id }) { movie ->

            FavoriteMovieItem(
                movie = movie,
                onClick = {

                    navController.currentBackStackEntry
                        ?.savedStateHandle
                        ?.set("movie", movie)

                    onMovieClick(movie.id)
                }
            )
        }
    }
}

@Composable
fun FavoriteMovieItem(
    movie: Movie,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .aspectRatio(2f / 3f)
            .clickable { onClick() },
        shape = RoundedCornerShape(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.DarkGray)
    ) {
        AsyncImage(
            model = "${K.BASE_IMAGE_URL}${movie.posterPath}",
            contentDescription = movie.title,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
    }
}



@Composable
fun FavoriteTvGrid(
    tvs: List<Tv>,
    navController: NavController,
    onTvClick: (Int) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(tvs, key = { it.id }) { tv ->

            FavoriteTvItem(
                tv = tv,
                onClick = {

                    navController.currentBackStackEntry
                        ?.savedStateHandle
                        ?.set("tv", tv)

                    onTvClick(tv.id)
                }
            )
        }
    }
}

@Composable
fun FavoriteTvItem(
    tv: Tv,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .aspectRatio(2f / 3f)
            .clickable { onClick() },
        shape = RoundedCornerShape(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.DarkGray)
    ) {
        AsyncImage(
            model = "${K.BASE_IMAGE_URL}${tv.posterPath}",
            contentDescription = tv.name,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
    }
}

@Composable
fun EmptyStateView(categoryName: String) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.Info,
            contentDescription = null,
            tint = Color.DarkGray,
            modifier = Modifier.size(80.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            "You haven't added any $categoryName yet.",
            color = Color.Gray,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 40.dp)
        )
    }
}