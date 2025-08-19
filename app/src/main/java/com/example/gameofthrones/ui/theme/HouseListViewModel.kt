package com.example.gameofthrones.ui.theme

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gameofthrones.model.HousesRepository
import com.example.gameofthrones.model.House
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HouseListViewModel @Inject constructor(
    private val repo: HousesRepository
) : ViewModel() {
    private val _houses = MutableStateFlow<List<House>>(emptyList())
    val houses: StateFlow<List<House>> = _houses

    init {
        viewModelScope.launch {
            _houses.value = repo.getAllHouses().sortedBy { it.name }
        }
    }
}