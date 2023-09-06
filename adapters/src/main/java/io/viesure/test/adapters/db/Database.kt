package io.viesure.adapters.db

import androidx.room.Database
import androidx.room.RoomDatabase
import io.viesure.test.adapters.db.article.Article
import io.viesure.test.adapters.db.article.ArticlesDao

@Database(entities = [Article::class], version = 1)
internal abstract class Database : RoomDatabase() {
    abstract fun articlesDao(): ArticlesDao
}
