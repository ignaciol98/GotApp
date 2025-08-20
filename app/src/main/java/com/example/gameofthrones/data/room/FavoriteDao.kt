package com.example.gameofthrones.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteDao {
    // devolvemos Int (count) para mapear después a Boolean con seguridad
    @Query("SELECT COUNT(*) FROM favorites WHERE houseId = :id")
    fun isFavoriteCountFlow(id: String): Flow<Int>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun add(fav: FavoriteEntity)

    @Query("DELETE FROM favorites WHERE houseId = :id")
    suspend fun remove(id: String)

    @Query("SELECT houseId FROM favorites")
    fun getAllFavoritesFlow(): Flow<List<String>>
}
