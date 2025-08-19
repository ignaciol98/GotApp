package com.example.gameofthrones.model

data class Region(
    val id: String,          // coincide con el name del <path> en el VectorDrawable
    val name: String,        // Nombre legible que mostrarás al usuario
    val houseIds: List<String> // IDs de las casas (coinciden con House.id)
)
