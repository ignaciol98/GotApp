package com.example.gameofthrones.ui.theme.settings

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.BrightnessAuto
import androidx.compose.material.icons.outlined.DarkMode
import androidx.compose.material.icons.outlined.LightMode
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.gameofthrones.ui.theme.ThemeMode
import com.example.gameofthrones.ui.theme.ThemeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    themeVM: ThemeViewModel,
    onBack: () -> Unit
) {
    val mode by themeVM.mode.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Ajustes") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Outlined.DarkMode, contentDescription = "Volver") // ícono cualquiera
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            Text("Tema de la app", style = MaterialTheme.typography.titleMedium)

            ThemeOptionRow(
                title = "Seguir sistema",
                icon = Icons.Outlined.BrightnessAuto,
                selected = mode == ThemeMode.SYSTEM,
                onClick = { themeVM.setMode(ThemeMode.SYSTEM) }
            )

            ThemeOptionRow(
                title = "Claro",
                icon = Icons.Outlined.LightMode,
                selected = mode == ThemeMode.LIGHT,
                onClick = { themeVM.setMode(ThemeMode.LIGHT) }
            )

            ThemeOptionRow(
                title = "Oscuro",
                icon = Icons.Outlined.DarkMode,
                selected = mode == ThemeMode.DARK,
                onClick = { themeVM.setMode(ThemeMode.DARK) }
            )
        }
    }
}

@Composable
private fun ThemeOptionRow(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    selected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        tonalElevation = if (selected) 2.dp else 0.dp,
        shape = MaterialTheme.shapes.medium,
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, contentDescription = null)
            Spacer(Modifier.width(12.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.weight(1f)
            )
            RadioButton(selected = selected, onClick = onClick)
        }
    }
}
