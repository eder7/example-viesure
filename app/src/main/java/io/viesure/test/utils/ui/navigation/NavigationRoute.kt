package io.viesure.test.utils.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import io.viesure.test.di.AppComponent
import io.viesure.test.utils.ui.Inject
import timber.log.Timber

interface NavigatingViewModel {
    val navigator: Navigator
}

/**
 * A route the app can navigate to.
 */
interface NavigationRoute<T : NavigatingViewModel> {

    val route: String

    /**
     * Returns the screen's content.
     */
    @Composable
    fun Content(viewModel: T)

    /**
     * Returns the screen's ViewModel. Needs to be overridden so that Hilt can generate code for the factory for the ViewModel class.
     */
    @Composable
    fun viewModel(): T

    /**
     * Override when this page uses arguments.
     *
     * We do it here and not in the [NavigationComponent to keep it centralized]
     */
    fun getArguments(): List<NamedNavArgument> = listOf()

    /**
     * Generates the composable for this route.
     */
    fun composable(
        builder: NavGraphBuilder,
        navHostController: NavHostController
    ) {
        builder.composable(route, getArguments()) {
            var viewModel: T? = null
            Inject(AppComponent.INSTANCE.getViewModelFactory()) {
                viewModel = viewModel()
            }
            val finalViewModel = viewModel!!

            val viewStateAsState by finalViewModel.navigator.navigationState.collectAsState()

            LaunchedEffect(viewStateAsState) {
                Timber.tag("Nav").d("%s updateNavigationState to %s", this, viewStateAsState)
                updateNavigationState(
                    navHostController,
                    viewStateAsState,
                    finalViewModel.navigator::onNavigated
                )
            }

            Content(finalViewModel)
        }
    }

    /**
     * Navigates to [navigationState].
     */
    private fun updateNavigationState(
        navHostController: NavHostController,
        navigationState: NavigationState,
        onNavigated: (navState: NavigationState) -> Unit,
    ) {
        when (navigationState) {
            is NavigationState.NavigateToRoute -> {
                navHostController.navigate(navigationState.route)
                onNavigated(navigationState)
            }

            is NavigationState.PopToRoute -> {
                navHostController.popBackStack(navigationState.staticRoute, false)
                onNavigated(navigationState)
            }

            is NavigationState.NavigateUp -> {
                navHostController.navigateUp()
                onNavigated(navigationState)
            }

            is NavigationState.Idle -> Unit
        }
    }
}

fun <T> SavedStateHandle.getOrThrow(key: String): T =
    get<T>(key) ?: throw IllegalArgumentException(
        "Mandatory argument $key missing in arguments."
    )
