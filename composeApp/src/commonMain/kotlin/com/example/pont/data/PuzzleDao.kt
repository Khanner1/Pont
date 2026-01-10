package com.example.pont.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface PuzzleDao {
    @Query("SELECT * FROM puzzles")
    fun getAllPuzzles(): Flow<List<Puzzle>>

    @Query("SELECT * FROM puzzles WHERE id = :id")
    suspend fun getPuzzleById(id: String): Puzzle?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(puzzle: List<Puzzle>)

    @Query("UPDATE puzzles SET isSolved= :completed WHERE id = :id")
    suspend fun updateCompletionStatus(id: String, completed: Boolean)

    @Query("Delete FROM puzzles")
    suspend fun deleteAllPuzzles()

    @Query("UPDATE puzzles SET isSolved = 0")
    suspend fun resetAllPuzzles()

}