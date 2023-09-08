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

private const val LOG_LOAD_FAILED = "Failed to load articles from backend"
private const val LOG_SYNCED = "Synced %d articles from backend (%d new or changed)"
private const val LOG_SYNCED_NOTHING = "Synced nothing from backend (no changes detected)"
private const val LOG_SYNC_FAILED = "Failed to sync articles from backend"

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
        Timber.tag(tag).w(exception, LOG_LOAD_FAILED)
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
                    LOG_SYNCED,
                    remoteArticles.size,
                    newOrChangedArticles.size
                )
            } else {
                Timber.tag(tag).d(LOG_SYNCED_NOTHING)
            }
        } catch (exception: Exception) {
            Timber.tag(tag).e(exception, LOG_SYNC_FAILED)
        }
    }
}
