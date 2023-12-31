package io.viesure.test.adapters

import dagger.Module
import io.viesure.adapters.db.DatabaseModule
import io.viesure.test.adapters.android.AndroidModule
import io.viesure.test.adapters.be.BackendModule
import io.viesure.test.usecases.UseCasesModule

@Module(
    includes = [
        BackendModule::class,
        UseCasesModule::class,
        DatabaseModule::class,
        AndroidModule::class
    ]
)
abstract class AdaptersModule
