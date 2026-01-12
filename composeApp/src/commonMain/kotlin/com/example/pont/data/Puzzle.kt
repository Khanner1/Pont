package com.example.pont.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.DrawableResource
import pont.composeapp.generated.resources.Res
import pont.composeapp.generated.resources.dummy_image
import pont.composeapp.generated.resources.mona_lisa
import pont.composeapp.generated.resources.orchestra


@Serializable
sealed class Hint {
    @Serializable class TextHint(val text: String): Hint()
    @Serializable class ImageHint(
        val imageResId: String,
        val caption: String? = null
    ): Hint()
}

@Serializable
@Entity(tableName = "puzzles")
data class Puzzle(
    @PrimaryKey val id: String,
    val word: String,
    val dateAdded: String,
    val language: String,
    val definition: String,
    val example: String,
    val exampleTranslation: String,
    val hint1: Hint,
    val hint2: Hint,
    val answer: String,
    val funFact: Article,
    val isSolved: Boolean
)

fun Hint.ImageHint.getDrawable(): DrawableResource {
    return when (imageResId) {
        "dummy_image" -> Res.drawable.dummy_image
        "mona_lisa" -> Res.drawable.mona_lisa
        "orchestra" -> Res.drawable.orchestra
        else -> throw IllegalArgumentException("Unknown imageResId: $imageResId")
    }
}
