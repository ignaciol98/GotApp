package com.example.gameofthrones.model

import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class HouseResponse(
    val nombre: String,
    val descripcion: String,
    val lema: String? = null,
    val personajes_destacados: List<PersonajeResponse> = emptyList(),
    val imagen: String? = null,
    val ubicacion: String? = null,
    val id: String? = null
) {
    fun toDomain(): House {
        val generatedId = id ?: nombre.lowercase(Locale.ROOT).replace("\\s+".toRegex(), "_")
        return House(
            id = generatedId,
            name = nombre,
            sigilUrl = imagen ?: "",
            description = descripcion,
            motto = lema ?: "",
            notableCharacters = personajes_destacados.map { it.toDomain() },
            locationName = ubicacion ?: ""
        )
    }
}





