package io.viesure.test.adapters.db.article

import dagger.Binds
import dagger.Module
import io.viesure.test.usecases.GetArticle
import io.viesure.test.usecases.GetArticles
import io.viesure.test.usecases.CurrentSortedArticlesStream
import io.viesure.test.usecases.PutArticles

@Module
internal abstract class ArticlesDatabaseModule {
    @Binds
    abstract fun bindCurrentSortedArticlesStream(sortedArticlesRepository: SortedArticlesRepository): CurrentSortedArticlesStream

    @Binds
    abstract fun bindGetArticles(sortedArticlesRepository: SortedArticlesRepository): GetArticles

    @Binds
    abstract fun bindGetArticle(sortedArticlesRepository: SortedArticlesRepository): GetArticle

    @Binds
    abstract fun bindPutArticles(sortedArticlesRepository: SortedArticlesRepository): PutArticles
}
