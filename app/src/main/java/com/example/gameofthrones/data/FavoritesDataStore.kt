package com.example.gameofthrones.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "got_prefs")

object FavoritesDataStore {
    private val FAVORITES_KEY = stringSetPreferencesKey("favorites")

    fun favoritesFlow(context: Context): Flow<Set<String>> =
        context.dataStore.data.map { prefs ->
            prefs[FAVORITES_KEY] ?: emptySet()
        }

    suspend fun addFavorite(context: Context, id: String) {
        context.dataStore.edit { prefs ->
            val set = prefs[FAVORITES_KEY]?.toMutableSet() ?: mutableSetOf()
            set.add(id)
            prefs[FAVORITES_KEY] = set
        }
    }

    suspend fun removeFavorite(context: Context, id: String) {
        context.dataStore.edit { prefs ->
            val set = prefs[FAVORITES_KEY]?.toMutableSet() ?: mutableSetOf()
            set.remove(id)
            prefs[FAVORITES_KEY] = set
        }
    }
}