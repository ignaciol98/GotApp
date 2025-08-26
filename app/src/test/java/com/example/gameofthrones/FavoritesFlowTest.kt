package com.example.gameofthrones

import app.cash.turbine.test
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

// Fake mínimo que emula el comportamiento de FavoriteRepository para unit tests
private class FakeFavoriteRepo {
    private val ids = MutableStateFlow<Set<String>>(emptySet())

    fun isFavorite(id: String) = ids.map { id in it }

    suspend fun setFavorite(id: String, fav: Boolean) {
        ids.value = if (fav) ids.value + id else ids.value - id
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
class FavoritesFlowTest {

    @Test
    fun toggle_updates_isFavorite_flow() = runTest {
        val repo = FakeFavoriteRepo()
        val flow = repo.isFavorite("stark")

        flow.test {
            // Estado inicial: no es favorito
            assertEquals(false, awaitItem())

            // Marcar como favorito
            repo.setFavorite("stark", true)
            assertEquals(true, awaitItem())

            // Desmarcar favorito
            repo.setFavorite("stark", false)
            assertEquals(false, awaitItem())

            cancelAndIgnoreRemainingEvents()
        }
    }
}
