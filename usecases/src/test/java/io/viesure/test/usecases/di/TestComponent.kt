package io.viesure.test.usecases.di

import dagger.Component
import io.viesure.test.usecases.ArticlesControllerTest
import javax.inject.Singleton


@Component(modules = [FakeModule::class])
@Singleton
interface TestComponent {
    fun inject(articlesControllerTest: ArticlesControllerTest)

    @Component.Builder
    interface Builder {
        fun fakeModule(fakeModule: FakeModule): Builder
        fun build(): TestComponent
    }
}
