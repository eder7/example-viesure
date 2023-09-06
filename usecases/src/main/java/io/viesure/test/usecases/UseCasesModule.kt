package io.viesure.test.usecases

import dagger.Binds
import dagger.Module

@Module
abstract class UseCasesModule {
    @Binds
    internal abstract fun bindGetArticlesSyncing(articlesController: ArticlesController): GetArticlesSyncing
}
