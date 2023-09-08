package io.viesure.test.usecases

import dagger.Binds
import dagger.Module

@Module
abstract class UseCasesModule {
    @Binds
    abstract fun bindGetArticlesSyncing(articlesController: ArticlesController): GetArticlesSyncing
}
