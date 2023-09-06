package io.viesure.test.di

import android.app.Application
import dagger.BindsInstance
import dagger.Component
import io.viesure.test.ui.MainActivity
import io.viesure.test.TestApplication
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }
    fun inject(testApplication: TestApplication)
    fun inject(mainActivity: MainActivity)
}
