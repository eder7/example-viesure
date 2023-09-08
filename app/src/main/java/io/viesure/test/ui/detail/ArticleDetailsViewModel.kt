package io.viesure.test.ui.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import io.viesure.test.R
import io.viesure.test.entities.Article
import io.viesure.test.ui.navigation.Routes
import io.viesure.test.usecases.GetArticle
import io.viesure.test.usecases.platform.Strings
import io.viesure.test.utils.ui.navigation.NavigatingViewModel
import io.viesure.test.utils.ui.navigation.Navigator
import io.viesure.test.utils.ui.viewmodelfactory.ViewModelAssistedFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

internal class ArticleDetailsViewModel @AssistedInject constructor(
    private val getArticle: GetArticle,
    strings: Strings,
    @Assisted private val savedStateHandle: SavedStateHandle,
    override val navigator: Navigator
) : ViewModel(), NavigatingViewModel {

    private val articleId = Routes.ArticleDetails.getIndexFrom(savedStateHandle)

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

    init {
        oneShotScope.launch {
            _uiState.value = getArticle.get(articleId)
                ?.toUiState()
                ?: let { UiState.INITIAL }
        }
    }

    fun onBackClicked() {
        navigator.navigateUp()
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

    @AssistedFactory
    interface Factory : ViewModelAssistedFactory<ArticleDetailsViewModel>
}
