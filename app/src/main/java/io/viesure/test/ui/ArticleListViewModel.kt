package io.viesure.test.ui

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.viesure.test.usecases.be.GetArticlesFromBackend
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.random.Random
import io.viesure.test.entities.Article as ArticleEntity

class ArticleListViewModel @Inject constructor(val getArticlesFromBackend: GetArticlesFromBackend) :
    ViewModel() {

    private val _uiState = MutableStateFlow(UiState.INITIAL)
    val uiState = _uiState.asSharedFlow()

    private val _uiActions = MutableSharedFlow<UiAction>(
        extraBufferCapacity = 99,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val uiActions = _uiActions.asSharedFlow()

    init {
        initializeDummyUiState()
        initializeFromWeb()
    }

    private fun initializeFromWeb() {
        synchronized(_uiState) {
            _uiState.value = _uiState.value.copy(
                loading = true
            )
        }

        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    val articles = getArticlesFromBackend
                        .getArticles()
                        .map { Article.fromEntity(it) }
                    synchronized(_uiState) {
                        _uiState.value = _uiState.value.copy(
                            articles = articles,
                            loading = false
                        )
                    }
                } catch (exception: Exception) {
                    // TODO replace by proper user-facing error message
                    // TODO use string resources instead of magic string
                    _uiActions.tryEmit(UiAction.ShowError("Couldn't synchronize articles from backend: ${exception.message}"))
                    Log.e(this@ArticleListViewModel::class.simpleName, "Error retrieving articles", exception)
                    synchronized(_uiState) {
                        _uiState.value = _uiState.value.copy(
                            loading = false
                        )
                    }
                }
            }
        }
    }

    private fun initializeDummyUiState() {
        val articles = mutableListOf<Article>().also { list ->
            (0 until 50).forEach { // TODO fix warning
                list.add(Article.createDummy())
            }
        }
        synchronized(_uiState) {
            _uiState.value = _uiState.value.copy(
                articles = articles.toList()
            )
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
