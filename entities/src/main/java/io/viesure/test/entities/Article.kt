package io.viesure.test.entities

data class Article(
    val id: Int,
    val title: String,
    val description: String,
    val authorEmail: String,
    val releaseDateString: String,
    val imageUri: String
)
