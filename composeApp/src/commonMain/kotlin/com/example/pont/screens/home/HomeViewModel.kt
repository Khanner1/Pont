package com.example.pont.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.pont.data.LocalPuzzleRepository
import com.example.pont.data.Puzzle
import com.example.pont.data.PuzzleRepository
import com.example.pont.ui.library.LibraryViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
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
            started =SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun resetAllProgress() {
        viewModelScope.launch {
            repository.resetAllProgress()
        }
    }






}