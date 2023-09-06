package io.viesure.adapters.db

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import io.viesure.test.adapters.db.article.ArticlesDatabaseModule
import javax.inject.Singleton

@Module(includes = [ArticlesDatabaseModule::class])
abstract class DatabaseModule {
    companion object {
        @Provides
        @JvmStatic
        @Singleton
        internal fun provideDatabase(applicationContext: Context) = Room.databaseBuilder(
            applicationContext,
            Database::class.java, "main"
        ).build()
    }
}
