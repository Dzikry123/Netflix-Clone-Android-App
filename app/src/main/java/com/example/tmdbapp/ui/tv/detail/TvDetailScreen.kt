package com.example.tmdbapp.ui.tv.detail

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
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.core.domain.model.tv.CastTv
import com.example.core.domain.model.tv.LastEpsToAir
import com.example.core.domain.model.tv.ReviewTv
import com.example.core.domain.model.tv.SeasonModel
import com.example.core.domain.model.tv.Tv
import com.example.core.domain.model.tv.TvDetail
import com.example.core.utils.K
import com.example.tmdbapp.R
import com.example.tmdbapp.ui.tv.home.LoadingView
import com.example.tmdbapp.ui.tv.home.TvCardItem


@Composable
fun TvDetailScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    tvDetailViewModel: TvDetailViewModel = hiltViewModel(),
    onNavigateUp: () -> Unit,
    onTvClick: (Int) -> Unit,
    onActorClick: (Int) -> Unit
) {
    val state by tvDetailViewModel.detailState.collectAsStateWithLifecycle()

//    val tv = tvDetailViewModel.tv
//
//    var isFavorite by remember {
//        mutableStateOf(tv?.isFavorite ?: false)
//    }

    val isFavorite = state.isFavorite


    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        state.tvDetail?.let { tvDetail ->
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                item {
                    HeroHeader(
                        tv = tvDetail,
                        isFavorite = isFavorite,
                        onFavoriteClick = {

                            tvDetailViewModel.onFavoriteToggle(
                                !state.isFavorite
                            )
                        },
                        onNavigateUp = onNavigateUp
                )
                }

                item {
                    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                        TvMetaSection(tvDetail)
                        ActionButtons()

                        if (tvDetail.tagline.isNotEmpty() && tvDetail.tagline != "Unknown ") {
                            Text(
                                text = "\"${tvDetail.tagline}\"",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.Gray,
                                fontStyle = FontStyle.Italic,
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
                        }

                        OverviewSection(tvDetail.overview)
                    }
                }

                // Daftar Seasons
                item { SeasonSection(tvDetail.seasons) }

                item { CastSection(tvDetail.castTv, onActorClick) }

                // Info Episode Terakhir
                item { LastEpisodeSection(tvDetail.lastEpsToAir) }

                item { ReviewSection(tvDetail.reviews) }

                item {
                    MoreLikeThis(
                        fetchMovies = tvDetailViewModel::fetchTv,
                        isMovieLoading = state.isTvLoading,
                        tvs = state.tv,
                        onTvClick = { tv ->
                            navController.currentBackStackEntry
                                ?.savedStateHandle
                                ?.set("tv", tv)

                            onTvClick(tv.id)
                        },
                        onFavoriteClick = { _, newState ->
                            tvDetailViewModel.onFavoriteToggle(newState)
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
    tv: TvDetail,
    isFavorite: Boolean,
    onFavoriteClick: () -> Unit,
    onNavigateUp: () -> Unit
) {
    Box(
        modifier = Modifier
            .height(450.dp)
            .fillMaxWidth()
    ) {
        AsyncImage(
            model = "${K.BASE_IMAGE_URL}${tv.backdropPath}",
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
                        colors = listOf(Color.Transparent, Color.Black.copy(0.7f), Color.Black),
                        startY = 300f
                    )
                )
        )

        // Tombol Back
        IconButton(
            onClick = onNavigateUp,
            modifier = Modifier
                .padding(top = 28.dp, start = 16.dp)
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

        // Judul & Indikator Merah
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(16.dp)
        ) {
            // Aksen Merah kecil di atas judul
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(modifier = Modifier.width(3.dp).height(20.dp).background(Color(0xFFE50914)))
                Spacer(Modifier.width(8.dp))
                Text(
                    text = "TV SHOW",
                    style = MaterialTheme.typography.labelMedium,
                    color = Color.LightGray,
                    letterSpacing = 4.sp
                )
            }
            Spacer(Modifier.height(8.dp))
            Text(
                text = tv.name.uppercase(),
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White,
                fontWeight = FontWeight.ExtraBold
            )
        }
    }
}

@Composable
fun TvMetaSection(tv: TvDetail) {
    Column(modifier = Modifier.padding(vertical = 12.dp)) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Rating Hijau seperti Netflix (jika > 7)
            Text(
                text = "${(tv.voteAverage * 10).toInt()}% Match",
                color = Color(0xFF46D369),
                fontWeight = FontWeight.Bold
            )
            Text(text = tv.firstAirDate.take(4), color = Color.Gray) // Hanya ambil tahun

            // Badge HD/4K
            Surface(
                color = Color.Transparent,
                border = BorderStroke(1.dp, Color.Gray),
                shape = RoundedCornerShape(4.dp)
            ) {
                Text(
                    " ${tv.status} ",
                    modifier = Modifier.padding(2.dp),
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.Gray
                )
            }

            Text(text = "${tv.numberOfSeason} Seasons", color = Color.Gray)
        }

        Spacer(Modifier.height(8.dp))

        Text(
            text = tv.genreIds.joinToString(" • "),
            style = MaterialTheme.typography.bodySmall,
            color = Color.LightGray
        )
    }
}

@Composable
fun ActionButtons() {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.padding(vertical = 8.dp)
    ) {
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {},
            colors = ButtonDefaults.buttonColors(containerColor = Color.White),
            shape = RoundedCornerShape(4.dp)
        ) {
            Icon(Icons.Default.PlayArrow, null, tint = Color.Black)
            Spacer(Modifier.width(8.dp))
            Text("Play", color = Color.Black, fontWeight = FontWeight.Bold)
        }

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {},
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF333333)),
            shape = RoundedCornerShape(4.dp)
        ) {
            Icon(Icons.Default.FileDownload, null, tint = Color.White)
            Spacer(Modifier.width(8.dp))
            Text("Download", color = Color.White, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun SeasonSection(seasons: List<SeasonModel>) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            "Seasons",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
        Spacer(Modifier.height(8.dp))
        LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            items(seasons) { season ->
                Column(modifier = Modifier.width(120.dp)) {
                    AsyncImage(
                        model = "${K.BASE_IMAGE_URL}${season.posterPath}",
                        contentDescription = null,
                        modifier = Modifier
                            .height(180.dp)
                            .clip(RoundedCornerShape(8.dp)),
                        contentScale = ContentScale.Crop
                    )
                    Text(
                        season.name,
                        color = Color.White,
                        maxLines = 1,
                        style = MaterialTheme.typography.bodySmall
                    )
                    Text(
                        "${season.episodeCount} Episodes",
                        color = Color.Gray,
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }
        }
    }
}

@Composable
fun LastEpisodeSection(lastEps: LastEpsToAir) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            "Latest Episode",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
        Card(
            modifier = Modifier.padding(top = 8.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1A1A))
        ) {
            Row(
                modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = "${K.BASE_IMAGE_URL}${lastEps.stillPath}",
                    contentDescription = null,
                    modifier = Modifier
                        .size(100.dp, 60.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    contentScale = ContentScale.Crop
                )
                Column(modifier = Modifier.padding(start = 12.dp)) {
                    Text(
                        "S${lastEps.seasonNumber} E${lastEps.episodeNumber}",
                        color = Color(0xFFE50914),
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        lastEps.name,
                        color = Color.White,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        lastEps.airDate,
                        color = Color.Gray,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
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
fun InfoSection(tv: TvDetail) {
    Column(modifier = Modifier.padding(16.dp)) {

        InfoRow("Language", tv.languages)
        InfoRow("Country", tv.productionCountry)
    }
}

@Composable
fun InfoRow(title: String, items: List<String>) {
    Text(
        text = "$title: ${items.joinToString()}", style = MaterialTheme.typography.bodyMedium
    )
}

@Composable
fun CollapsibleText(
    text: String, collapsedMaxLines: Int = 3
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
                .clickable { expanded = !expanded })
    }
}

@Composable
fun CastSection(
    castTv: List<CastTv>, onActorClick: (Int) -> Unit
) {
    Column(modifier = Modifier.padding(16.dp)) {

        Text("Cast", fontWeight = FontWeight.Bold)

        Spacer(Modifier.height(8.dp))

        LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            items(castTv) {
                ActorItem(
                    castTv = it, modifier = Modifier.clickable { onActorClick(it.id) })
            }
        }
    }
}

@Composable
fun ActorItem(
    modifier: Modifier = Modifier, castTv: CastTv
) {
    Column(
        modifier = modifier.width(80.dp), horizontalAlignment = Alignment.CenterHorizontally
    ) {

        AsyncImage(
            model = "${K.BASE_IMAGE_URL}${castTv.profilePath}",
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
            text = castTv.firstName,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            maxLines = 1
        )

        Text(
            text = castTv.genderRole,
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
    tvs: List<Tv>,
    onTvClick: (Tv) -> Unit,
    onFavoriteClick: (Tv, Boolean) -> Unit
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
                modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(tvs) {
                    TvCardItem(tv = it, onTvClick = onTvClick, onFavoriteClick = onFavoriteClick )
                }
            }
        }
    }
}

@Composable
fun ReviewSection(reviews: List<ReviewTv>) {
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
fun ReviewItem(
    modifier: Modifier = Modifier, review: ReviewTv
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {

        Column(modifier = Modifier.padding(12.dp)) {

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                // Avatar
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = review.author.first().uppercase(),
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Column {
                    Text(
                        text = review.author, fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = review.createdAt,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Star, null, tint = Color.Yellow)
                    Text(
                        text = "${review.rating}", style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            CollapsibleText(text = review.content)
        }
    }
}