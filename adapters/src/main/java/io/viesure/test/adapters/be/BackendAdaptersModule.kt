package io.viesure.test.adapters.be

import dagger.Binds
import dagger.Module
import io.viesure.test.usecases.be.GetArticlesFromBackend

@Module
abstract class BackendAdaptersModule {

    @Binds
    abstract fun bindGetArticlesFromBackend(backendAdapter: BackendAdapter): GetArticlesFromBackend
}