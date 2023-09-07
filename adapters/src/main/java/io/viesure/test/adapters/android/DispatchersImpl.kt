package io.viesure.test.adapters.android

import io.viesure.test.usecases.platform.Dispatchers
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers as AndroidDispatchers

class DispatchersImpl @Inject constructor() : Dispatchers {
    override val default = AndroidDispatchers.Default
    override val main = AndroidDispatchers.Main
    override val unconfined = AndroidDispatchers.Unconfined
    override val io = AndroidDispatchers.IO
}
