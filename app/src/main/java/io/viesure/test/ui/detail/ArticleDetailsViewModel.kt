package io.viesure.test.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.viesure.test.entities.Article
import io.viesure.test.usecases.GetArticle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject

class ArticleDetailsViewModel @Inject constructor(val getArticle: GetArticle) : ViewModel() {

    private val _uiState = MutableStateFlow(UiState.INITIAL)
    val uiState = _uiState.asSharedFlow()

    // Dedicated job so that coroutine is not cancelled
    private val oneShotScope = CoroutineScope(viewModelScope.coroutineContext + Job())

    fun setArticleId(articleId: Int) {
        oneShotScope.launch {
            _uiState.value = getArticle.get(articleId)
                ?.let { UiState.fromEntity(it) }
                ?: run { UiState.INITIAL }
        }
    }

    data class UiState(
        val id: Int = 0,
        val title: String = "",
        val description: String = "",
        val authorEmail: String = "",
        val releaseDate: String = "",
        val imageUri: String = ""
    ) {
        companion object {
            val INITIAL = UiState()

            // TODO: Replace with java.time API from API level 26!
            // We use this instead of the default format to match the design mockup instead of current locale defaults for demonstration purposes only
            private val dateFormat = SimpleDateFormat("E, MMM d, ''yy", Locale.ENGLISH)

            fun fromEntity(entity: Article) = UiState(
                entity.id,
                entity.title,
                entity.description,
                entity.authorEmail,
                dateFormat.format(entity.releaseDateSeconds * 1000L),
                entity.imageUri
            )
        }
    }
}
