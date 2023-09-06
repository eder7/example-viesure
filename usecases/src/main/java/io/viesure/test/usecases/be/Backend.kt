package io.viesure.test.usecases.be

import io.viesure.test.entities.Article

interface GetArticlesFromBackend {
    suspend operator fun invoke(): List<Article>
}
