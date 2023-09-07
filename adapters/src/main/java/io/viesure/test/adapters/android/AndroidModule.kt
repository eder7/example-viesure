package io.viesure.test.adapters.android

import dagger.Binds
import dagger.Module
import io.viesure.test.usecases.platform.Dispatchers

@Module
abstract class AndroidModule {
    @Binds
    abstract fun bindDispatchers(dispatchersImpl: DispatchersImpl): Dispatchers
}
