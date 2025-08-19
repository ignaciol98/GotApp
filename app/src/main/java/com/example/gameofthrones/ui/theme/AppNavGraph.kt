package com.example.gameofthrones.ui.theme

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Map
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.gameofthrones.model.House
import com.example.gameofthrones.ui.theme.houses.SearchableHouseListScreen
import com.example.gameofthrones.ui.theme.houses.HouseDetailScreen
import com.example.gameofthrones.ui.theme.map.SimplifiedMapScreen
import com.example.gameofthrones.ui.theme.map.RegionViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavGraph(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    // ViewModel de casas
    val viewModel: HousesViewModel = hiltViewModel()
    val houses by viewModel.houses.collectAsState(initial = emptyList())

    NavHost(navController, startDestination = "list", modifier = modifier) {
        // Lista y buscador con AppBar que incluye botón de mapa
        composable("list") {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text("Casas de Poniente") },
                        actions = {
                            IconButton(onClick = { navController.navigate("map") }) {
                                Icon(
                                    imageVector = Icons.Default.Map,
                                    contentDescription = "Ver mapa"
                                )
                            }
                        }
                    )
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

        // Detalle de casa
        composable("detail/{houseId}") { backStackEntry ->
            val houseId = backStackEntry.arguments!!.getString("houseId")!!
            val house: House? = houses.firstOrNull { it.id == houseId }
            if (house != null) {
                HouseDetailScreen(house = house)
            } else {
                Text(
                    text = "Casa no encontrada",
                    modifier = Modifier
                        .fillMaxSize()
                        .wrapContentSize(Alignment.Center)
                )
            }
        }

        // Mapa interactivo
        composable("map") {
            val regionVM: RegionViewModel = hiltViewModel()
            SimplifiedMapScreen(
                regions = regionVM.regions,
                houses = houses,
                onHouseClick = { id -> navController.navigate("detail/$id") }
            )
        }
    }
}
