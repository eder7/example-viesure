package io.viesure.test.usecases

import dagger.Module
import io.viesure.test.usecases.repository.RepositoriesModule

@Module(includes = [RepositoriesModule::class])
class UsecasesModule
