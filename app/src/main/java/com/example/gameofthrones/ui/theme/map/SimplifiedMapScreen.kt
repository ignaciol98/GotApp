package com.example.gameofthrones.ui.theme.map

import android.content.Context
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
import coil.compose.rememberAsyncImagePainter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.gameofthrones.R
import com.example.gameofthrones.model.Region
import com.example.gameofthrones.model.House

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SimplifiedMapScreen(
    regions: List<Region>,
    houses: List<House>,
    onHouseClick: (String) -> Unit
) {
    var selectedRegion by remember { mutableStateOf<Region?>(null) }

    // Obtener dimensiones de pantalla en dp
    val config = LocalConfiguration.current
    val screenWidth = config.screenWidthDp.dp
    val screenHeight = config.screenHeightDp.dp
    val ctx = LocalContext.current

    Box(modifier = Modifier.fillMaxSize()) {
        // Imagen de fondo (mapa de Poniente)
        Image(
            painter = painterResource(id = R.drawable.ic_map_poniente),
            contentDescription = "Mapa de Poniente",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Áreas "tocables" (ejemplos; ajusta offsets/tamaños según tu mapa)
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

        // Si hay región seleccionada, muestro sus casas en un BottomSheet
        selectedRegion?.let { region ->
            val regionHouses = houses.filter { it.id in region.houseIds }

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
                            // <-- Aquí: llamamos *directamente* a composables en el scope composable
                            val painter = if (!house.sigilUrl.isNullOrBlank()) {
                                // carga remota con Coil (devuelve Painter)
                                rememberAsyncImagePainter(model = house.sigilUrl)
                            } else {
                                // intenta drawable por id de casa; si no existe fallback
                                val drawableId = getDrawableIdForHouse(ctx, house.id)
                                if (drawableId != 0) painterResource(id = drawableId)
                                else painterResource(id = R.drawable.question)
                            }

                            Image(
                                painter = painter,
                                contentDescription = house.name,
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(RoundedCornerShape(4.dp)),
                                contentScale = ContentScale.Crop
                            )

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

/** Busca el drawable local por nombre de casa (ej: "Stark" -> R.drawable.stark) */
private fun getDrawableIdForHouse(context: Context, houseId: String): Int {
    val resName = houseId.lowercase().replace("\\s+".toRegex(), "_")
    return context.resources.getIdentifier(resName, "drawable", context.packageName)
}
