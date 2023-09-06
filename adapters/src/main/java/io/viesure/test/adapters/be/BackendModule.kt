package io.viesure.test.adapters.be

import dagger.Binds
import dagger.Module
import io.viesure.test.usecases.be.LoadArticlesFromBackend

@Module
abstract class BackendModule {

    @Binds
    abstract fun bindLoadArticlesFromBackend(backendAdapter: BackendAdapter): LoadArticlesFromBackend
}