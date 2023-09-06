package io.viesure.test.usecases

import io.viesure.test.entities.Article
import kotlinx.coroutines.flow.StateFlow

interface GetArticlesStreamCached {
    val articlesLoadingStream: StateFlow<Boolean>
    val articlesStream: StateFlow<List<Article>>
}
