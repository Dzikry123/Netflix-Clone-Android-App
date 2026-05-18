//package com.example.tmdbapp.ui.tv.search
//
//package com.example.tmdbapp.ui.tv.search
//
//
//import androidx.compose.animation.AnimatedVisibility
//import androidx.compose.animation.core.LinearEasing
//import androidx.compose.animation.core.animateFloat
//import androidx.compose.animation.core.infiniteRepeatable
//import androidx.compose.animation.core.rememberInfiniteTransition
//import androidx.compose.animation.core.tween
//import androidx.compose.foundation.background
//import androidx.compose.foundation.border
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.PaddingValues
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxHeight
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.size
//import androidx.compose.foundation.layout.width
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.items
//import androidx.compose.foundation.shape.CircleShape
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.foundation.text.BasicTextField
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.Close
//import androidx.compose.material.icons.filled.Search
//import androidx.compose.material.icons.filled.SearchOff
//import androidx.compose.material.icons.filled.Star
//import androidx.compose.material3.Card
//import androidx.compose.material3.CardDefaults
//import androidx.compose.material3.Icon
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.LaunchedEffect
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.graphics.Brush
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.graphics.SolidColor
//import androidx.compose.ui.layout.ContentScale
//import androidx.compose.ui.text.TextStyle
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.text.style.TextAlign
//import androidx.compose.ui.text.style.TextOverflow
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import androidx.hilt.navigation.compose.hiltViewModel
//import androidx.lifecycle.compose.collectAsStateWithLifecycle
//import androidx.navigation.NavController
//import coil.compose.AsyncImage
//import com.example.core.domain.model.tv.Tv
//import com.example.core.utils.GenreConstants
//import com.example.core.utils.K
//import com.example.tmdbapp.R
//import com.example.tmdbapp.ui.tv.home.TvViewModel
//import kotlinx.coroutines.delay
//
//@Composable
//fun SearchTvScreen(
//    viewModel: TvViewModel = hiltViewModel(),
//    navController: NavController,
//    onTvClick: (Int) -> Unit
//) {
//    val state by viewModel.tvState.collectAsStateWithLifecycle()
//
//    var query by remember { mutableStateOf("") }
//
//    LaunchedEffect(query) {
//        delay(500) // tunggu user berhenti ngetik
//        viewModel.searchTv(query)
//    }
//
//    Box(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(color = Color.Black)
//    ) {
//
//        Column {
//
//            // 🔍 Modern Search Bar
//            SearchBar(
//                query = query,
//                onQueryChange = { query = it }
//            )
//
//            if (state.isLoading) {
////                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
//                NetflixLoadingBar()
//            }
//
//            if (state.searchTv.isEmpty() && query.isNotBlank()) {
//                EmptySearchView(query)
//            } else {
//                LazyColumn(
//                    contentPadding = PaddingValues(16.dp),
//                    verticalArrangement = Arrangement.spacedBy(16.dp)
//                ) {
//                    item {
//                        Text(
//                            text = if (query.isEmpty()) "Top Searches" else "Result for $query ",
//                            style = MaterialTheme.typography.titleMedium,
//                            color = Color.White,
//                            fontWeight = FontWeight.ExtraBold,
//                            modifier = Modifier.padding(16.dp)
//                        )
//                    }
//                    if (query.isEmpty()) {
//                        items(state.topRatedTvs) { tv ->
//                            TvSearchItem(
//                                tv = tv,
//                                onTvClick = { tv ->
//
//                                    navController.currentBackStackEntry
//                                        ?.savedStateHandle
//                                        ?.set("tv", tv)
//
//                                    onTvClick(tv.id)
//                                },
//                            )
//                        }
//                    } else {
//                        items(state.searchTv) { tv ->
//                            TvSearchItem(
//                                tv = tv,
//                                onTvClick = { tv ->
//
//                                    navController.currentBackStackEntry
//                                        ?.savedStateHandle
//                                        ?.set("tv", tv)
//
//                                    onTvClick(tv.id)
//                                },
//                            )
//                        }
//                    }
//                }
//            }
//        }
//    }
//}
//
//@Composable
//fun SearchBar(
//    query: String,
//    onQueryChange: (String) -> Unit
//) {
//
//    Box(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(horizontal = 16.dp, vertical = 12.dp)
//            .clip(RoundedCornerShape(12.dp))
//            .background(Color(0xFF1C1C1C))
//            .border(
//                width = 1.dp,
//                color = if (query.isNotBlank())
//                    Color.Red.copy(alpha = 0.6f)
//                else
//                    Color.Transparent,
//                shape = RoundedCornerShape(12.dp)
//            )
//            .height(56.dp),
//        contentAlignment = Alignment.Center
//    ) {
//
//        Row(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(horizontal = 14.dp),
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//
//            Icon(
//                Icons.Default.Search,
//                contentDescription = null,
//                tint = Color.LightGray,
//                modifier = Modifier.size(22.dp)
//            )
//
//            Spacer(modifier = Modifier.width(10.dp))
//
//            BasicTextField(
//                value = query,
//                onValueChange = onQueryChange,
//                singleLine = true,
//                textStyle = TextStyle(
//                    color = Color.White,
//                    fontSize = 16.sp
//                ),
//                modifier = Modifier.weight(1f),
//                cursorBrush = SolidColor(Color.Red),
//                decorationBox = { innerTextField ->
//
//                    if (query.isEmpty()) {
//                        Text(
//                            text = "Search tv shows...",
//                            color = Color.Gray,
//                            fontSize = 15.sp
//                        )
//                    }
//
//                    innerTextField()
//                }
//            )
//
//            AnimatedVisibility(
//                visible = query.isNotEmpty()
//            ) {
//
//                Box(
//                    modifier = Modifier
//                        .size(22.dp)
//                        .clip(CircleShape)
//                        .background(Color.DarkGray)
//                        .clickable {
//                            onQueryChange("")
//                        },
//                    contentAlignment = Alignment.Center
//                ) {
//
//                    Icon(
//                        Icons.Default.Close,
//                        contentDescription = null,
//                        tint = Color.White,
//                        modifier = Modifier.size(14.dp)
//                    )
//                }
//            }
//        }
//    }
//}
//
//@Composable
//fun TvSearchItem(
//    tv: Tv,
//    onTvClick: (Tv) -> Unit
//) {
//    Card(
//        modifier = Modifier
//            .fillMaxWidth()
//            .height(140.dp)
//            .clickable { onTvClick(tv) },
//        shape = RoundedCornerShape(20.dp),
//        elevation = CardDefaults.cardElevation(6.dp)
//    ) {
//
//        Row {
//
//            // Poster
//            AsyncImage(
//                model = when {
//                    tv.posterPath.isNotBlank() ->
//                        "${K.BASE_IMAGE_URL}${tv.posterPath}"
//
//                    tv.backdropPath.isNotBlank() ->
//                        "${K.BASE_IMAGE_URL}${tv.backdropPath}"
//
//                    else -> R.drawable.bg_image_movie
//                },
//                contentDescription = null,
//                contentScale = ContentScale.Crop,
//                modifier = Modifier
//                    .width(100.dp)
//                    .fillMaxHeight()
//            )
//
//            Box(
//                modifier = Modifier
//                    .weight(1f)
//                    .fillMaxHeight()
//            ) {
//                // Background gradient
//                Box(
//                    modifier = Modifier
//                        .matchParentSize()
//                        .background(
//                            brush = Brush.horizontalGradient(
//                                colors = listOf(
//                                    Color.Black.copy(0.85f),
//                                    Color.Black.copy(0.6f),
//                                    Color.Black.copy(0.3f),
//                                )
//                            )
//                        )
//                )
//                // Content
//                Column(
//                    modifier = Modifier
//                        .padding(12.dp)
//                        .fillMaxSize()
//                ) {
//
//                    // Title
//                    Text(
//                        text = tv.name,
//                        style = MaterialTheme.typography.titleMedium,
//                        fontWeight = FontWeight.Bold,
//                        maxLines = 2
//                    )
//
//                    Spacer(Modifier.height(6.dp))
//
//                    // Rating + Language
//                    Row(verticalAlignment = Alignment.CenterVertically) {
//
//                        Icon(
//                            Icons.Default.Star,
//                            contentDescription = null,
//                            tint = Color.Yellow,
//                            modifier = Modifier.size(16.dp)
//                        )
//
//                        Text(
//                            text = " ${String.format("%.1f", tv.voteAverage)}",
//                            style = MaterialTheme.typography.bodySmall
//                        )
//
//                        Spacer(Modifier.width(8.dp))
//
//                        Text(
//                            text = tv.originalLanguage.uppercase(),
//                            style = MaterialTheme.typography.bodySmall,
//                            color = Color.Gray
//                        )
//                    }
//
//                    Spacer(Modifier.height(6.dp))
//
//                    // 🎭 Genre Chip
//                    Row(
//                        horizontalArrangement = Arrangement.spacedBy(6.dp)
//                    ) {
//                        tv.genreIds.take(3).forEach { genreId ->
//                            val genreName =
//                                GenreConstants.getGenreNameById(genreId)
//
//                            Box(
//                                modifier = Modifier
//                                    .background(
//                                        Color.White.copy(0.2f),
//                                        RoundedCornerShape(50)
//                                    )
//                                    .padding(horizontal = 10.dp, vertical = 4.dp)
//                            ) {
//                                Text(
//                                    text = genreName,
//                                    color = Color.White,
//                                    style = MaterialTheme.typography.bodySmall
//                                )
//                            }
//                        }
//                    }
//                    Spacer(Modifier.height(6.dp))
//
//                    // 🧾 Overview (short)
//                    Text(
//                        text = tv.overview,
//                        maxLines = 2,
//                        overflow = TextOverflow.Ellipsis,
//                        style = MaterialTheme.typography.bodySmall,
//                        color = Color.Gray
//                    )
//                }
//
//            }
//
//
//        }
//    }
//}
//
//@Composable
//fun EmptySearchView(
//    query: String
//) {
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(Color.Black)
//            .padding(horizontal = 32.dp),
//        verticalArrangement = Arrangement.Center,
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//
//        Icon(
//            imageVector = Icons.Default.SearchOff,
//            contentDescription = null,
//            tint = Color.Red,
//            modifier = Modifier.size(90.dp)
//        )
//
//        Spacer(modifier = Modifier.height(24.dp))
//
//        Text(
//            text = "Your search didn't have any matches.",
//            color = Color.White,
//            fontSize = 20.sp,
//            fontWeight = FontWeight.Bold,
//            textAlign = TextAlign.Center
//        )
//
//        Spacer(modifier = Modifier.height(12.dp))
//
//        Text(
//            text = "Try different keywords for \"$query\"",
//            color = Color.Gray,
//            fontSize = 14.sp,
//            textAlign = TextAlign.Center
//        )
//
//        Spacer(modifier = Modifier.height(32.dp))
//
//        Box(
//            modifier = Modifier
//                .clip(RoundedCornerShape(8.dp))
//                .background(Color(0xFF1C1C1C))
//                .padding(horizontal = 18.dp, vertical = 10.dp)
//        ) {
//
//            Text(
//                text = "Explore Popular Titles",
//                color = Color.White,
//                fontWeight = FontWeight.Medium
//            )
//        }
//    }
//}
//
//@Composable
//fun NetflixLoadingBar() {
//
//    val infiniteTransition = rememberInfiniteTransition(label = "")
//
//    val progress by infiniteTransition.animateFloat(
//        initialValue = 0f,
//        targetValue = 1f,
//        animationSpec = infiniteRepeatable(
//            animation = tween(1200, easing = LinearEasing)
//        ),
//        label = ""
//    )
//
//    Box(
//        modifier = Modifier
//            .fillMaxWidth()
//            .height(3.dp)
//            .background(Color.DarkGray.copy(alpha = 0.4f))
//    ) {
//
//        Box(
//            modifier = Modifier
//                .fillMaxHeight()
//                .fillMaxWidth(progress)
//                .background(Color.Red)
//        )
//    }
//}