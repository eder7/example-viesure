package io.viesure.test.entities

data class Article(
    val id: Int,
    val title: String,
    val description: String,
    val authorEmail: String,
    val releaseDateSeconds: Long, // we use seconds as kotlinx-datetime is still experimental and to be platform-independent
    val imageUri: String
)
