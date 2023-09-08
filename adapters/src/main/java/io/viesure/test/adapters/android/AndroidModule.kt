package io.viesure.test.adapters.android

import dagger.Binds
import dagger.Module
import io.viesure.test.usecases.platform.Dispatchers
import io.viesure.test.usecases.platform.Strings

@Module
internal abstract class AndroidModule {
    @Binds
    abstract fun bindDispatchers(dispatchersImpl: DispatchersImpl): Dispatchers

    @Binds
    abstract fun bindStrings(stringResourceProvider: StringResourceProvider): Strings
}
