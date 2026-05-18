package com.example.tmdbapp.ui.tv.search


import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SearchOff
import androidx.compose.material.icons.outlined.PlayCircleOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.core.domain.model.tv.Tv
import com.example.core.utils.K
import com.example.tmdbapp.ui.tv.home.TvViewModel
import kotlinx.coroutines.delay

@Composable
fun SearchTvScreen(
    viewModel: TvViewModel = hiltViewModel(),
    navController: NavController,
    onTvClick: (Int) -> Unit
) {
    val state by viewModel.tvState.collectAsStateWithLifecycle()

    var query by remember { mutableStateOf("") }

    LaunchedEffect(query) {
        delay(500) // tunggu user berhenti ngetik
        viewModel.searchTv(query)
    }

    Scaffold(
        topBar = {
            SearchBar(query = query, onQueryChange = { query = it })
        }, containerColor = Color.Black
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {

            // Loading Bar tepat di bawah Search Bar
            if (state.isLoading) {
                NetflixLoadingBar()
            }

            if (state.searchTv.isEmpty() && query.isNotBlank() && !state.isLoading) {
                EmptySearchView(query)
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(2.dp) // Jarak tipis antar item
                ) {
                    item {
                        Text(
                            text = if (query.isEmpty()) "Top Searches" else "Result for $query ",
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.White,
                            fontWeight = FontWeight.ExtraBold,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                    if (query.isEmpty()) {
                        items(state.topRatedTvs) { tv ->
                            TvSearchItem(
                                tv = tv,
                                onTvClick = { tv ->

                                    navController.currentBackStackEntry
                                        ?.savedStateHandle
                                        ?.set("tv", tv)

                                    onTvClick(tv.id)
                                },
                            )
                        }
                    } else {
                        items(state.searchTv) { tv ->
                            TvSearchItem(
                                tv = tv,
                                onTvClick = { tv ->

                                    navController.currentBackStackEntry
                                        ?.savedStateHandle
                                        ?.set("tv", tv)

                                    onTvClick(tv.id)
                                },
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit
) {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF121212))
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        BasicTextField(
            value = query,
            onValueChange = onQueryChange,
            singleLine = true,
            cursorBrush = SolidColor(Color.Red),
            textStyle = TextStyle(color = Color.White, fontSize = 16.sp),
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .background(Color(0xFF2B2B2B)) // Abu-abu Netflix
                .padding(horizontal = 12.dp, vertical = 10.dp),
            decorationBox = { innerTextField ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.Search,
                        null,
                        tint = Color.Gray,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(Modifier.width(12.dp))
                    Box(Modifier.weight(1f)) {
                        if (query.isEmpty()) {
                            Text(
                                "Search for a tv shows, genre, etc.",
                                color = Color.Gray,
                                fontSize = 15.sp
                            )
                        }
                        innerTextField()
                    }
                    if (query.isNotEmpty()) {
                        Icon(
                            Icons.Default.Close,
                            null,
                            tint = Color.Gray,
                            modifier = Modifier
                                .size(20.dp)
                                .clickable { onQueryChange("") })
                    }
                }
            })
    }
}

@Composable
fun TvSearchItem(
    tv: Tv,
    onTvClick: (Tv) -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(90.dp)
            .background(Color.Black)
            .padding(bottom = 8.dp)
            .clickable {
                keyboardController?.hide()
                onTvClick(tv)
            },verticalAlignment = Alignment.CenterVertically
    ) {
        // Poster/Backdrop (Kiri)
        Box(
            modifier = Modifier
                .width(140.dp)
                .fillMaxHeight()
                .clip(RoundedCornerShape(4.dp))
        ) {
            AsyncImage(
                model = if (tv.backdropPath.isNotBlank()) "${K.BASE_IMAGE_URL}${tv.backdropPath}"
                else "${K.BASE_IMAGE_URL}${tv.posterPath}",
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            // Overlay gelap tipis agar gambar tidak terlalu terang
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(0.2f))
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        // Info Bagian Tengah
        Column(
            modifier = Modifier.weight(1f), verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = tv.name,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(4.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                // Rating Merah khas Netflix
                Text(
                    text = "${(tv.voteAverage * 10).toInt()}% Match",
                    color = Color(0xFF46D369), // Hijau Match Netflix
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = tv.originalLanguage.uppercase(),
                    color = Color.Gray,
                    style = MaterialTheme.typography.labelSmall
                )
            }
        }

        // Play Icon (Kanan)
        Icon(
            imageVector = Icons.Outlined.PlayCircleOutline,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .size(28.dp)
        )
    }
}

@Composable
fun EmptySearchView(
    query: String
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(horizontal = 32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Icon(
            imageVector = Icons.Default.SearchOff,
            contentDescription = null,
            tint = Color.Red,
            modifier = Modifier.size(90.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Your search didn't have any matches.",
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Try different keywords for \"$query\"",
            color = Color.Gray,
            fontSize = 14.sp,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(32.dp))

        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(Color(0xFF1C1C1C))
                .padding(horizontal = 18.dp, vertical = 10.dp)
        ) {

            Text(
                text = "Explore Popular Titles",
                color = Color.White,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun NetflixLoadingBar() {

    val infiniteTransition = rememberInfiniteTransition(label = "")

    val progress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = LinearEasing)
        ),
        label = ""
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(3.dp)
            .background(Color.DarkGray.copy(alpha = 0.4f))
    ) {

        Box(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(progress)
                .background(Color.Red)
        )
    }
}