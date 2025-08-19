package com.example.gameofthrones.ui.theme.map

import androidx.lifecycle.ViewModel
import com.example.gameofthrones.data.RegionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RegionViewModel @Inject constructor(
    private val repo: RegionRepository
) : ViewModel() {
    // exponer la lista de regiones
    val regions = repo.getAllRegions()
}