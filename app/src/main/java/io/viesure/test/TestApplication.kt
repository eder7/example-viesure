package io.viesure.test

import android.app.Application
import io.viesure.test.di.AppComponent
import timber.log.Timber

class TestApplication : Application() {

    init {
        AppComponent.initialize(this)
    }

    override fun onCreate() {
        super.onCreate()

        initTimber()
    }

    private fun initTimber() {
        Timber.plant(Timber.DebugTree())
    }
}
