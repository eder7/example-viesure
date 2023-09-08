package io.viesure.test.utils.ui.navigation

import androidx.annotation.VisibleForTesting
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

interface Navigator {
    val navigationState: StateFlow<NavigationState>

    fun onNavigated(state: NavigationState)
    fun navigateUp()
    fun popToRoute(route: String)
    fun navigateToRoute(route: String)
}

@Singleton
class NavigatorImpl @Inject constructor() : Navigator {

    override val navigationState: MutableStateFlow<NavigationState> =
        MutableStateFlow(NavigationState.Idle)

    override fun onNavigated(state: NavigationState) {
        // clear navigation state, if state is the current state
        navigationState.compareAndSet(state, NavigationState.Idle)
    }

    override fun popToRoute(route: String) = navigate(NavigationState.PopToRoute(route))

    override fun navigateUp() = navigate(NavigationState.NavigateUp())

    override fun navigateToRoute(route: String) = navigate(NavigationState.NavigateToRoute(route))

    @VisibleForTesting
    fun navigate(state: NavigationState) {
        navigationState.value = state
    }
}

sealed class NavigationState {

    object Idle : NavigationState()

    data class NavigateToRoute(
        val route: String,
        val id: String = UUID.randomUUID().toString()
    ) : NavigationState()

    data class PopToRoute(
        val staticRoute: String,
        val id: String = UUID.randomUUID().toString()
    ) : NavigationState()

    data class NavigateUp(val id: String = UUID.randomUUID().toString()) : NavigationState()
}
