package com.example.pont.data

import kotlinx.serialization.Serializable

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
