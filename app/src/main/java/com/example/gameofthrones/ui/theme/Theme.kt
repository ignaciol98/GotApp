package com.example.gameofthrones.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme

// Theme adaptado a los colores definidos en Color.kt (RegalBackground, RegalAccent, RegalPaper, OnPrimary)
// Si querés otros roles (p.ej. usar distintos colores para secondary, surface, etc.) decímelo y lo ajusto.

private val LightColors = lightColorScheme(
    primary = RegalAccent,
    onPrimary = OnPrimary,
    secondary = RegalPaper,
    onSecondary = OnPrimary,
    background = RegalPaper,
    onBackground = OnPrimary,
    surface = RegalPaper,
    onSurface = OnPrimary,
    error = Color(0xFFB00020),
    onError = Color.White
)

private val DarkColors = darkColorScheme(
    primary = RegalAccent,
    onPrimary = OnPrimary, // texto sobre el acento (en dorado). Si querés distinto para dark mode, lo cambiamos.
    secondary = RegalPaper,
    onSecondary = RegalBackground,
    background = RegalBackground,
    onBackground = RegalPaper,
    surface = RegalBackground,
    onSurface = RegalPaper,
    error = Color(0xFFCF6679),
    onError = Color.Black
)

@Composable
fun GameOfThronesTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColors else LightColors

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography, // utiliza Typography definido en Type.kt (ajustar si es de material2)
        shapes = Shapes,         // utiliza Shapes definido en Shape.kt (ajustar si es de material2)
        content = content
    )
}
