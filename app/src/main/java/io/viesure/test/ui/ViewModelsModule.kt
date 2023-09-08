package io.viesure.test.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.MapKey
import dagger.Module
import dagger.multibindings.IntoMap
import io.viesure.test.ui.detail.ArticleDetailsViewModel
import io.viesure.test.ui.list.ArticleListViewModel
import io.viesure.test.utils.ViewModelFactory
import io.viesure.test.utils.ViewModelFactoryModule
import kotlin.reflect.KClass

@Module(includes = [ViewModelFactoryModule::class])
internal abstract class ViewModelsModule {
    @Binds
    @IntoMap
    @ViewModelKey(ArticleListViewModel::class)
    internal abstract fun bindArticlesListViewModel(articleListViewModel: ArticleListViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ArticleDetailsViewModel::class)
    internal abstract fun bindArticleDetailsViewModel(articleListViewModel: ArticleDetailsViewModel): ViewModel

    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}

@MapKey
internal annotation class ViewModelKey(val value: KClass<out ViewModel>)
