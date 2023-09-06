package io.viesure.test.usecases.be

import io.viesure.test.entities.Article

interface GetArticlesFromBackend {
    suspend fun getArticles(): List<Article>
}
