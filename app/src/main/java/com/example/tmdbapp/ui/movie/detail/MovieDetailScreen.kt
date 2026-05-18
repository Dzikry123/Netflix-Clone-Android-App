package com.example.tmdbapp.ui.movie.detail

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.core.domain.model.movie.CastMovie
import com.example.core.domain.model.movie.Movie
import com.example.core.domain.model.movie.MovieDetail
import com.example.core.domain.model.movie.Review
import com.example.core.utils.GenreConstants
import com.example.core.utils.K
import com.example.tmdbapp.R
import com.example.tmdbapp.ui.movie.home.LoadingView
import com.example.tmdbapp.ui.movie.home.MovieCardItem

@Composable
fun MovieDetailScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    movieDetailViewModel: MovieDetailViewModel = hiltViewModel(),
    onNavigateUp: () -> Unit,
    onMovieClick: (Int) -> Unit,
    onActorClick: (Int) -> Unit,
) {
    val state by movieDetailViewModel.detailState.collectAsStateWithLifecycle()

//    val movie = movieDetailViewModel.movie
//    val movie =
//        navController.previousBackStackEntry
//            ?.savedStateHandle
//            ?.get<Movie>("movie")
//
//    var isFavorite by remember {
//        mutableStateOf(movie?.isFavorite ?: false)
//    }

    val isFavorite = state.isFavorite

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black) // Hitam pekat
    ) {
        state.movieDetail?.let { movieDetail ->
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                // 1. Header Poster dengan Gradient
                item {
                    HeroHeader(
                        movie = movieDetail,
                        isFavorite = isFavorite,
                        onFavoriteClick = {
                            val newState = !state.isFavorite

                            movieDetailViewModel.onFavoriteToggle(
                                !state.isFavorite
                            )

//                            isFavorite = !isFavorite

//                            movie?.let {
//                                movieDetailViewModel.onFavoriteToggle(
//                                    it,
//                                    isFavorite
//                                )
//                            }
                        },
                        onNavigateUp = onNavigateUp
                    )
                }

                item {
                    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                        // 2. Meta Data (Match, Year, Runtime)
                        MovieMetaSection(movieDetail)

                        // 3. Tombol Aksi (Play & Download)
                        ActionButtons(movieDetail)

                        // 4. Deskripsi
                        OverviewSection(movieDetail.overview)

                        // 5. Informasi Tambahan (Bahasa & Negara)
                        InfoSection(movieDetail)
                    }
                }

                // 6. Cast Section
                item {
                    CastSection(movieDetail.castMovie, onActorClick)
                }

                // 7. Review Section (Netflix Style)
                item {
                    ReviewSection(movieDetail.reviews)
                }

                // 8. Rekomendasi
                item {
                    MoreLikeThis(
                        fetchMovies = movieDetailViewModel::fetchMovie,
                        isMovieLoading = state.isMovieLoading,
                        movies = state.movies,
                        onMovieClick = { movie ->
                            navController.currentBackStackEntry
                                ?.savedStateHandle
                                ?.set("movie", movie)

                            onMovieClick(movie.id)
                        },
                        onFavoriteClick = { _, newState ->
                            movieDetailViewModel.onFavoriteToggle(newState)
                        }
                    )
                }

                item { Spacer(modifier = Modifier.height(32.dp)) }
            }
        }
        LoadingView(isLoading = state.isLoading)
    }
}

@Composable
fun HeroHeader(
    movie: MovieDetail,
    isFavorite: Boolean,
    onFavoriteClick: () -> Unit,
    onNavigateUp: () -> Unit
) {
    Box(modifier = Modifier
        .height(450.dp)
        .fillMaxWidth()) {
        AsyncImage(
            model = "${K.BASE_IMAGE_URL}${movie.backdropPath}",
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // Gradient Overlay
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color.Transparent, Color.Black.copy(0.5f), Color.Black),
                        startY = 300f
                    )
                )
        )

        // Back Button
        IconButton(
            onClick = onNavigateUp,
            modifier = Modifier
                .padding(top = 32.dp, start = 16.dp)
                .align(Alignment.TopStart)
                .background(Color.Black.copy(0.5f), CircleShape)
        ) {
            Icon(Icons.AutoMirrored.Default.ArrowBack, null, tint = Color.White)
        }

        Row(
            modifier = Modifier
                .padding(top = 28.dp, end = 16.dp)
                .align(Alignment.TopEnd),
            horizontalArrangement = Arrangement.spacedBy(12.dp) // Jarak antar tombol
        ) {
            // Bookmark Button
            IconButton(
                onClick = onFavoriteClick,
                modifier = Modifier
                    .background(Color.Black.copy(0.5f), CircleShape)
            ) {
                Icon(
                    imageVector =
                        if (isFavorite)
                            Icons.Filled.Bookmark
                        else
                            Icons.Outlined.BookmarkBorder,
                    contentDescription = "Bookmark",
                    tint =
                        if (isFavorite)
                            Color.Red
                        else
                            Color.White
                )
            }

            // Share Button
            IconButton(
                onClick = {  },
                modifier = Modifier
                    .background(Color.Black.copy(0.5f), CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Default.Share,
                    contentDescription = "Share",
                    tint = Color.White
                )
            }
        }

        // Judul dengan Aksen Merah
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(modifier = Modifier
                    .width(3.dp)
                    .height(20.dp)
                    .background(Color(0xFFE50914)))
                Spacer(Modifier.width(8.dp))
                Text(
                    text = "FILM",
                    style = MaterialTheme.typography.labelMedium,
                    color = Color.LightGray,
                    letterSpacing = 4.sp
                )
            }
            Spacer(Modifier.height(8.dp))
            Text(
                text = movie.title.uppercase(),
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White,
                fontWeight = FontWeight.ExtraBold,
                lineHeight = 32.sp
            )
        }
    }
}

@Composable
fun MovieMetaSection(movie: MovieDetail) {
    val genres = remember(movie.genreIds) {
        movie.genreIds.map { genreId ->
            GenreConstants.getGenreNameById(genreId)
        }
    }
    Row(
        modifier = Modifier.padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Simulasi Match Percentage
        val match = (movie.voteAverage * 10).toInt()
        Text(
            text = "$match% Match",
            color = Color(0xFF46D369), // Hijau Netflix
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.bodyMedium
        )

        Text(text = movie.releaseDate.take(4), color = Color.LightGray)

        // Badge Kualitas (HD/4K)
        Surface(
            color = Color.Transparent,
            border = BorderStroke(1.dp, Color.Gray),
            shape = RoundedCornerShape(4.dp)
        ) {
            Text(
                text = if (movie.video) " 4K " else " HD ",
                modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp),
                style = MaterialTheme.typography.labelSmall,
                color = Color.Gray
            )
        }

        Text(text = movie.runTime, color = Color.LightGray)
    }

    Text(
        text = genres.joinToString(" • "),
        style = MaterialTheme.typography.bodySmall,
        color = Color.LightGray,
        modifier = Modifier.padding(bottom = 8.dp)
    )
}

@Composable
fun ActionButtons(movie: MovieDetail) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = { /* Play Action */ },
            colors = ButtonDefaults.buttonColors(containerColor = Color.White),
            shape = RoundedCornerShape(4.dp)
        ) {
            Icon(Icons.Default.PlayArrow, null, tint = Color.Black)
            Spacer(Modifier.width(8.dp))
            Text("Play", color = Color.Black, fontWeight = FontWeight.Bold)
        }

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = { /* Download Action */ },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2B2B2B)),
            shape = RoundedCornerShape(4.dp)
        ) {
            Icon(Icons.Default.FileDownload, null, tint = Color.White)
            Spacer(Modifier.width(8.dp))
            Text("Download", color = Color.White, fontWeight = FontWeight.Bold)
        }
    }
}


@Composable
fun OverviewSection(text: String) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text("Overview", fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(8.dp))
        CollapsibleText(text = text)
    }
}

@Composable
fun InfoSection(movie: MovieDetail) {
    Column(modifier = Modifier.padding(vertical = 16.dp)) {
        Divider(color = Color.DarkGray, thickness = 0.5.dp)
        Spacer(Modifier.height(16.dp))

        Text(
            text = "Audio: ${movie.language.joinToString(", ")}",
            style = MaterialTheme.typography.bodySmall,
            color = Color.Gray
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text = "Country: ${movie.productionCountry.joinToString(", ")}",
            style = MaterialTheme.typography.bodySmall,
            color = Color.Gray
        )
    }
}

@Composable
fun InfoRow(title: String, items: List<String>) {
    Text(
        text = "$title: ${items.joinToString()}",
        style = MaterialTheme.typography.bodyMedium
    )
}

@Composable
fun CollapsibleText(
    text: String,
    collapsedMaxLines: Int = 3
) {
    var expanded by remember { mutableStateOf(false) }

    Column {
        Text(
            text = text,
            maxLines = if (expanded) Int.MAX_VALUE else collapsedMaxLines,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.bodyMedium
        )

        Text(
            text = if (expanded) "See less" else "See more",
            color = Color(0xFFE50914),
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier
                .padding(top = 4.dp)
                .clickable { expanded = !expanded }
        )
    }
}

@Composable
fun CastSection(
    castMovie: List<CastMovie>,
    onActorClick: (Int) -> Unit
) {
    Column(modifier = Modifier.padding(16.dp)) {

        Text("Cast", fontWeight = FontWeight.Bold)

        Spacer(Modifier.height(8.dp))

        LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            items(castMovie) {
                ActorItem(
                    castMovie = it,
                    modifier = Modifier.clickable { onActorClick(it.id) }
                )
            }
        }
    }
}

@Composable
fun ActorItem(
    modifier: Modifier = Modifier,
    castMovie: CastMovie
) {
    Column(
        modifier = modifier.width(80.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        AsyncImage(
            model = "${K.BASE_IMAGE_URL}${castMovie.profilePath}",
            contentDescription = null,
            modifier = Modifier
                .size(70.dp)
                .clip(CircleShape)
                .border(2.dp, Color.White.copy(0.2f), CircleShape),
            contentScale = ContentScale.Crop,
            placeholder = painterResource(R.drawable.baseline_person_24)
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = castMovie.firstName,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            maxLines = 1
        )

        Text(
            text = castMovie.genderRole,
            style = MaterialTheme.typography.bodySmall,
            color = Color.Gray,
            maxLines = 1
        )
    }
}

@Composable
fun MoreLikeThis(
    modifier: Modifier = Modifier,
    fetchMovies: () -> Unit,
    isMovieLoading: Boolean,
    movies: List<Movie>,
    onMovieClick: (Movie) -> Unit,
    onFavoriteClick: (Movie, Boolean) -> Unit
) {
    LaunchedEffect(Unit) {
        fetchMovies()
    }

    Column(modifier = modifier.padding(16.dp)) {

        Text(
            text = "More Like This",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(12.dp))

        if (isMovieLoading) {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(movies) {
                    MovieCardItem(movie = it, onMovieClick = onMovieClick, onFavoriteClick = onFavoriteClick )
                }
            }
        }
    }
}

@Composable
fun ReviewSection(reviews: List<Review>) {
    Column(modifier = Modifier.padding(16.dp)) {

        Text("Reviews", fontWeight = FontWeight.Bold)

        Spacer(Modifier.height(8.dp))

        reviews.take(3).forEach {
            ReviewItem(review = it)
            Spacer(Modifier.height(12.dp))
        }
    }
}

@Composable
fun ReviewItem(review: Review) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF121212))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Initial Avatar
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFE50914)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(review.author.first().toString(), color = Color.White)
                }
                Spacer(Modifier.width(12.dp))
                Column {
                    Text(review.author, fontWeight = FontWeight.Bold, color = Color.White)
                    Text(review.createdAt, style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                }
            }
            Spacer(Modifier.height(12.dp))
            Text(
                text = review.content,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.LightGray,
                maxLines = 4,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}