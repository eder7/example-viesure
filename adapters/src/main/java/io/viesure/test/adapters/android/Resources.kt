package io.viesure.test.adapters.android

import android.content.Context
import androidx.annotation.StringRes
import io.viesure.test.usecases.platform.Strings
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StringResourceProvider @Inject constructor(applicationContext: Context) : Strings {
    private val resources = applicationContext.resources

    override fun invoke(@StringRes resourceId: Int): String = resources.getString(resourceId)
}
