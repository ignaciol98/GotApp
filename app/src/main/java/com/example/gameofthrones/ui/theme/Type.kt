package com.example.gameofthrones.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.gameofthrones.R

val CinzelFamily = FontFamily(
    Font(R.font.cinzel_regular, FontWeight.Normal),
    Font(R.font.cinzel_bold, FontWeight.Bold)
)

val MerriWeatherFamily = FontFamily(
    Font(R.font.merriweather_regular, FontWeight.Normal),
    Font(R.font.merriweather_bold, FontWeight.Bold)
)

// Typography para Material3
val Typography = Typography(
    displayLarge = TextStyle(        // equivalente aproximado de h1
        fontFamily = CinzelFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 26.sp,
        lineHeight = 32.sp
    ),
    headlineMedium = TextStyle(      // equivalente aproximado de h2
        fontFamily = CinzelFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 20.sp
    ),
    bodyLarge = TextStyle(           // equivalente aproximado de body1
        fontFamily = MerriWeatherFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    ),
    titleMedium = TextStyle(         // equivalente aproximado de subtitle1
        fontFamily = MerriWeatherFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp
    )

)
