package com.example.gameofthrones

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.material3.Scaffold
import com.example.gameofthrones.ui.theme.AppNavGraph
import com.example.gameofthrones.ui.theme.GameOfThronesTheme
import dagger.hilt.android.AndroidEntryPoint
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.MaterialTheme
import androidx.core.view.WindowCompat

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Habilita edge-to-edge (quita el ajuste automático de los decor windows).
        // Alternativa: si tenías una función enableEdgeToEdge(), podés removerla y usar esto.
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            GameOfThronesTheme {
                // 1. Contenedor de fondo
                Box(modifier = Modifier.fillMaxSize()) {
                    // 2. Imagen de pergamino en segundo plano (ocupa todo el fondo)
                    Image(
                        painter = painterResource(R.drawable.bg_parchment),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )

                    // 3. UI principal encima. Aplicamos systemBarsPadding() para que el contenido
                    //    no quede debajo de la barra de estado / navegación.
                    Scaffold(
                        modifier = Modifier
                            .fillMaxSize()
                            .systemBarsPadding(),
                        containerColor = Color.Transparent,
                        contentColor = MaterialTheme.colorScheme.onBackground
                    ) { innerPadding ->
                        AppNavGraph(
                            modifier = Modifier
                                .padding(innerPadding)
                        )
                    }
                }
            }
        }
    }
}
