package com.example.pont.data

import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.DrawableResource
import pont.composeapp.generated.resources.Res
import pont.composeapp.generated.resources.dummy_image
import pont.composeapp.generated.resources.mona_lisa

@Serializable
sealed interface ArticleItem {
    @Serializable
    data class Paragraph(val text: String) : ArticleItem

    @Serializable
    data class Image(
        val imageResId: String,
        val caption: String? = null
    ) : ArticleItem

    @Serializable
    data class Bibliography(
        val sources: List<String>,
        val isExpanded: Boolean = false
    ) : ArticleItem
}

@Serializable
data class Article(
    val title: String,
    val items: List<ArticleItem>
)

fun ArticleItem.Image.getDrawable(): DrawableResource {
    return when (imageResId) {
        "dummy_image" -> Res.drawable.dummy_image
        "mona_lisa" -> Res.drawable.mona_lisa
        else -> throw IllegalArgumentException("Unknown imageResId: $imageResId")
    }
}
