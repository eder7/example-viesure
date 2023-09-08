package io.viesure.test.usecases

import io.viesure.test.entities.Article
import kotlinx.coroutines.flow.StateFlow

/**
 * Streams current local articles, the initial value being an empty list
 */
interface CurrentSortedArticlesStream {
    val articlesStream: StateFlow<List<Article>>
}

/**
 * Provides articles, potentially waiting for its availability
 */
interface GetArticles {
    suspend fun getAll(): List<Article>
}

/**
 * Provides an article, potentially waiting for its availability
 */
interface GetArticle {
    suspend fun get(id: Int): Article?
}

interface PutArticles {
    suspend fun insertOrReplace(articles: List<Article>)
}

/**
 * Provides whether syncing articles with backend is in progress
 */
interface GetArticlesSyncing {
    val articlesSyncingStream: StateFlow<Boolean>
}
