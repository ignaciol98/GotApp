package com.example.gameofthrones.model

import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class HouseResponse(
    val nombre: String,
    val descripcion: String,
    val lema: String? = null,
    val personajes_destacados: List<PersonajeResponse> = emptyList(),
    val imagen: String? = null,   // tu JSON usa "imagen" para la URL del escudo
    val ubicacion: String
) {
    fun toDomain(): House {
        val id = nombre.lowercase(Locale.ROOT).replace("\\s+".toRegex(), "_")
        return House(
            id = id,
            name = nombre,
            sigilUrl = imagen ?: "",
            description = descripcion,
            motto = lema,
            notableCharacters = personajes_destacados.map { it.toDomain() },
            locationName = ubicacion
        )
    }
}




