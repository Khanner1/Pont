package com.example.pont.data

import androidx.room.TypeConverter
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class Converters {
    @TypeConverter
    fun fromHint(hint: Hint): String = Json.encodeToString(hint)

    @TypeConverter
    fun toHint(hintString: String): Hint = Json.decodeFromString(hintString)

    @TypeConverter
    fun fromArticle(article: Article): String = Json.encodeToString(article)

    @TypeConverter
    fun toArticle(articleString: String): Article = Json.decodeFromString(articleString)
}