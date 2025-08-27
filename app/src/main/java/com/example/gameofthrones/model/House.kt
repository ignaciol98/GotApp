package com.example.gameofthrones.model

data class House(
    val id: String,
    val name: String,
    val sigilUrl: String = "",
    val description: String,
    val motto: String? = null,
    val notableCharacters: List<Character> = emptyList(),
    val locationName: String
)
