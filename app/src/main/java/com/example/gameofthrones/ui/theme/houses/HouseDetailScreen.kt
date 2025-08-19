package com.example.gameofthrones.ui.theme.houses

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.gameofthrones.R
import com.example.gameofthrones.model.House
import java.util.*

@Composable
fun HouseDetailScreen(
    house: House
) {
    val context = LocalContext.current

    val drawableId = remember(house.id) {
        context.resources.getIdentifier(
            house.id.lowercase(Locale.ROOT),
            "drawable",
            context.packageName
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        if (!house.sigilUrl.isNullOrBlank()) {
            Image(
                painter = rememberAsyncImagePainter(model = house.sigilUrl),
                contentDescription = "${house.name} escudo",
                modifier = Modifier
                    .size(128.dp)
                    .align(Alignment.CenterHorizontally)
                    .clip(RoundedCornerShape(8.dp))
            )
        } else if (drawableId != 0) {
            Image(
                painter = painterResource(id = drawableId),
                contentDescription = "${house.name} escudo",
                modifier = Modifier
                    .size(128.dp)
                    .align(Alignment.CenterHorizontally)
                    .clip(RoundedCornerShape(8.dp))
            )
        }

        Spacer(Modifier.height(16.dp))

        Text(
            text = house.name,
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Text(
            text = house.motto ?: "Sin lema",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(Modifier.height(24.dp))

        Text(
            text = "Descripción:",
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            text = house.description,
            style = MaterialTheme.typography.bodySmall
        )

        Spacer(Modifier.height(24.dp))

        Text(
            text = "Personajes destacados:",
            style = MaterialTheme.typography.titleMedium
        )
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.padding(start = 8.dp, top = 8.dp)
        ) {
            house.notableCharacters.forEach { character ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Si el character tiene imageUrl la mostramos, sino placeholder
                    if (character.imageUrl.isNotBlank()) {
                        Image(
                            painter = rememberAsyncImagePainter(model = character.imageUrl),
                            contentDescription = character.name,
                            modifier = Modifier
                                .size(56.dp)
                                .clip(RoundedCornerShape(8.dp))
                        )
                    } else {
                        Image(
                            painter = painterResource(id = R.drawable.question),
                            contentDescription = character.name,
                            modifier = Modifier
                                .size(56.dp)
                                .clip(RoundedCornerShape(8.dp))
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(text = character.name, style = MaterialTheme.typography.titleSmall)
                        Text(text = character.biography, style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
        }
    }
}
