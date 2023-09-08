package io.viesure.test.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.viesure.test.R
import io.viesure.test.entities.Article
import io.viesure.test.usecases.GetArticle
import io.viesure.test.usecases.platform.Strings
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject

internal class ArticleDetailsViewModel @Inject constructor(
    private val getArticle: GetArticle,
    strings: Strings
) : ViewModel() {

    private val _uiState = MutableStateFlow(UiState.INITIAL)
    val uiState = _uiState.asSharedFlow()

    // Dedicated job so that coroutine is not cancelled
    private val oneShotScope = CoroutineScope(viewModelScope.coroutineContext + Job())

    // TODO: Replace with java.time API from API level 26!
    // We use this instead of the default format to match the design mockup instead of current locale defaults for demonstration purposes only
    private val dateFormat = SimpleDateFormat(
        strings(R.string.date_format_medium),
        Locale.getDefault()
    )

    fun setArticleId(articleId: Int) {
        oneShotScope.launch {
            _uiState.value = getArticle.get(articleId)
                ?.toUiState()
                ?: let { UiState.INITIAL }
        }
    }

    private fun Article.toUiState() = UiState(
        id,
        title,
        description,
        authorEmail,
        formatDate(releaseDateSeconds),
        imageUri
    )

    private fun formatDate(releaseDateSeconds: Long): String =
        dateFormat.format(releaseDateSeconds * 1000L)

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
        }
    }
}
