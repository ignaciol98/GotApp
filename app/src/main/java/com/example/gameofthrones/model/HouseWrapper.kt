package com.example.gameofthrones.model

import kotlinx.serialization.Serializable

@Serializable
data class HousesWrapper(
    val houses: List<HouseResponse> = emptyList()
)