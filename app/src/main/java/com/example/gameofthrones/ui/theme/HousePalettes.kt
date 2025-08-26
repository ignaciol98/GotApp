package com.example.gameofthrones.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.gameofthrones.model.House

// -------------------- PALETAS GLOBALES (Light/Dark) --------------------

val LightColorScheme = lightColorScheme(
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

val DarkColorScheme = darkColorScheme(
    primary = RegalAccent,
    onPrimary = OnPrimary,
    secondary = RegalPaper,
    onSecondary = RegalBackground,
    background = RegalBackground,
    onBackground = RegalPaper,
    surface = RegalBackground,
    onSurface = RegalPaper,
    error = Color(0xFFCF6679),
    onError = Color.Black
)

// -------------------- PALETAS POR CASA --------------------

private data class HousePalette(
    val primary: Color,
    val onPrimary: Color = Color.White,
    val secondary: Color,
    val onSecondary: Color = Color.Black,
    val tertiary: Color? = null
)

// Tonos orientativos, elegidos para reflejar la heráldica típica.
// Ajustá a tu gusto si querés más fidelidad histórica/artística.

private val ARRYN     = HousePalette(primary = Color(0xFF2F5AA6), onPrimary = Color.White, secondary = Color(0xFFD7E3F8))
private val BARATHEON = HousePalette(primary = Color(0xFFE0B100), onPrimary = Color.Black, secondary = Color(0xFF1A1A1A))
private val BLACKWOOD = HousePalette(primary = Color(0xFF1C1C1C), onPrimary = Color(0xFFECECEC), secondary = Color(0xFFB3001B))
private val BOLTON    = HousePalette(primary = Color(0xFFC92C2C), onPrimary = Color.White, secondary = Color(0xFF8A8A8A))
private val CLEGANE   = HousePalette(primary = Color(0xFF202020), onPrimary = Color(0xFFEFC94C), secondary = Color(0xFFEFC94C))
private val DARRY     = HousePalette(primary = Color(0xFF274060), onPrimary = Color.White, secondary = Color(0xFF97BCE8))
private val FLORENT   = HousePalette(primary = Color(0xFF4A8C2C), onPrimary = Color.White, secondary = Color(0xFFC79A3A))
private val FREY      = HousePalette(primary = Color(0xFF3F6EA1), onPrimary = Color.White, secondary = Color(0xFF8CB6E8))
private val GLOVER    = HousePalette(primary = Color(0xFF7A0B0B), onPrimary = Color.White, secondary = Color(0xFFB58C6A))
private val GREYJOY   = HousePalette(primary = Color(0xFF0E0E0E), onPrimary = Color(0xFFE3C64A), secondary = Color(0xFFE3C64A))
private val HARLAW    = HousePalette(primary = Color(0xFF1B1B1B), onPrimary = Color(0xFFE0E0E0), secondary = Color(0xFF6C7A89))
private val HIGHTOWER = HousePalette(primary = Color(0xFF5C7F67), onPrimary = Color.White, secondary = Color(0xFFC9D7D1))
private val KARSTARK  = HousePalette(primary = Color(0xFF111111), onPrimary = Color(0xFFECECEC), secondary = Color(0xFF9FBED1))
private val MANDERLY  = HousePalette(primary = Color(0xFF1AAE9F), onPrimary = Color.Black, secondary = Color(0xFF0D5A52))
private val MARTELL   = HousePalette(primary = Color(0xFFF37B1D), onPrimary = Color.Black, secondary = Color(0xFFB3001B))
private val MORMONT   = HousePalette(primary = Color(0xFF2E7D32), onPrimary = Color.White, secondary = Color(0xFFA5D6A7))
private val REED      = HousePalette(primary = Color(0xFF3C6E47), onPrimary = Color.White, secondary = Color(0xFF9CCC92))
private val ROYCE     = HousePalette(primary = Color(0xFF6B4F3A), onPrimary = Color.White, secondary = Color(0xFFC4A484))
private val TARLY     = HousePalette(primary = Color(0xFF2F6B2F), onPrimary = Color.White, secondary = Color(0xFFB5CDA3))
private val TULLY     = HousePalette(primary = Color(0xFF2E5A9E), onPrimary = Color.White, secondary = Color(0xFFC0392B))
private val TYRELL    = HousePalette(primary = Color(0xFF2FA749), onPrimary = Color.White, secondary = Color(0xFFC7A33A))
private val UMBER     = HousePalette(primary = Color(0xFF6E2C00), onPrimary = Color.White, secondary = Color(0xFFC27D3B))
private val LANNISTER = HousePalette(primary = Color(0xFFB3001B), onPrimary = Color.White, secondary = Color(0xFFC79A3A))
private val STARK     = HousePalette(primary = Color(0xFF8A949E), onPrimary = Color.Black, secondary = Color(0xFFBFD4E5))
private val TARGARYEN = HousePalette(primary = Color(0xFFBF0A30), onPrimary = Color.White, secondary = Color(0xFF0A0A0A))

// -------------------- RESOLUCIÓN Y APLICACIÓN --------------------

private val housePaletteMap: Map<String, HousePalette> = mapOf(
    // principales
    "lannister" to LANNISTER,
    "stark" to STARK,
    "targaryen" to TARGARYEN,
    // pedidas
    "arryn" to ARRYN,
    "baratheon" to BARATHEON,
    "blackwood" to BLACKWOOD,
    "bolton" to BOLTON,
    "clegane" to CLEGANE,
    "darry" to DARRY,
    "florent" to FLORENT,
    "frey" to FREY,
    "glover" to GLOVER,
    "greyjoy" to GREYJOY,
    "harlaw" to HARLAW,
    "hightower" to HIGHTOWER,
    "karstark" to KARSTARK,
    "manderly" to MANDERLY,
    "martell" to MARTELL,
    "mormont" to MORMONT,
    "reed" to REED,
    "royce" to ROYCE,
    "tarly" to TARLY,
    "tully" to TULLY,
    "tyrell" to TYRELL,
    "umber" to UMBER
)

private fun resolvePaletteByName(name: String): HousePalette? {
    val n = name.lowercase()
    // acepta "House Stark", "Casa Stark", etc.
    return housePaletteMap.entries.firstOrNull { (key, _) -> key in n }?.value
}

private fun applyHousePalette(base: ColorScheme, hp: HousePalette): ColorScheme {
    return base.copy(
        primary = hp.primary,
        onPrimary = hp.onPrimary,
        secondary = hp.secondary,
        onSecondary = hp.onSecondary,
        tertiary = hp.tertiary ?: base.tertiary
    )
}

/**
 * ColorScheme para una Casa concreta. Úsalo en HouseDetailScreen:
 *
 * val scheme = houseColorSchemeFor(house)
 * MaterialTheme(colorScheme = scheme) { ... }
 */
@Composable
fun houseColorSchemeFor(house: House): ColorScheme {
    val dark = isSystemInDarkTheme()
    val base = if (dark) DarkColorScheme else LightColorScheme
    val hp = resolvePaletteByName(house.name) ?: return base
    return applyHousePalette(base, hp)
}
