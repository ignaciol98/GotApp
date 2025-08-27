package com.example.gameofthrones.ui.theme.houses

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.gameofthrones.R
import com.example.gameofthrones.model.House
import androidx.compose.foundation.clickable
import java.util.Locale


@Composable
private fun FadingAsyncImage(
    model: Any?,
    contentDesc: String?,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop
) {
    var loaded by remember { mutableStateOf(false) }
    val alpha by animateFloatAsState(targetValue = if (loaded) 1f else 0f, label = "img_fade")

    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(model)
            .crossfade(true)
            .build(),
        contentDescription = contentDesc,
        modifier = modifier.graphicsLayer { this.alpha = alpha },
        contentScale = contentScale,
        onSuccess = { loaded = true },
        onLoading = { loaded = false },
        onError = { loaded = true }
    )
}

@Composable
fun SearchableHouseListScreen(
    houses: List<House>,
    onHouseClick: (String) -> Unit
) {
    var query by remember { mutableStateOf(TextFieldValue("")) }

    val filtered = remember(houses, query) {
        houses
            .filter { it.name.contains(query.text, ignoreCase = true) }
            .sortedBy { it.name }
    }

    val context = LocalContext.current

    Column(modifier = Modifier.fillMaxSize()) {
        OutlinedTextField(
            value = query,
            onValueChange = { query = it },
            placeholder = { Text("Buscar casa…") },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .testTag("search") // 👈 para UI tests
        )

        if (filtered.isEmpty()) {

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Sin resultados",
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            return@Column
        }

        LazyColumn(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(filtered) { house ->
                val drawableId = remember(house.id) {
                    context.resources.getIdentifier(
                        house.id.lowercase(Locale.ROOT),
                        "drawable",
                        context.packageName
                    )
                }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("house-${house.id}") // 👈 para UI tests
                        .then(Modifier)
                        .clickable { onHouseClick(house.id) },
                    elevation = CardDefaults.cardElevation(0.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.Transparent,
                        contentColor = MaterialTheme.colorScheme.onBackground
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .padding(12.dp)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Prioridad 1: drawable local
                        if (drawableId != 0) {
                            Image(
                                painter = painterResource(id = drawableId),
                                contentDescription = "${house.name} escudo",
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(RoundedCornerShape(6.dp)),
                                contentScale = ContentScale.Crop
                            )
                        } else if (!house.sigilUrl.isNullOrBlank()) {
                            // Prioridad 2: imagen remota (con fade-in)
                            FadingAsyncImage(
                                model = house.sigilUrl,
                                contentDesc = "${house.name} escudo",
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(RoundedCornerShape(6.dp)),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            // Fallback
                            Image(
                                painter = painterResource(id = R.drawable.question),
                                contentDescription = "sin escudo",
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(RoundedCornerShape(6.dp)),
                                contentScale = ContentScale.Crop
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column {
                            Text(
                                text = house.name,
                                style = MaterialTheme.typography.titleMedium
                            )
                            Text(
                                text = house.motto ?: "Sin lema",
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                }
            }
        }
    }
}
