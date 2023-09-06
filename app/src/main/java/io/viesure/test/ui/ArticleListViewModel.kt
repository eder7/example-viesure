package io.viesure.test.ui

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.viesure.test.usecases.GetCurrentArticles
import io.viesure.test.usecases.GetArticlesSyncing
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject
import kotlin.random.Random
import io.viesure.test.entities.Article as ArticleEntity

class ArticleListViewModel @Inject constructor(
    private val getCurrentArticles: GetCurrentArticles,
    private val getArticlesSyncing: GetArticlesSyncing
) : ViewModel() {

    private val _uiState = MutableStateFlow(UiState.INITIAL)
    val uiState = _uiState.asSharedFlow()

    private val _uiActions = MutableSharedFlow<UiAction>(
        extraBufferCapacity = 99,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val uiActions = _uiActions.asSharedFlow()

    init {
        launchArticlesStream()
        launchArticlesLoadingStream()
    }

    private fun launchArticlesStream() {
        getCurrentArticles.articlesStream
            .onEach { articles ->
                updateUiState { uiState ->
                    uiState.copy(
                        articles = articles.map { Article.fromEntity(it) }
                    )
                }
            }
            .flowOn(Dispatchers.IO)
            .launchIn(viewModelScope)
    }

    private fun launchArticlesLoadingStream() {
        getArticlesSyncing.articlesSyncingStream
            .onEach { loading ->
                updateUiState {
                    it.copy(
                        loading = loading
                    )
                }
            }
            .flowOn(Dispatchers.IO)
            .launchIn(viewModelScope)
    }

    private fun updateUiState(block: (uiState: UiState) -> UiState) {
        synchronized(_uiState) {
            _uiState.value = block(_uiState.value)
        }
    }

    sealed interface UiAction {
        data class ShowError(val message: String) : UiAction
    }

    data class UiState(val loading: Boolean, val articles: List<Article>) {
        companion object {
            val INITIAL = UiState(false, emptyList())
        }
    }

    data class Article(
        val id: Int,
        val title: String,
        val description: String,
        val imageUri: Uri
    ) {
        companion object {
            fun createDummy() = Article(
                Random.nextInt(),
                "Some title is here to inform us about something about something about something about something about something about something about something",
                "Some description, Some description, Some description, Some description, Some description, Some description, Some description, Some description, Some description, Some description, Some description, Some description, Some description, Some description, Some description, Some description, Some description, Some description!",
                Uri.parse("")
            )

            fun fromEntity(article: ArticleEntity) =
                Article(article.id, article.title, article.description, Uri.parse(article.imageUri))
        }
    }
}
