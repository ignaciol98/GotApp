package com.example.gameofthrones.ui.theme

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gameofthrones.model.House
import com.example.gameofthrones.model.HousesRepository
import com.example.gameofthrones.data.FavoriteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HousesViewModel @Inject constructor(
    private val repo: HousesRepository,
    private val favoriteRepo: FavoriteRepository
) : ViewModel() {

    // --- Modelo original de casas
    private val _houses = MutableStateFlow<List<House>>(emptyList())
    val houses: StateFlow<List<House>> = _houses.asStateFlow()

    // --- Estado interno de ids favoritos (set para búsquedas rápidas)
    private val _favoriteIds = MutableStateFlow<Set<String>>(emptySet())
    val favoriteIds: StateFlow<Set<String>> = _favoriteIds.asStateFlow()

    // --- Modelo para la UI: House + isFavorite
    data class HouseUi(val house: House, val isFavorite: Boolean)

    // --- Lista preparada para la UI (combina casas + favoritos)
    private val _housesUi = MutableStateFlow<List<HouseUi>>(emptyList())
    val housesUi: StateFlow<List<HouseUi>> = _housesUi.asStateFlow()

    init {
        // 1) Cargar las casas (una sola vez al init)
        viewModelScope.launch {
            _houses.value = repo.getAllHouses()
        }

        // 2) Coleccionar los favoritos desde la DB y mantener _favoriteIds actualizado
        viewModelScope.launch {
            favoriteRepo.getAllFavoriteIds()                 // Flow<List<String>>
                .map { list -> list.toSet() }               // convierte a Set<String>
                .collect { set -> _favoriteIds.value = set }
        }

        // 3) Combinar casas + favoritos para producir housesUi
        viewModelScope.launch {
            combine(
                _houses,                 // Flow<List<House>>
                _favoriteIds             // Flow<Set<String>>
            ) { housesList, favIds ->
                housesList.map { h -> HouseUi(h, favIds.contains(h.id)) }
            }.collect { uiList ->
                _housesUi.value = uiList
            }
        }
    }

    // Metodo público para alternar favorito (UI lo llama)
    fun toggleFavorite(houseId: String) {
        viewModelScope.launch {
            val currentlyFav = _favoriteIds.value.contains(houseId)
            favoriteRepo.setFavorite(houseId, !currentlyFav)
            // No es necesario actualizar _favoriteIds aquí porque lo actualiza el collector
            // que escucha favoriteRepo.getAllFavoriteIds()
        }
    }
}

