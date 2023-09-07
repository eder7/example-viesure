package io.viesure.test.usecases

import io.mockk.coEvery
import io.mockk.coVerify
import io.viesure.test.entities.Article
import io.viesure.test.usecases.be.LoadArticlesFromBackend
import io.viesure.test.usecases.di.DaggerTestComponent
import io.viesure.test.usecases.di.FakeModule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Test
import java.time.Instant
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
class ArticlesControllerTest {

    @Inject
    lateinit var getArticles: GetArticles

    @Inject
    lateinit var getArticlesSyncing: GetArticlesSyncing

    @Inject
    lateinit var loadArticlesFromBackend: LoadArticlesFromBackend

    @Inject
    lateinit var scope: TestScope

    @Test
    fun `Syncing entries from backend with no local entries`() {
        val expected = List(3) { mockArticle(it + 1) }

        setUp(
            FakeModule.Setup(
                localArticles = emptyList(),
                remoteArticles = expected
            )
        )

        scope.runTest {
            val actual = getArticles.getAll()
            assertEquals(expected, actual)
        }
    }

    @Test
    fun `Syncing entries from backend with some existing entries`() {
        val expected = List(3) { mockArticle(it) }

        setUp(
            FakeModule.Setup(
                localArticles = listOf(/* 1 missing */ mockArticle(2) /* 3 missing */),
                remoteArticles = expected
            )
        )

        scope.runTest {
            val actual = getArticles.getAll()
            assertTrue("Local articles don't match remote", actual.toSet() == expected.toSet())
        }
    }

    @Test
    fun `Failed backend access is retried 3 times with 2 seconds in between and then fails`() {
        val backoffMillis: Long = 2000

        setUp()

        coEvery { loadArticlesFromBackend() } throws Exception()

        scope.runCurrent()

        coVerify(exactly = 1) { loadArticlesFromBackend() }

        scope.advanceTimeBy(backoffMillis)

        coVerify(exactly = 1) { loadArticlesFromBackend() }

        scope.advanceTimeBy(1)

        coVerify(exactly = 2) { loadArticlesFromBackend() }

        scope.advanceTimeBy(backoffMillis)

        coVerify(exactly = 3) { loadArticlesFromBackend() }

        scope.advanceTimeBy(backoffMillis)

        coVerify(exactly = 3) { loadArticlesFromBackend() }
    }

    @Test
    fun `Syncing state is updated`() {

        val delayMillis = 1000L

        setUp()

        coEvery { loadArticlesFromBackend() } coAnswers {
            delay(delayMillis)
            callOriginal()
        }

        scope.runCurrent()

        assertTrue(getArticlesSyncing.articlesSyncingStream.value)

        scope.advanceTimeBy(delayMillis + 1)

        assertFalse(getArticlesSyncing.articlesSyncingStream.value)
    }

    private fun setUp(setup: FakeModule.Setup = FakeModule.Setup()): FakeModule {
        val fakeModule = FakeModule(setup)

        DaggerTestComponent.builder()
            .fakeModule(fakeModule)
            .build()
            .inject(this)

        return fakeModule
    }

    private fun mockArticle(id: Int) = Article(
        id,
        "This is article $id",
        "This describes article $id in length",
        "author$id@test.com",
        Instant.now().epochSecond,
        "https://dummy.com/images/$id.png"
    )
}
