package io.viesure.test.usecases.be

import io.viesure.test.entities.Article

interface LoadArticlesFromBackend {
    suspend operator fun invoke(): List<Article>
}
