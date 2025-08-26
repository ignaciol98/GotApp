package com.example.gameofthrones.ui.theme.houses

import android.util.Log
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.gameofthrones.R
import com.example.gameofthrones.model.House
import com.example.gameofthrones.ui.theme.houseColorSchemeFor
import java.text.Normalizer
import java.util.Locale

// ---------- Helper: imagen con fade-in suave (Coil + alpha animada) ----------
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
            .crossfade(true) // fade de Coil
            .build(),
        contentDescription = contentDesc,
        modifier = modifier.graphicsLayer { this.alpha = alpha }, // alpha animado propio
        contentScale = contentScale,
        onSuccess = { loaded = true },
        onLoading = { loaded = false },
        onError = { loaded = true } // para no dejarla invisible si falla
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HouseDetailScreen(
    house: House,
    isFavorite: Boolean = false,
    onToggleFavorite: (House) -> Unit = {},
    onBack: () -> Unit = {}
) {
    val ctx = LocalContext.current

    // --- Util para buscar drawable local por nombre/id ---
    fun normalizeName(input: String): String {
        val noAccents = Normalizer.normalize(input, Normalizer.Form.NFD)
            .replace("\\p{InCombiningDiacriticalMarks}+".toRegex(), "")
        return noAccents.lowercase(Locale.ROOT)
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
                found = res; break
            }
        }
        Log.d("HouseDetail", "Tried names=$triedNames -> drawableId=$found")
        found
    }

    // --- Tema por casa sólo para esta pantalla ---
    val scheme = houseColorSchemeFor(house)

    MaterialTheme(colorScheme = scheme, typography = MaterialTheme.typography) {

        val isDark = MaterialTheme.colorScheme.background.luminance() < 0.5f
        val topBarContainer =
            if (isDark) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.surfaceVariant
        val onBar = MaterialTheme.colorScheme.onSurface

        // --- Animaciones del botón de favorito ---
        val scale by animateFloatAsState(
            targetValue = if (isFavorite) 1.25f else 1f,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessMedium
            ),
            label = "fav_scale"
        )
        val targetTint = if (isFavorite) MaterialTheme.colorScheme.primary
        else MaterialTheme.colorScheme.onSurfaceVariant
        val tint by animateColorAsState(targetValue = targetTint, label = "fav_tint")

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(house.name, color = onBar) },
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(
                                imageVector = Icons.Filled.ArrowBack,
                                contentDescription = "Volver",
                                tint = onBar
                            )
                        }
                    },
                    actions = {
                        IconButton(onClick = { onToggleFavorite(house) }) {
                            Icon(
                                imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                                contentDescription = if (isFavorite) "Quitar de favoritos" else "Agregar a favoritos",
                                tint = tint,
                                modifier = Modifier.scale(scale)
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = topBarContainer,
                        titleContentColor = onBar,
                        navigationIconContentColor = onBar,
                        actionIconContentColor = onBar
                    )
                )
            }
        ) { padding ->
            Box(modifier = Modifier.fillMaxSize()) {
                // 🎨 Fondo pergamino con opacidad según tema
                val parchmentAlpha = if (isDark) 0.08f else 0.22f
                Image(
                    painter = painterResource(R.drawable.bg_parchment),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize()
                        .graphicsLayer { alpha = parchmentAlpha },
                    contentScale = ContentScale.Crop
                )

                // Contenido
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
                        FadingAsyncImage(
                            model = house.sigilUrl,
                            contentDesc = "${house.name} escudo",
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

                    // Nombre de la casa
                    Text(text = house.name, style = MaterialTheme.typography.headlineMedium)

                    Spacer(Modifier.height(8.dp))

                    // 👉 Lema en chip justo debajo del nombre
                    if (!house.motto.isNullOrBlank()) {
                        Surface(
                            color = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary,
                            shape = RoundedCornerShape(50)
                        ) {
                            Text(
                                text = house.motto!!,
                                style = MaterialTheme.typography.labelMedium,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }

                    Spacer(Modifier.height(18.dp))

                    Text(
                        text = "Descripción:",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Text(text = house.description, style = MaterialTheme.typography.bodyMedium)

                    Spacer(Modifier.height(18.dp))

                    Text(
                        text = "Personajes destacados:",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary,
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
                                    FadingAsyncImage(
                                        model = character.imageUrl,
                                        contentDesc = character.name,
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
                                Text(
                                    text = character.name,
                                    style = MaterialTheme.typography.titleSmall
                                )
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
}
