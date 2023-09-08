package io.viesure.test.utils.ui.navigation

import dagger.Binds
import dagger.Module

@Module
abstract class NavigationModule {
    @Binds
    abstract fun bindNavigator(navigatorImpl: NavigatorImpl): Navigator
}
