package io.viesure.test.adapters.db.article

import android.util.Log
import io.viesure.adapters.db.Database
import io.viesure.test.usecases.GetArticles
import io.viesure.test.usecases.GetCurrentArticles
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
import javax.inject.Inject
import javax.inject.Singleton

/**
 *
 */
@Singleton
internal class ArticlesRepository @Inject constructor(private val database: Database) :
    GetCurrentArticles, GetArticles, PutArticles {

    private val tag = this::class.simpleName
    private val scope = CoroutineScope(Dispatchers.IO)

    private val _articlesStream = database.articlesDao().getAllStream()
        .map { articles ->
            articles.map { article ->
                article.toEntity()
            }.also {
                Log.i(tag, "Loaded ${it.size} persisted articles")
            }
        }.shareIn(scope, SharingStarted.Eagerly, replay = 1)

    override val articlesStream: StateFlow<List<io.viesure.test.entities.Article>> =
        _articlesStream.stateIn(scope, SharingStarted.Eagerly, emptyList())

    override suspend fun getAll(): List<io.viesure.test.entities.Article> =
        withContext(Dispatchers.IO) {
            _articlesStream.first()
        }

    override suspend fun insertOrReplace(articles: List<io.viesure.test.entities.Article>) =
        withContext(Dispatchers.IO) {
            database.articlesDao().insert(articles.map { Article.fromEntity(it) })
        }
}
