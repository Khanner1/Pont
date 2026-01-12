package com.example.pont.ui.library

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pont.data.Puzzle
import com.example.pont.data.PuzzleRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

enum class PuzzleFilter {
    DATE,
    LANGUAGE,
    SOLVED,
    ALPHABETICAL
}

fun PuzzleFilter.toDisplayName(): String = when (this) {
    PuzzleFilter.DATE -> "Date Added"
    PuzzleFilter.ALPHABETICAL -> "A-Z"
    else -> this.name.lowercase().replaceFirstChar { it.uppercase() }
}

class LibraryViewModel(
    private val repository: PuzzleRepository
) : ViewModel() {

    val allPuzzles: StateFlow<List<Puzzle>> = repository.getAllPuzzles()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private val _filter = MutableStateFlow<PuzzleFilter>(PuzzleFilter.DATE)
    val filter: StateFlow<PuzzleFilter> = _filter.asStateFlow()

    fun updateFilter(newFilter: PuzzleFilter) {
        _filter.value = newFilter
    }

    val displayedPuzzles: StateFlow<List<Puzzle>> = combine(
        allPuzzles, _filter
    ) { all, filter ->
        when (filter) {
            PuzzleFilter.DATE -> all.sortedByDescending { it.dateAdded }
            PuzzleFilter.LANGUAGE -> all.sortedByDescending { it.language }
            PuzzleFilter.SOLVED -> all.sortedByDescending { it.isSolved }
            PuzzleFilter.ALPHABETICAL -> all.sortedBy { it.word }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
}