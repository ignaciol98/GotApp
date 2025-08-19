package com.example.gameofthrones.model

data class Character(
    val id: String,
    val name: String,
    val biography: String,
    val imageUrl: String = "" // opcional, puede venir vacío si no hay URL en el JSON
)
