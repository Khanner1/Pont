package com.example.pont.data

import kotlinx.serialization.json.Json
import pont.composeapp.generated.resources.Res // This will be generated after build

suspend fun loadPuzzles(): List<Puzzle> {
    return try {
        // Res.readBytes is the KMP way to read files in commonMain
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