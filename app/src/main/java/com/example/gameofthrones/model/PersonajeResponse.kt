package com.example.gameofthrones.model

import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class PersonajeResponse(
    val nombre: String,
    val biografia: String,
    val imagen: String? = null
) {
    fun toDomain(): Character {
        val id = nombre.lowercase(Locale.ROOT).replace("\\s+".toRegex(), "_")
        return Character(
            id = id,
            name = nombre,
            biography = biografia,
            imageUrl = imagen
                ?: ""
        )
    }
}

