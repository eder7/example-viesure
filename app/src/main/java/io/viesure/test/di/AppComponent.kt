package io.viesure.test.di

import android.app.Application
import dagger.BindsInstance
import dagger.Component
import io.viesure.test.TestApplication
import io.viesure.test.ui.MainActivity
import io.viesure.test.utils.ViewModelFactory
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

    val viewModelFactory: ViewModelFactory

    fun inject(testApplication: TestApplication)
    fun inject(mainActivity: MainActivity)

    companion object {
        lateinit var INSTANCE: AppComponent

        fun initialize(application: TestApplication): AppComponent {
            INSTANCE = DaggerAppComponent
                .builder()
                .application(application)
                .build()
            return INSTANCE
        }
    }
}
