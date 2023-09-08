package io.viesure.test.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.viesure.test.usecases.CurrentSortedArticlesStream
import io.viesure.test.usecases.GetArticlesSyncing
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject
import kotlin.random.Random
import io.viesure.test.entities.Article as ArticleEntity

private val DUMMY_TITLE = "Some title is here to inform us about something! ".repeat(3).trim()
private val DUMMY_DESCRIPTION = "Some description. ".repeat(15).trim()
private const val DUMMY_IMAGE_URI = ""

internal class ArticleListViewModel @Inject constructor(
    private val currentSortedArticlesStream: CurrentSortedArticlesStream,
    private val getArticlesSyncing: GetArticlesSyncing
) : ViewModel() {

    private val _uiState = MutableStateFlow(UiState.INITIAL)
    val uiState = _uiState.asSharedFlow()

    init {
        launchArticlesStream()
        launchArticlesLoadingStream()
    }

    private fun launchArticlesStream() {
        currentSortedArticlesStream.articlesStream
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

    data class UiState(val loading: Boolean, val articles: List<Article>) {
        companion object {
            val INITIAL = UiState(
                loading = true,
                articles = emptyList()
            )
        }
    }

    data class Article(
        val id: Int,
        val title: String,
        val description: String,
        val imageUri: String
    ) {
        companion object {
            fun createDummy(): Article {
                return Article(
                    id = Random.nextInt(),
                    title = DUMMY_TITLE,
                    description = DUMMY_DESCRIPTION,
                    imageUri = DUMMY_IMAGE_URI
                )
            }

            fun fromEntity(article: ArticleEntity) =
                Article(
                    id = article.id,
                    title = article.title,
                    description = article.description,
                    imageUri = article.imageUri
                )
        }
    }
}
