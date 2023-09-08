package io.viesure.test.utils.ui.viewmodelfactory

import dagger.Binds
import dagger.Module

@Module
abstract class ViewModelFactoryModule {
    @Binds
    abstract fun bindDaggerViewModelAssistedFactory(factory: DaggerViewModelAssistedFactory): ViewModelFactory
}
