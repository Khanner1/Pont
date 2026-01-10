package com.example.pont.ui.puzzle

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.pont.data.LocalPuzzleRepository
import com.example.pont.data.Puzzle
import com.example.pont.data.PuzzleRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PuzzleViewModel(
    private val repository: PuzzleRepository
) : ViewModel(){

    private val _puzzle = MutableStateFlow<Puzzle?>(null)
    val puzzle: StateFlow<Puzzle?> = _puzzle.asStateFlow()

    private val _guessCount = MutableStateFlow(0)
    val guessCount: StateFlow<Int> = _guessCount.asStateFlow()

    private val _showHint2 = MutableStateFlow(false)
    val showHint2: StateFlow<Boolean> = _showHint2.asStateFlow()

    private val _showAnswer = MutableStateFlow(false)
    val showAnswer: StateFlow<Boolean> = _showAnswer.asStateFlow()


    suspend fun loadPuzzle(puzzleId: String) {
        val loadedPuzzle = repository.getPuzzleById(puzzleId)
        _puzzle.value = loadedPuzzle

        _guessCount.value = 0
        _showHint2.value = false
        _showAnswer.value = false
    }

    private fun handlePuzzleCompletion(puzzleId: String) {
        viewModelScope.launch {
            repository.togglePuzzleCompletion(id = puzzleId, isCompleted = true)

            _showHint2.value = true
            _showHint2.value = true
            _showAnswer.value = true

            _puzzle.update {it?.copy(isSolved = true)}
        }
    }

    fun submitGuess(guess: String) {
        val currentPuzzle = _puzzle.value ?: return

        if (guess.equals(currentPuzzle.answer, ignoreCase = true)) {
            handlePuzzleCompletion(currentPuzzle.id)
        } else {
            when (_guessCount.value) {
                0 -> {
                    _showHint2.value = true
                }
                else -> {
                    handlePuzzleCompletion(currentPuzzle.id)
                }
            }
        }

        _guessCount.update {it + 1}
    }




}