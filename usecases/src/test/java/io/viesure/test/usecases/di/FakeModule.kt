package io.viesure.test.usecases.di

import dagger.Module
import dagger.Provides
import io.mockk.spyk
import io.viesure.test.entities.Article
import io.viesure.test.usecases.GetArticles
import io.viesure.test.usecases.PutArticles
import io.viesure.test.usecases.UseCasesModule
import io.viesure.test.usecases.be.LoadArticlesFromBackend
import io.viesure.test.usecases.platform.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.TestScope
import javax.inject.Singleton

@Module(includes = [UseCasesModule::class])
class FakeModule(setup: Setup) {

    val localArticles = setup.localArticles.toMutableList()
    val remoteArticles = setup.remoteArticles.toMutableList()

    @Provides
    @Singleton
    fun provideGetArticles() = object : GetArticles {
        override suspend fun getAll(): List<Article> = localArticles
    }

    @Provides
    @Singleton
    fun provideLoadArticlesFromBackend() = spyk(
        object : LoadArticlesFromBackend {
            override suspend fun invoke(): List<Article> = remoteArticles
        }
    )

    @Provides
    @Singleton
    fun providePutArticles() = object : PutArticles {
        override suspend fun insertOrReplace(articles: List<Article>) {
            articles.forEach {
                val index = localArticles.indexOf(it)
                if (index > 0) {
                    localArticles.removeAt(index)
                }
                localArticles.add(it)
            }
        }
    }

    @Provides
    @Singleton
    fun provideTestDispatcher(): TestDispatcher = StandardTestDispatcher()

    @Provides
    @Singleton
    fun provideTestScope(testDispatcher: TestDispatcher) = TestScope(testDispatcher)

    @Provides
    @Singleton
    fun provideDispatchers(testDispatcher: TestDispatcher) = Dispatchers.create(testDispatcher)

    data class Setup(
        val localArticles: List<Article> = emptyList(),
        val remoteArticles: List<Article> = emptyList()
    )
}