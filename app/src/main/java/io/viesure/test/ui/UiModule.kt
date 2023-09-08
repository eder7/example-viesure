package io.viesure.test.ui

import dagger.Module
import io.viesure.test.utils.ui.navigation.NavigationModule

@Module(includes = [ViewModelsModule::class, NavigationModule::class])
abstract class UiModule
