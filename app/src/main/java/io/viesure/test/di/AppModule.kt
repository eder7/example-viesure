package io.viesure.test.di

import android.app.Application
import android.content.Context
import dagger.Binds
import dagger.Module
import io.viesure.test.adapters.AdaptersModule
import io.viesure.test.ui.UiModule

@Module(includes = [AdaptersModule::class, UiModule::class])
internal abstract class AppModule {
    @Binds
    abstract fun bindsApplicationContext(application: Application): Context
}
