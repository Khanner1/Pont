package com.example.pont.data

import kotlinx.coroutines.flow.Flow

interface PuzzleRepository {
    suspend fun initializeDatabase()

    suspend fun getPuzzleById(id: String): Puzzle?
    fun getAllPuzzles(): Flow<List<Puzzle>>

    suspend fun togglePuzzleCompletion(id: String, isCompleted: Boolean)

    suspend fun clearAllData()

    suspend fun resetAllProgress()
}