package io.viesure.test.adapters

import dagger.Module
import io.viesure.test.adapters.be.BackendAdaptersModule

@Module(includes = [BackendAdaptersModule::class])
abstract class AdaptersModule
