package io.viesure.test.ui

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.MapKey
import dagger.Module
import dagger.multibindings.IntoMap
import io.viesure.test.ui.detail.ArticleDetailsViewModel
import io.viesure.test.ui.list.ArticleListViewModel
import io.viesure.test.utils.ui.viewmodelfactory.ViewModelAssistedFactory
import io.viesure.test.utils.ui.viewmodelfactory.ViewModelFactoryModule
import kotlin.reflect.KClass

/**
 * Module exclusively mapping ViewModel::class type to instances
 */
@Module(includes = [ViewModelFactoryModule::class])
internal abstract class ViewModelsModule {

    @Binds
    @IntoMap
    @ViewModelKey(ArticleListViewModel::class)
    internal abstract fun bindArticleListViewModel(factory: ArticleListViewModel.Factory): ViewModelAssistedFactory<*>

    @Binds
    @IntoMap
    @ViewModelKey(ArticleDetailsViewModel::class)
    internal abstract fun bindArticleDetailsViewModel(factory: ArticleDetailsViewModel.Factory): ViewModelAssistedFactory<*>
}

@MapKey
internal annotation class ViewModelKey(val value: KClass<out ViewModel>)
