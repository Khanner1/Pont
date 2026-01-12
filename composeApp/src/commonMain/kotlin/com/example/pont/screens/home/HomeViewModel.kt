package com.example.pont.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pont.data.Puzzle
import com.example.pont.data.PuzzleRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HomeViewModel(
    private val repository: PuzzleRepository
) : ViewModel() {
    init {
        viewModelScope.launch {
            repository.initializeDatabase()
        }
    }

    val allPuzzles: StateFlow<List<Puzzle>> = repository.getAllPuzzles()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun resetAllProgress() {
        viewModelScope.launch {
            repository.resetAllProgress()
        }
    }
}