package com.example.gameofthrones.data

import android.content.Context
import com.example.gameofthrones.model.House
import com.example.gameofthrones.model.HouseResponse
import com.example.gameofthrones.model.HousesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import javax.inject.Inject

class AssetHousesRepository @Inject constructor(
    private val context: Context
) : HousesRepository {

    private val json = Json { ignoreUnknownKeys = true }

    override suspend fun getAllHouses(): List<House> = withContext(Dispatchers.IO) {
        val assetName = "houses.json"
        context.assets.open(assetName).use { stream ->
            // decodifica la estructura principal: { "houses": [ ... ] }
            val wrapper = json.decodeFromStream<HousesWrapper>(stream)
            wrapper.houses.map { it.toDomain() }
        }
    }

    override suspend fun getHouseById(id: String): House? = withContext(Dispatchers.IO) {
        getAllHouses().firstOrNull { it.id == id }
    }
}


@kotlinx.serialization.Serializable
private data class HousesWrapper(val houses: List<HouseResponse>)
