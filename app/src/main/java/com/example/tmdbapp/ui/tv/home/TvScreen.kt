package com.example.tmdbapp.ui.tv.home


import android.util.Log
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.core.domain.model.tv.Tv
import com.example.core.utils.GenreConstants
import com.example.core.utils.K
import com.example.tmdbapp.R
import com.example.tmdbapp.ui.movie.home.LoadingView

@Composable
fun TvScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    tvViewModel: TvViewModel = hiltViewModel(),
    onTvClick: (Int) -> Unit,
    onSearchClick: () -> Unit,
    onProfileClick: () -> Unit,
) {
    val state by tvViewModel.tvState.collectAsStateWithLifecycle()

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(color = Color.Black)
    ) {

        if (state.isLoading) {
            LoadingView(isLoading = true)
        }

        if (state.error != null) {
            Text(
                text = state.error ?: "",
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        if (!state.isLoading && state.error == null) {

            LazyColumn {
                item {
                    NetflixTopBar(onSearchClick, onProfileClick)

                    HeroCarousel(
                        tvs = state.topRatedTvs.take(5),
                        onTvClick = { tv ->

                            navController.currentBackStackEntry
                                ?.savedStateHandle
                                ?.set("tv", tv)

                            onTvClick(tv.id)
                        },
                    )
                }

                item {
                    TvSection(
                        title = "Discover TV Shows",
                        tvs = state.discoverTvs,
                        onTvClick = { tv ->

                            navController.currentBackStackEntry
                                ?.savedStateHandle
                                ?.set("tv", tv)

                            onTvClick(tv.id)
                        },
                        onFavoriteClick = { movie, newState ->
                            tvViewModel.onFavoriteToggle(movie, newState)
                        }
                    )
                }

                item {
                    TvSection(
                        title = "Upcoming TV Shows",
                        tvs = state.upcomingTvs,
                        onTvClick = { tv ->

                            navController.currentBackStackEntry
                                ?.savedStateHandle
                                ?.set("tv", tv)

                            onTvClick(tv.id)
                        },
                        onFavoriteClick = { movie, newState ->
                            tvViewModel.onFavoriteToggle(movie, newState)
                        }
                    )
                }

                item {
                    TvSection(
                        title = "Top Rated TV Shows",
                        tvs = state.topRatedTvs,
                        onTvClick = { tv ->

                            navController.currentBackStackEntry
                                ?.savedStateHandle
                                ?.set("tv", tv)

                            onTvClick(tv.id)
                        },
                        onFavoriteClick = { movie, newState ->
                            tvViewModel.onFavoriteToggle(movie, newState)
                        }
                    )
                }
            }
        }
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HeroCarousel(
    tvs: List<Tv>,
    onTvClick: (Tv) -> Unit,
) {
    val pagerState = rememberPagerState { tvs.size }

    Box {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .height(500.dp),
            pageSpacing = 16.dp,
            contentPadding = PaddingValues(16.dp)
        ) { page ->

            val tv = tvs[page]

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(32.dp))
                    .clickable { onTvClick(tv) }
            ) {


                // Gradient overlay
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                listOf(
                                    Color.Transparent,
                                    Color.Black.copy(0.3f),
                                    Color.Black
                                )
                            )
                        )
                )

                AsyncImage(
                    model = "${K.BASE_IMAGE_URL}${tv.posterPath}",
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .matchParentSize()
                        .blur(40.dp)
                )


                AsyncImage(
                    model = "${K.BASE_IMAGE_URL}${tv.posterPath}",
                    contentDescription = null,
                    contentScale = ContentScale.Fit, // ⬅️ penting
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.Center)
                        .aspectRatio(2f / 3f)
                        .graphicsLayer {
                            // paksa poster yang tadinya "Fit" menjadi sedikit lebih besar
                            // agar memotong sedikit bagian atas/bawah tapi tidak se-agresif "Crop"
                            scaleX = 1f
                            scaleY = 1f
//                            shape = RoundedCornerShape(
//                                32.dp
//                            )
                            // 3. Wajib set clip = true
                            clip = true

                        }
//                        .scale(0.85f) // zoom out
//                        .graphicsLayer(
//                            scaleX = 0.85f, // Zoom out ke 80%
//                            scaleY = 0.85f, // Zoom out ke 80%
//                        )
                )

                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    Color.Black.copy(alpha = 0.4f),
                                    Color.Black.copy(alpha = 0.7f),
                                )
                            )
                        )
                )

                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(16.dp)
                ) {

                    // Rating
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = Color.Yellow,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(Modifier.width(4.dp))
                        Text(
                            text = String.format("%.1f", tv.voteAverage),
                            color = Color.White
                        )
                    }

                    Spacer(Modifier.height(6.dp))

                    // Title
                    Text(
                        text = tv.name,
                        style = MaterialTheme.typography.headlineSmall,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        maxLines = 2
                    )

                    Spacer(Modifier.height(8.dp))

                    // Genre Chip
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        tv.genreIds.take(3).forEach { genreId ->
                            val genreName =
                                GenreConstants.getGenreNameById(genreId)

                            Box(
                                modifier = Modifier
                                    .background(
                                        Color.White.copy(0.2f),
                                        RoundedCornerShape(50)
                                    )
                                    .padding(horizontal = 10.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = genreName,
                                    color = Color.White,
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }
                    }
                    Spacer(Modifier.height(12.dp))

                    Button(
                        onClick = {
                            Log.d("TV_ID_DEBUG", "Clicked TV ID = ${tv.id}")
                            onTvClick(tv)
                        },
                        colors = ButtonDefaults.buttonColors(Color.Red),
                        shape = RoundedCornerShape(50)
                    ) {
                        Text("Watch Now", color = Color.White)
                    }
                    Spacer(Modifier.height(24.dp))

                }
            }
        }

        Spacer(Modifier.height(8.dp))

        // Indicator (manual swipe indicator)
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            repeat(tvs.size) { index ->
                val isSelected = pagerState.currentPage == index

                Box(
                    modifier = Modifier
                        .height(4.dp)
                        .width(if (isSelected) 18.dp else 12.dp)
                        .clip(RoundedCornerShape(50))
                        .background(
                            if (isSelected) Color.Red else Color.White.copy(0.4f)
                        )
                )
            }
        }

    }
}


@Composable
fun TvSection(
    title: String,
    tvs: List<Tv>,
    onTvClick: (Tv) -> Unit,
    onFavoriteClick: (Tv, Boolean) -> Unit
) {
    Column(modifier = Modifier.padding(16.dp)) {

        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(12.dp))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(tvs, key = { it.id }) { tv ->
                TvCardItem(
                    tv = tv,
                    onTvClick = onTvClick,
                    onFavoriteClick = onFavoriteClick
                )
            }
        }
    }
}

@Composable
fun TvCardItem(
    tv: Tv,
    onTvClick: (Tv) -> Unit,
    onFavoriteClick: (Tv, Boolean) -> Unit
) {
    Card(
        modifier = Modifier
            .width(140.dp)
            .height(220.dp)
            .clickable { onTvClick(tv) },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {

        Box {
            AsyncImage(
                model = "${K.BASE_IMAGE_URL}${tv.posterPath}",
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            // Bookmark Icon (Top Start)
            IconButton(
                onClick = { onFavoriteClick(tv, !tv.isFavorite) },
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(4.dp)
                    .background(Color.Black.copy(0.5f), CircleShape)
                    .size(32.dp)
            ) {
                Icon(
                    imageVector = if (tv.isFavorite) Icons.Filled.Bookmark else Icons.Outlined.BookmarkBorder,
                    contentDescription = "Favorite",
                    tint = if (tv.isFavorite) Color.Red else Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }

            // rating badge
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(6.dp)
                    .background(Color.Black.copy(.7f), CircleShape)
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = "⭐ ${tv.voteAverage}",
                    color = Color.White,
                    fontSize = 12.sp
                )
            }
        }
    }
}

@Composable
fun LoadingView(
    isLoading: Boolean
) {
    if (!isLoading) return

    val infiniteTransition = rememberInfiniteTransition(label = "LoadingAnimation")

    val scale by infiniteTransition.animateFloat(
        initialValue = 0.85f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(900, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )

    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(900, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "alpha"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        // Background Glow yang Lembut
         Box(
            modifier = Modifier
                .size(250.dp)
                .graphicsLayer {
                    scaleX = scale
                    scaleY = scale
                    this.alpha = alpha * 0.5f // Sedikit lebih terang
                }
                // Gunakan Radial Gradient untuk pendaran cahaya yang lebih natural dari pusat ke luar
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Color.Red.copy(alpha = 0.6f),
                            Color.Red.copy(alpha = 0.3f),
                            Color.Transparent
                        ),
                        center = Offset.Unspecified,
                        radius = Float.POSITIVE_INFINITY
                    )
                )
                // Blur tetap diberikan untuk soft ekstra
                .blur(60.dp)
        )

        // Netflix N Logo
        Text(
            text = "N",
            color = Color.Red,
            fontSize = 90.sp,
            fontWeight = FontWeight.ExtraBold,
            modifier = Modifier.graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
        )

        // --- Loading dots bawah tetap sama ---
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 120.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            repeat(3) { index ->
                val dotAlpha by infiniteTransition.animateFloat(
                    initialValue = 0.2f,
                    targetValue = 1f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(600, delayMillis = index * 200),
                        repeatMode = RepeatMode.Reverse
                    ),
                    label = "dotAlpha"
                )

                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = dotAlpha))
                )
            }
        }
    }
}

@Composable
fun NetflixTopBar(
    onSearchClick: () -> Unit,
    onProfileClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Gradient hitam di paling atas agar teks/ikon terlihat jelas jika gambar hero terang
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color.Black.copy(alpha = 0.7f), Color.Transparent)
                )
            )
            .padding(start = 16.dp, end = 16.dp, top = 16.dp)
//            .statusBarsPadding() // Menghindari notch/status bar
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // --- LOGO NETFLIX ---
            Text(
                text = "NETFLIX",
                color = Color(0xFFE50914), // Merah Khas Netflix
                fontSize = 28.sp,
                fontWeight = FontWeight.Black, // Sangat tebal
                letterSpacing = (-1).sp
            )

            // --- IKON SEARCH & PROFILE ---
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onSearchClick) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = Color.White,
                        modifier = Modifier.size(28.dp)
                    )
                }
                Spacer(Modifier.width(8.dp))
                // Ikon profil kotak khas Netflix
                Box(
                    modifier = Modifier
                        .size(32.dp) // Ukuran kotak
                        .clip(RoundedCornerShape(4.dp))
                        .background(Color.Gray) // Background cadangan jika gambar gagal load
                        .clickable { onProfileClick() }
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.profile),
                        contentDescription = "Profile",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
            }
        }
    }
}
