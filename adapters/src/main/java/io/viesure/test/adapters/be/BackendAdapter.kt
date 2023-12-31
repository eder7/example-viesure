package io.viesure.test.adapters.be

import io.viesure.test.usecases.be.LoadArticlesFromBackend
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject
import io.viesure.test.entities.Article as ArticleEntity

private const val BASE_URL = "https://run.mocky.io/v3/"
private const val DATE_FORMAT = "M/d/y"

class BackendAdapter @Inject constructor() : LoadArticlesFromBackend {

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val service: BackendService = retrofit.create(BackendService::class.java)

    override suspend fun invoke(): List<ArticleEntity> {
        return service.listArticles().map { it.toEntity() }
    }
}

private interface BackendService {
    @GET("de42e6d9-2d03-40e2-a426-8953c7c94fb8")
    suspend fun listArticles(): List<Article>
}

private data class Article(
    val id: Int,
    val title: String,
    val description: String,
    val author: String,
    val release_date: String,
    val image: String
)

private fun Article.toEntity() =
    ArticleEntity(
        id,
        title,
        description,
        author,
        parseDate(release_date),
        image
    )

private val dateFormatter = SimpleDateFormat(DATE_FORMAT, Locale.ROOT) // TODO

private fun parseDate(releaseDateString: String): Long {
    return dateFormatter.parse(releaseDateString)!!.time / 1000L
}
