package io.viesure.test

import android.app.Application
import io.viesure.test.di.DaggerAppComponent

class TestApplication : Application() {

    internal val component = DaggerAppComponent
        .builder()
        .application(this)
        .build()

    init {
        component.inject(this)
    }
}