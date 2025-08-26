package com.example.gameofthrones.ui.theme

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gameofthrones.data.FavoriteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    repo: FavoriteRepository
) : ViewModel() {

    // IDs de casas favoritas que vienen de Room (dao.getAllFavoritesFlow())
    val favoriteIds: StateFlow<List<String>> =
        repo.getAllFavoriteIds()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())
}
