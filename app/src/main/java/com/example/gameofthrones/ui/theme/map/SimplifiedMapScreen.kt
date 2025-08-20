package com.example.gameofthrones.ui.theme.map

import android.content.res.Resources
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import coil.compose.AsyncImage
import androidx.compose.ui.unit.dp
import com.example.gameofthrones.R
import com.example.gameofthrones.model.Region
import com.example.gameofthrones.model.House
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SimplifiedMapScreen(
    regions: List<Region>,
    houses: List<House>,
    onHouseClick: (String) -> Unit
) {
    var selectedRegion by remember { mutableStateOf<Region?>(null) }

    val config = LocalConfiguration.current
    val screenWidth = config.screenWidthDp.dp
    val screenHeight = config.screenHeightDp.dp

    Box(modifier = Modifier.fillMaxSize()) {
        // Fondo (mapa)
        Image(
            painter = painterResource(id = R.drawable.ic_map_poniente),
            contentDescription = "Mapa de Poniente",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Zonas "tocables" (ajusta offsets/tamaños según tu imagen)
        Box(
            modifier = Modifier
                .offset(x = screenWidth * 0.13f, y = screenHeight * 0.12f)
                .size(width = screenWidth * 0.80f, height = screenHeight * 0.25f)
                .clickable { selectedRegion = regions.firstOrNull { it.id == "north" } }
                .background(Color.Transparent)
        )
        Box(
            modifier = Modifier
                .offset(x = screenWidth * 0.53f, y = screenHeight * 0.37f)
                .size(width = screenWidth * 0.37f, height = screenHeight * 0.10f)
                .clickable { selectedRegion = regions.firstOrNull { it.id == "vale" } }
                .background(Color.Transparent)
        )
        Box(
            modifier = Modifier
                .offset(x = screenWidth * 0.28f, y = screenHeight * 0.37f)
                .size(width = screenWidth * 0.25f, height = screenHeight * 0.10f)
                .clickable { selectedRegion = regions.firstOrNull { it.id == "riverlands" } }
                .background(Color.Transparent)
        )
        Box(
            modifier = Modifier
                .offset(x = screenWidth * 0.088f, y = screenHeight * 0.37f)
                .size(width = screenWidth * 0.19f, height = screenHeight * 0.10f)
                .clickable { selectedRegion = regions.firstOrNull { it.id == "iron_islands" } }
                .background(Color.Transparent)
        )
        Box(
            modifier = Modifier
                .offset(x = screenWidth * 0.099f, y = screenHeight * 0.49f)
                .size(width = screenWidth * 0.30f, height = screenHeight * 0.20f)
                .clickable { selectedRegion = regions.firstOrNull { it.id == "westerlands" } }
                .background(Color.Transparent)
        )
        Box(
            modifier = Modifier
                .offset(x = screenWidth * 0.37f, y = screenHeight * 0.85f)
                .size(width = screenWidth * 0.60f, height = screenHeight * 0.10f)
                .clickable { selectedRegion = regions.firstOrNull { it.id == "dorne" } }
                .background(Color.Transparent)
        )
        Box(
            modifier = Modifier
                .offset(x = screenWidth * 0.099f, y = screenHeight * 0.69f)
                .size(width = screenWidth * 0.27f, height = screenHeight * 0.25f)
                .clickable { selectedRegion = regions.firstOrNull { it.id == "reach" } }
                .background(Color.Transparent)
        )
        Box(
            modifier = Modifier
                .offset(x = screenWidth * 0.37f, y = screenHeight * 0.69f)
                .size(width = screenWidth * 0.60f, height = screenHeight * 0.16f)
                .clickable { selectedRegion = regions.firstOrNull { it.id == "stormlands" } }
                .background(Color.Transparent)
        )
        Box(
            modifier = Modifier
                .offset(x = screenWidth * 0.40f, y = screenHeight * 0.47f)
                .size(width = screenWidth * 0.57f, height = screenHeight * 0.22f)
                .clickable { selectedRegion = regions.firstOrNull { it.id == "crownlands" } }
                .background(Color.Transparent)
        )

        // BottomSheet con casas de la región seleccionada
        selectedRegion?.let { region ->
            val regionHouses = houses.filter { it.id in region.houseIds }
            val context = LocalContext.current

            ModalBottomSheet(onDismissRequest = { selectedRegion = null }) {
                Text(
                    text = region.name,
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(16.dp)
                )

                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(regionHouses) { house ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onHouseClick(house.id) }
                                .padding(8.dp)
                        ) {
                            // resolver drawable local (convención: id normalizado a lowercase)
                            val drawableId = remember(house.id) {
                                context.resources.getIdentifier(
                                    house.id.lowercase(Locale.getDefault()),
                                    "drawable",
                                    context.packageName
                                )
                            }

                            // Primero: drawable local si existe
                            if (drawableId != 0) {
                                Image(
                                    painter = painterResource(id = drawableId),
                                    contentDescription = "${house.name} escudo",
                                    modifier = Modifier
                                        .size(48.dp)
                                        .clip(RoundedCornerShape(4.dp)),
                                    contentScale = ContentScale.Crop
                                )
                            } else if (!house.sigilUrl.isNullOrBlank()) {
                                // Segundo: url remota con Coil AsyncImage
                                AsyncImage(
                                    model = house.sigilUrl,
                                    contentDescription = "${house.name} escudo",
                                    placeholder = painterResource(id = R.drawable.question),
                                    error = painterResource(id = R.drawable.question),
                                    modifier = Modifier
                                        .size(48.dp)
                                        .clip(RoundedCornerShape(4.dp)),
                                    contentScale = ContentScale.Crop
                                )
                            } else {
                                // Fallback
                                Image(
                                    painter = painterResource(id = R.drawable.question),
                                    contentDescription = "sin escudo",
                                    modifier = Modifier
                                        .size(48.dp)
                                        .clip(RoundedCornerShape(4.dp)),
                                    contentScale = ContentScale.Crop
                                )
                            }

                            Spacer(Modifier.width(12.dp))

                            Column {
                                Text(house.name, style = MaterialTheme.typography.titleMedium)
                                Text(
                                    house.motto ?: "Sin lema",
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
