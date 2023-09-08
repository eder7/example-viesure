package io.viesure.test.utils.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.lifecycle.ViewModel
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.compose.viewModel
import io.viesure.test.utils.ui.viewmodelfactory.LocalViewModelFactory
import io.viesure.test.utils.ui.viewmodelfactory.ViewModelFactory
import io.viesure.test.utils.ui.viewmodelfactory.getViewModelFactory

// Credits to https://t.ly/U9ApY

/**
 * Use this in conjunction with [io.viesure.test.utils.Inject] to retrieve a view model.
 *
 * **Example:**
 *
 * ```kotlin
 * Inject(AppComponent.INSTANCE.getViewModelFactory()) {
 *     viewModel = daggerViewModel()
 * }
 * ```
 */
@Composable
inline fun <reified VM : ViewModel> daggerViewModel(): VM {
    val factory = getViewModelFactory()
    return viewModel {
        val savedStateHandle = createSavedStateHandle()
        factory.create(VM::class.java, savedStateHandle)
    }
}

/**
 * Use in conjunction with [daggerViewModel].
 */
@Composable
fun Inject(
    viewModelFactory: ViewModelFactory,
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(
        LocalViewModelFactory provides viewModelFactory,
        content = content
    )
}
