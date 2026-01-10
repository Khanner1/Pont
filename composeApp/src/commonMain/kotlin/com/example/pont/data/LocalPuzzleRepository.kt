package com.example.pont.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow

class LocalPuzzleRepository(private val puzzleDao: PuzzleDao) : PuzzleRepository {

    override suspend fun initializeDatabase() {
        val currentPuzzles = puzzleDao.getAllPuzzles().first()

        if (currentPuzzles.isEmpty()) {
            puzzleDao.insert(loadPuzzles())
        }
    }

    override suspend fun togglePuzzleCompletion(id: String, isCompleted: Boolean) {
        puzzleDao.updateCompletionStatus(id, isCompleted)
    }

    override suspend fun clearAllData() {
        puzzleDao.deleteAllPuzzles()
    }

    override suspend fun getPuzzleById(id: String): Puzzle? {
        return puzzleDao.getPuzzleById(id)
    }

    override fun getAllPuzzles(): Flow<List<Puzzle>> {
        return puzzleDao.getAllPuzzles()
    }

    override suspend fun resetAllProgress() {
        puzzleDao.resetAllPuzzles()
    }
}




