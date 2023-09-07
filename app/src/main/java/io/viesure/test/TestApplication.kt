package io.viesure.test

import android.app.Application
import io.viesure.test.di.DaggerAppComponent
import timber.log.Timber

class TestApplication : Application() {

    internal val component = DaggerAppComponent
        .builder()
        .application(this)
        .build()

    init {
        component.inject(this) // For potential use of injections in the application class
    }

    override fun onCreate() {
        super.onCreate()

        initTimber()
    }

    private fun initTimber() {
        Timber.plant(Timber.DebugTree())
    }
}
