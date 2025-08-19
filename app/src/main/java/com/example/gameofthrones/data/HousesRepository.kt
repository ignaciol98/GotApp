package com.example.gameofthrones.model

interface HousesRepository {
    suspend fun getAllHouses(): List<House>
    suspend fun getHouseById(id: String): House?
}
