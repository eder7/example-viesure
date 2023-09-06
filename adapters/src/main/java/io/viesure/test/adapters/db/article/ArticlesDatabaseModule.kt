package io.viesure.test.adapters.db.article

import dagger.Binds
import dagger.Module
import io.viesure.test.usecases.GetArticles
import io.viesure.test.usecases.GetCurrentArticles
import io.viesure.test.usecases.PutArticles

@Module
internal abstract class ArticlesDatabaseModule {
    @Binds
    abstract fun bindGetCurrentArticles(articlesRepository: ArticlesRepository): GetCurrentArticles

    @Binds
    abstract fun bindGetArticles(articlesRepository: ArticlesRepository): GetArticles

    @Binds
    abstract fun bindPutArticles(articlesRepository: ArticlesRepository): PutArticles
}
