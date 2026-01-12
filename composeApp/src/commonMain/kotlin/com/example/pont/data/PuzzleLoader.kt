package com.example.pont.data

import kotlinx.serialization.json.Json
import pont.composeapp.generated.resources.Res

suspend fun loadPuzzles(): List<Puzzle> {
    return try {
        val bytes = Res.readBytes("files/puzzles.json")
        val jsonString = bytes.decodeToString()

        Json {
            ignoreUnknownKeys = true
            classDiscriminator = "type"
        }.decodeFromString<List<Puzzle>>(jsonString)
    } catch (e: Exception) {
        emptyList()
    }
}