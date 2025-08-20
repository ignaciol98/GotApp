package com.example.gameofthrones.ui.theme.houses

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.gameofthrones.R
import com.example.gameofthrones.model.House
import java.text.Normalizer
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HouseDetailScreen(
    house: House,
    isFavorite: Boolean = false,
    onToggleFavorite: (House) -> Unit = {},
    onBack: () -> Unit = {}
) {
    val ctx = LocalContext.current

    fun normalizeName(input: String): String {
        val noAccents = Normalizer.normalize(input, Normalizer.Form.NFD)
            .replace("\\p{InCombiningDiacriticalMarks}+".toRegex(), "")
        return noAccents
            .lowercase(Locale.ROOT)
            .replace("[^a-z0-9]+".toRegex(), "_")
            .trim('_')
    }

    val triedNames = remember(house) {
        listOfNotNull(
            house.id.takeIf { it.isNotBlank() },
            normalizeName(house.name).takeIf { it.isNotBlank() },
            "house_${normalizeName(house.name)}".takeIf { it.isNotBlank() }
        ).distinct()
    }

    val drawableId = remember(triedNames, ctx.packageName) {
        var found = 0
        for (name in triedNames) {
            val res = ctx.resources.getIdentifier(name, "drawable", ctx.packageName)
            if (res != 0) {
                found = res
                break
            }
        }
        Log.d("HouseDetail", "Tried names=$triedNames -> drawableId=$found")
        found
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(house.name) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
                actions = {
                    IconButton(onClick = { onToggleFavorite(house) }) {
                        Icon(
                            imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                            contentDescription = if (isFavorite) "Quitar de favoritos" else "Agregar a favoritos"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.6f)
                )
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            // 🎨 Fondo de pergamino
            Image(
                painter = painterResource(R.drawable.bg_parchment),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            // Contenido principal
            Column(
                modifier = Modifier
                    .padding(padding)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(Modifier.height(8.dp))

                // Escudo
                if (drawableId != 0) {
                    Image(
                        painter = painterResource(id = drawableId),
                        contentDescription = "${house.name} escudo",
                        modifier = Modifier
                            .size(160.dp)
                            .clip(RoundedCornerShape(12.dp)),
                        contentScale = ContentScale.Crop
                    )
                } else if (!house.sigilUrl.isNullOrBlank()) {
                    AsyncImage(
                        model = house.sigilUrl,
                        contentDescription = "${house.name} escudo",
                        placeholder = painterResource(id = R.drawable.question),
                        error = painterResource(id = R.drawable.question),
                        modifier = Modifier
                            .size(160.dp)
                            .clip(RoundedCornerShape(12.dp)),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Image(
                        painter = painterResource(id = R.drawable.question),
                        contentDescription = "Sin escudo",
                        modifier = Modifier
                            .size(160.dp)
                            .clip(RoundedCornerShape(12.dp)),
                        contentScale = ContentScale.Crop
                    )
                }

                Spacer(Modifier.height(12.dp))

                Text(text = house.name, style = MaterialTheme.typography.headlineMedium)
                Text(text = house.motto ?: "Sin lema", style = MaterialTheme.typography.bodyMedium)

                Spacer(Modifier.height(18.dp))

                Text(
                    text = "Descripción:",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.fillMaxWidth()
                )
                Text(text = house.description, style = MaterialTheme.typography.bodyMedium)

                Spacer(Modifier.height(18.dp))

                Text(
                    text = "Personajes destacados:",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(18.dp))

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    house.notableCharacters.forEach { character ->
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            if (character.imageUrl.isNotBlank()) {
                                AsyncImage(
                                    model = character.imageUrl,
                                    contentDescription = character.name,
                                    placeholder = painterResource(id = R.drawable.question),
                                    error = painterResource(id = R.drawable.question),
                                    modifier = Modifier
                                        .size(180.dp)
                                        .clip(RoundedCornerShape(8.dp)),
                                    contentScale = ContentScale.Crop
                                )
                            } else {
                                Image(
                                    painter = painterResource(id = R.drawable.question),
                                    contentDescription = character.name,
                                    modifier = Modifier
                                        .size(120.dp)
                                        .clip(RoundedCornerShape(8.dp)),
                                    contentScale = ContentScale.Crop
                                )
                            }
                            Spacer(Modifier.height(10.dp))
                            Text(text = character.name, style = MaterialTheme.typography.titleSmall)
                            Spacer(Modifier.height(9.dp))
                            Text(
                                text = character.biography,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                }
            }
        }
    }
}
