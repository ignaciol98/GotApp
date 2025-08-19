package com.example.gameofthrones.ui.theme

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gameofthrones.model.House
import com.example.gameofthrones.model.HousesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HouseDetailViewModel @Inject constructor(
    private val repo: HousesRepository
) : ViewModel() {

    private val _house = MutableStateFlow<House?>(null)
    val house: StateFlow<House?> = _house.asStateFlow()

    fun load(id: String) {
        viewModelScope.launch {
            _house.value = repo.getHouseById(id)
        }
    }
}
