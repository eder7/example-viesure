package io.viesure.test.utils.ui.viewmodelfactory

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidedValue
import androidx.compose.runtime.compositionLocalOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import javax.inject.Inject
import javax.inject.Provider

// Credits to https://t.ly/U9ApY

interface ViewModelAssistedFactory<T : ViewModel> {
    fun create(handle: SavedStateHandle): T
}

interface ViewModelFactory {
    fun <VM : ViewModel> create(modelClass: Class<VM>, handle: SavedStateHandle): VM
}

/**
 * [ViewModelFactory] implementation that creates instances of view models using
 * [dagger.assisted.AssistedInject]-annotated constructors.
 */
class DaggerViewModelAssistedFactory @Inject constructor(
    private val assistedFactoryMap: Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModelAssistedFactory<*>>>
) : ViewModelFactory {

    @Suppress("UNCHECKED_CAST")
    override fun <VM : ViewModel> create(modelClass: Class<VM>, handle: SavedStateHandle): VM {
        val creator =
            assistedFactoryMap[modelClass] ?: assistedFactoryMap.asIterable().firstOrNull {
                modelClass.isAssignableFrom(it.key)
            }?.value ?: throw IllegalArgumentException("unknown model class $modelClass")
        return try {
            creator.get().create(handle) as VM
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }
}

@Composable
@PublishedApi
internal fun getViewModelFactory(): ViewModelFactory {
    return checkNotNull(LocalViewModelFactory.current) {
        "No ViewModelFactory was provided via LocalViewModelFactory"
    }
}

internal object LocalViewModelFactory {
    private val LocalViewModelFactory =
        compositionLocalOf<ViewModelFactory?> { null }

    val current: ViewModelFactory?
        @Composable
        get() = LocalViewModelFactory.current

    infix fun provides(viewModelFactory: ViewModelFactory): ProvidedValue<ViewModelFactory?> {
        return LocalViewModelFactory.provides(viewModelFactory)
    }
}
