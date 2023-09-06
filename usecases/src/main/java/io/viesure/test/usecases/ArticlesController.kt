package io.viesure.test.usecases

import android.util.Log
import io.viesure.test.entities.Article
import io.viesure.test.usecases.be.LoadArticlesFromBackend
import io.viesure.test.usecases.utils.retry
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Contains business logic around articles (loading from backend, syncing, persisting locally)
 */
@Singleton
internal class ArticlesController @Inject constructor(
    private val loadArticlesFromBackend: LoadArticlesFromBackend,
    private val saveLocalArticles: PutArticles,
    private val getLocalArticles: GetArticles
) : GetArticlesSyncing {

    private val tag = this::class.simpleName
    private val scope = CoroutineScope(Dispatchers.IO)

    override val articlesSyncingStream = MutableStateFlow(false)

    init {
        launchSync()
    }

    private fun launchSync() {
        scope.launch {
            articlesSyncingStream.value = true
            val remoteArticles = loadFromBackendWithRetry()
            syncWithLocal(remoteArticles)
        }
    }

    private suspend fun loadFromBackendWithRetry() = try {
        retry(times = 3, delayMillis = 2000) {
            loadArticlesFromBackend()
        }
    } catch (exception: Exception) {
        Log.w(tag, "Failed to load articles from backend", exception)
        emptyList()
    } finally {
        articlesSyncingStream.value = false
    }

    /**
     * Checks with local database and updates corresponding
     * articles in order to save a insert/load cycle
     */
    private suspend fun syncWithLocal(remoteArticles: List<Article>) {
        try {
            val newOrChangedArticles = remoteArticles - getLocalArticles.getAll()
            if (newOrChangedArticles.isNotEmpty()) {
                saveLocalArticles.insertOrReplace(newOrChangedArticles)
                Log.d(
                    tag,
                    "Synced ${remoteArticles.size} articles " +
                            "(${newOrChangedArticles.size} new or changed)"
                )
            } else {
                Log.d(tag, "Synced nothing (no changes detected)")
            }
        } catch (exception: Exception) {
            Log.e(tag, "Failed to sync articles to local database", exception)
        }
    }
}
