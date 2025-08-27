package com.example.gameofthrones.model

data class Region(
    val id: String,
    val name: String,
    val houseIds: List<String>
)
