package com.example.tmdbapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.tmdbapp.ui.navigation.BottomNavItem
import com.example.tmdbapp.ui.navigation.MovieNavigationGraph
import com.example.tmdbapp.ui.navigation.Route
import com.example.tmdbapp.ui.theme.TmdbAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TmdbAppTheme {
//                HomeScreen {  }
                App()
            }
        }
    }

    @Composable
    fun App() {
        val items = listOf(
            BottomNavItem.Movie,
            BottomNavItem.Tv,
        )

        val navController = rememberNavController()

        val navBackStackEntry by navController.currentBackStackEntryAsState()

        val currentRoute = navBackStackEntry
            ?.destination
            ?.route

        val showBottomBar =
            currentRoute?.startsWith(Route.DetailMovie.route) != true &&
                    currentRoute?.startsWith(Route.DetailTv.route) != true &&
                    currentRoute?.startsWith(Route.SearchTv.route) != true &&
                    currentRoute?.startsWith(Route.SearchMovie.route) != true &&
                    currentRoute?.startsWith(Route.Profile.route) != true &&
                    currentRoute?.startsWith(Route.Favorite.route) != true

        Scaffold(
            modifier = Modifier.fillMaxSize(),
            bottomBar = {
                if (showBottomBar) {
                    NavigationBar(
                        containerColor = Color.Black,
                        modifier = Modifier.drawBehind {
                            // Garis pemisah tipis di paling atas bar
                            drawLine(
                                color = Color.White.copy(alpha = 0.1f),
                                start = Offset(0f, 0f),
                                end = Offset(size.width, 0f),
                                strokeWidth = 1.dp.toPx()
                            )
                        },
                        tonalElevation = 0.dp
                    ) {
                        items.forEach { item ->
                            val isSelected = currentRoute == item.route

                            NavigationBarItem(
                                selected = isSelected,
                                onClick = {
                                    navController.navigate(item.route) {
                                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                },
                                icon = {
                                    // Gunakan Column agarbisa menaruh garis di bawah icon
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Icon(
                                            imageVector = item.icon,
                                            contentDescription = item.title,
                                            modifier = Modifier.size(24.dp)
                                        )

                                        // Garis Merah (Hanya muncul jika dipilih)
                                        if (isSelected) {
                                            Spacer(modifier = Modifier.height(4.dp)) // Jarak antara ikon dan garis
                                            Box(
                                                modifier = Modifier
                                                    .width(16.dp) // Lebar garis merah
                                                    .height(2.dp) // Ketebalan garis
                                                    .clip(RoundedCornerShape(2.dp))
                                                    .background(Color(0xFFE50914))
                                            )
                                        } else {
                                            // Spacer transparan agar posisi icon tetap stabil saat tidak terpilih
                                            Spacer(modifier = Modifier.height(6.dp))
                                        }
                                    }
                                },
                                label = {
                                    Text(
                                        text = item.title,
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                            fontSize = 10.sp
                                        )
                                    )
                                },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = Color.White,
                                    selectedTextColor = Color.White,
                                    unselectedIconColor = Color.Gray,
                                    unselectedTextColor = Color.Gray,
                                    indicatorColor = Color.Transparent // Wajib transparan
                                )
                            )
                        }
                    }
                }
            }
        ) { padding ->
            MovieNavigationGraph(
                navController = navController,
                modifier = Modifier.padding(padding)
            )
        }
    }

}
