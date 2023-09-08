package io.viesure.test.adapters.db.article

import io.viesure.adapters.db.Database
import io.viesure.test.usecases.CurrentSortedArticlesStream
import io.viesure.test.usecases.GetArticle
import io.viesure.test.usecases.GetArticles
import io.viesure.test.usecases.PutArticles
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton
import io.viesure.test.entities.Article as ArticleEntity

private const val LOG_LOADED_ARTICLES = "Loaded %d persisted articles"

/**
 * This repository abstracts access to locally persisted articles and is optimized for small
 * datasets only, reducing database read operations but needing to hold the whole dataset
 * in memory in turn. To support larger datasets paging would have to be implemented.
 */
@Singleton
internal class SortedArticlesRepository @Inject constructor(private val database: Database) :
    CurrentSortedArticlesStream, GetArticles, GetArticle, PutArticles {

    private val tag = this::class.simpleName!!
    private val scope = CoroutineScope(Dispatchers.IO)

    private val _articlesStream = database.articlesDao().getAllStream()
        .map { articles ->
            articles.map { article ->
                article.toEntity()
            }.also {
                Timber.tag(tag).i(LOG_LOADED_ARTICLES, it.size)
            }
        }.shareIn(scope, SharingStarted.Eagerly, replay = 1)

    override val articlesStream: StateFlow<List<ArticleEntity>> =
        _articlesStream.stateIn(scope, SharingStarted.Eagerly, emptyList())

    override suspend fun getAll(): List<ArticleEntity> =
        withContext(Dispatchers.IO) {
            _articlesStream.first()
        }

    override suspend fun get(id: Int): ArticleEntity? =
        withContext(Dispatchers.IO) {
            _articlesStream.first().find { it.id == id }
        }

    override suspend fun insertOrReplace(articles: List<ArticleEntity>) =
        withContext(Dispatchers.IO) {
            database.articlesDao().insert(articles.map { Article.fromEntity(it) })
        }
}
