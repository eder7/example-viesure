package io.viesure.test.di

import android.app.Application
import android.content.Context
import dagger.Binds
import dagger.Module
import io.viesure.test.adapters.AdaptersModule

@Module(includes = [AdaptersModule::class, ViewModelModule::class])
abstract class AppModule {
    @Binds
    abstract fun bindsApplicationContext(application: Application): Context
}
