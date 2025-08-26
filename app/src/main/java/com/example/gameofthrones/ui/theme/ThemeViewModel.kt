package com.example.gameofthrones.ui.theme

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ThemeViewModel @Inject constructor(
    private val repo: ThemeRepository
) : ViewModel() {

    val mode: StateFlow<ThemeMode> =
        repo.modeFlow.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            ThemeMode.SYSTEM
        )

    fun setMode(mode: ThemeMode) = viewModelScope.launch { repo.setMode(mode) }

    /** Conviene un ciclo rápido: SYSTEM → LIGHT → DARK → SYSTEM ... */
    fun cycleMode() = viewModelScope.launch {
        val next = when (mode.value) {
            ThemeMode.SYSTEM -> ThemeMode.LIGHT
            ThemeMode.LIGHT -> ThemeMode.DARK
            ThemeMode.DARK -> ThemeMode.SYSTEM
        }
        repo.setMode(next)
    }
}
