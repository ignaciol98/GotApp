package com.example.gameofthrones.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.gameofthrones.ui.theme.houses.HouseDetailScreen
import com.example.gameofthrones.ui.theme.houses.SearchableHouseListScreen
import com.example.gameofthrones.ui.theme.map.RegionViewModel
import com.example.gameofthrones.ui.theme.map.SimplifiedMapScreen
import com.example.gameofthrones.ui.theme.settings.SettingsScreen

// Accompanist Navigation-Animation
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController

// Animations
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.AnimatedContentTransitionScope

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun AppNavGraph(modifier: Modifier = Modifier) {
    val navController = rememberAnimatedNavController()

    // ViewModel de casas
    val viewModel: HousesViewModel = hiltViewModel()
    val houses by viewModel.houses.collectAsState(initial = emptyList())

    AnimatedNavHost(
        navController = navController,
        startDestination = "list",
        modifier = modifier,
        enterTransition = {
            fadeIn() + slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left)
        },
        exitTransition = {
            fadeOut() + slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Left)
        },
        popEnterTransition = {
            fadeIn() + slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Right)
        },
        popExitTransition = {
            fadeOut() + slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right)
        }
    ) {

        // LISTA DE CASAS
        composable("list") {
            val isDark = isSystemInDarkTheme()
            val barContainer =
                if (isDark) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.surfaceVariant
            val onBar = MaterialTheme.colorScheme.onSurface

            // VM de tema (para usar en Settings si quisieras mostrar estado aquí también)
            val themeVM: ThemeViewModel = hiltViewModel()

            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text("Casas de Poniente", color = onBar) },
                        actions = {
                            // Mapa
                            IconButton(onClick = { navController.navigate("map") }) {
                                Icon(
                                    imageVector = Icons.Filled.Map,
                                    contentDescription = "Ver mapa",
                                    tint = onBar
                                )
                            }
                            // Ajustes
                            IconButton(onClick = { navController.navigate("settings") }) {
                                Icon(
                                    imageVector = Icons.Outlined.Settings,
                                    contentDescription = "Ajustes",
                                    tint = onBar
                                )
                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = barContainer,
                            titleContentColor = onBar,
                            navigationIconContentColor = onBar,
                            actionIconContentColor = onBar
                        )
                    )
                },
                // FAB → Favoritos
                floatingActionButton = {
                    FloatingActionButton(onClick = { navController.navigate("favorites") }) {
                        Icon(Icons.Filled.Favorite, contentDescription = "Ver favoritos")
                    }
                }
            ) { innerPadding ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    SearchableHouseListScreen(
                        houses = houses,
                        onHouseClick = { id -> navController.navigate("detail/$id") }
                    )
                }
            }
        }

        // DETALLE DE CASA
        composable("detail/{houseId}") { backStackEntry ->
            val houseId = backStackEntry.arguments?.getString("houseId") ?: return@composable

            val detailVM: HouseDetailViewModel = hiltViewModel()
            val houseState by detailVM.house.collectAsState()
            val isFav by detailVM.isFavorite.collectAsState()

            LaunchedEffect(houseId) {
                detailVM.load(houseId)
            }

            if (houseState != null) {
                HouseDetailScreen(
                    house = houseState!!,
                    isFavorite = isFav,
                    onToggleFavorite = { detailVM.toggleFavorite(houseId) },
                    onBack = { navController.popBackStack() }
                )
            } else {
                Text(
                    text = "Cargando...",
                    modifier = Modifier
                        .fillMaxSize()
                        .wrapContentSize(Alignment.Center)
                )
            }
        }

        // MAPA
        composable("map") {
            val isDark = isSystemInDarkTheme()
            val barContainer =
                if (isDark) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.surfaceVariant
            val onBar = MaterialTheme.colorScheme.onSurface

            val regionVM: RegionViewModel = hiltViewModel()

            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text("Mapa de Poniente", color = onBar) },
                        navigationIcon = {
                            IconButton(onClick = { navController.popBackStack() }) {
                                Icon(
                                    imageVector = Icons.Filled.ArrowBack,
                                    contentDescription = "Volver",
                                    tint = onBar
                                )
                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = barContainer,
                            titleContentColor = onBar,
                            navigationIconContentColor = onBar,
                            actionIconContentColor = onBar
                        )
                    )
                }
            ) { innerPadding ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    SimplifiedMapScreen(
                        regions = regionVM.regions,
                        houses = houses,
                        onHouseClick = { id -> navController.navigate("detail/$id") }
                    )
                }
            }
        }

        // FAVORITOS
        composable("favorites") {
            val isDark = isSystemInDarkTheme()
            val barContainer =
                if (isDark) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.surfaceVariant
            val onBar = MaterialTheme.colorScheme.onSurface

            val favVM: FavoritesViewModel = hiltViewModel()
            val favoriteIds by favVM.favoriteIds.collectAsState()

            val favorites = houses
                .filter { it.id in favoriteIds.toSet() }
                .sortedBy { it.name }

            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text("Favoritos", color = onBar) },
                        navigationIcon = {
                            IconButton(onClick = { navController.popBackStack() }) {
                                Icon(
                                    imageVector = Icons.Filled.ArrowBack,
                                    contentDescription = "Volver",
                                    tint = onBar
                                )
                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = barContainer,
                            titleContentColor = onBar,
                            navigationIconContentColor = onBar,
                            actionIconContentColor = onBar
                        )
                    )
                }
            ) { innerPadding ->
                if (favorites.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Aún no tenés favoritos",
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        SearchableHouseListScreen(
                            houses = favorites,
                            onHouseClick = { id -> navController.navigate("detail/$id") }
                        )
                    }
                }
            }
        }

        // SETTINGS
        composable("settings") {
            // Si tu SettingsScreen ya provee su propia TopBar, podemos llamarla directo.
            // Le inyectamos el ThemeViewModel con Hilt.
            val themeVM: ThemeViewModel = hiltViewModel()
            SettingsScreen(
                themeVM = themeVM,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
