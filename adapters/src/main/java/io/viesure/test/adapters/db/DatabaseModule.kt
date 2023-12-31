package io.viesure.adapters.db

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import io.viesure.test.adapters.db.article.ArticlesDatabaseModule
import io.viesure.test.adapters.db.article.DatabaseEncryptionKey
import net.sqlcipher.database.SQLiteDatabase
import net.sqlcipher.database.SupportFactory
import javax.inject.Singleton

private const val DATABASE_NAME = "main-encrypted"

@Module(includes = [ArticlesDatabaseModule::class])
abstract class DatabaseModule {
    companion object {
        @Provides
        @JvmStatic
        @Singleton
        internal fun provideDatabase(
            applicationContext: Context,
            databaseEncryptionKey: DatabaseEncryptionKey
        ) = Room.databaseBuilder(
            applicationContext,
            Database::class.java,
            DATABASE_NAME
        )
            .openHelperFactory(
                SupportFactory(
                    SQLiteDatabase.getBytes(databaseEncryptionKey.getOrCreate())
                )
            )
            .build()
    }
}
