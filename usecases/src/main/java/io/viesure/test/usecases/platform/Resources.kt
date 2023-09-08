package io.viesure.test.usecases.platform

import androidx.annotation.StringRes

interface Strings {
    operator fun invoke(@StringRes resourceId: Int): String
}
