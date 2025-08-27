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
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HouseDetailViewModel @Inject constructor(
    private val repo: HousesRepository,
    private val favoritesRepo: FavoriteRepository
) : ViewModel() {

    private val _house = MutableStateFlow<House?>(null)
    val house: StateFlow<House?> = _house.asStateFlow()

    private val _isFavorite = MutableStateFlow(false)
    val isFavorite: StateFlow<Boolean> = _isFavorite.asStateFlow()

    /**
     * Carga la casa y además comienza a observar el estado de favorito para esta casa.
     * Llamar desde la pantalla con el houseId (ej: en LaunchedEffect).
     */
    fun load(id: String) {
        // Cargar detalles de la casa
        viewModelScope.launch {
            _house.value = repo.getHouseById(id)
        }

        // Observar cambios en la persistencia de favoritos (flow)
        viewModelScope.launch {
            favoritesRepo.isFavorite(id).collect { fav ->
                _isFavorite.value = fav
            }
        }
    }

    /**
     * Alterna el favorito y lo persiste usando FavoriteRepository.
     */
    fun toggleFavorite(houseId: String) {
        viewModelScope.launch {
            val newValue = !_isFavorite.value
            favoritesRepo.setFavorite(houseId, newValue)

            _isFavorite.value = newValue
        }
    }
}
