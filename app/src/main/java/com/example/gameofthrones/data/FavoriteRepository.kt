package com.example.gameofthrones.data

import com.example.gameofthrones.data.room.FavoriteEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton
import com.example.gameofthrones.data.room.FavoriteDao


@Singleton
class FavoriteRepository @Inject constructor(
    private val dao: FavoriteDao
) {
    fun isFavorite(id: String): Flow<Boolean> {
        return dao.isFavoriteCountFlow(id).map { count -> count > 0 }
    }

    suspend fun setFavorite(id: String, fav: Boolean) {
        if (fav) {
            dao.add(FavoriteEntity(id))
        } else {
            dao.remove(id)
        }
    }

    fun getAllFavoriteIds(): Flow<List<String>> = dao.getAllFavoritesFlow()
}
