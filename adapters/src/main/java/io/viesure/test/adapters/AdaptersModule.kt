package io.viesure.test.adapters

import dagger.Module
import io.viesure.test.adapters.be.BackendAdaptersModule
import io.viesure.test.usecases.repository.RepositoriesModule

@Module(includes = [BackendAdaptersModule::class, RepositoriesModule::class])
abstract class AdaptersModule
