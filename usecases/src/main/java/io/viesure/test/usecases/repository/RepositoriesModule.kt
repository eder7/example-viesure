package io.viesure.test.usecases.repository

import dagger.Binds
import dagger.Module
import io.viesure.test.usecases.GetArticlesStreamCached

@Module
abstract class RepositoriesModule {
    @Binds
    abstract fun bindGetArticlesStreamCached(articlesRepository: ArticlesRepository): GetArticlesStreamCached
}
