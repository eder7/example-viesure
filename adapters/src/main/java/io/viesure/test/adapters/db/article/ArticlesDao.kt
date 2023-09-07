package io.viesure.test.adapters.db.article

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
internal interface ArticlesDao {
    @Query("SELECT * FROM Article")
    fun getAllStream(): Flow<List<Article>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(articles: List<Article>)
}

@Entity
internal data class Article(
    @PrimaryKey val id: Int,
    @NonNull @ColumnInfo(name = "title") val title: String,
    @NonNull @ColumnInfo(name = "description") val description: String,
    @NonNull @ColumnInfo(name = "author_email") val authorEmail: String,
    @NonNull @ColumnInfo(name = "release_date_seconds") val releaseDateSeconds: Long,
    @NonNull @ColumnInfo(name = "image_uri") val imageUri: String
) {
    companion object {
        fun fromEntity(entity: io.viesure.test.entities.Article) = Article(
            entity.id,
            entity.title,
            entity.description,
            entity.authorEmail,
            entity.releaseDateSeconds,
            entity.imageUri
        )
    }
}

internal fun Article.toEntity() = io.viesure.test.entities.Article(
    id,
    title,
    description,
    authorEmail,
    releaseDateSeconds,
    imageUri
)
