package io.viesure.test.usecases

import io.viesure.test.entities.Article
import io.viesure.test.usecases.be.LoadArticlesFromBackend
import io.viesure.test.usecases.platform.Dispatchers
import io.viesure.test.usecases.utils.retry
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Contains business logic around articles (loading from backend, syncing, persisting locally)
 */
@Singleton
class ArticlesController @Inject constructor(
    private val loadArticlesFromBackend: LoadArticlesFromBackend,
    private val saveLocalArticles: PutArticles,
    private val getLocalArticles: GetArticles,
    dispatchers: Dispatchers
) : GetArticlesSyncing {

    private val tag = this::class.simpleName!!
    private val scope = CoroutineScope(dispatchers.io)

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
        Timber.tag(tag).w(exception, "Failed to load articles from backend")
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
                Timber.tag(tag).d(
                    "Synced %d articles (%d new or changed)",
                    remoteArticles.size,
                    newOrChangedArticles.size
                )
            } else {
                Timber.tag(tag).d("Synced nothing (no changes detected)")
            }
        } catch (exception: Exception) {
            Timber.tag(tag).e(exception, "Failed to sync articles to local database")
        }
    }
}
