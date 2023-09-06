package io.viesure.test.usecases.repository

import android.util.Log
import io.viesure.test.entities.Article
import io.viesure.test.usecases.GetArticlesStreamCached
import io.viesure.test.usecases.be.GetArticlesFromBackend
import io.viesure.test.usecases.utils.retry
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class ArticlesRepository @Inject constructor(val getArticlesFromBackend: GetArticlesFromBackend) :
    GetArticlesStreamCached {

    override val articlesStream = MutableStateFlow(emptyList<Article>())

    override val articlesLoadingStream = MutableStateFlow(false)

    private val scope = CoroutineScope(Dispatchers.IO)

    init {
        launchGetFromBackend()
    }

    private fun launchGetFromBackend() {
        scope.launch {
            articlesLoadingStream.value = true
            articlesStream.value = try {
                retry(times = 3, delayMillis = 2000) {
                    getArticlesFromBackend()
                }
            } catch (exception: Exception) {
                Log.w(this::class.simpleName, "Failed to load articles from backend", exception)
                emptyList()
            } finally {
                articlesLoadingStream.value = false
            }
        }
    }
}