package io.viesure.test.ui.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument
import io.viesure.test.ui.detail.ArticleDetails
import io.viesure.test.ui.detail.ArticleDetailsViewModel
import io.viesure.test.ui.list.ArticleListViewModel
import io.viesure.test.ui.list.ArticlesList
import io.viesure.test.utils.ui.daggerViewModel
import io.viesure.test.utils.ui.navigation.NavigationRoute
import io.viesure.test.utils.ui.navigation.getOrThrow

private const val ARTICLES_ROUTE = "articles/"
private const val ARTICLE_ID_KEY = "ARTICLE_ID"

object Routes {

    internal object ArticleList : NavigationRoute<ArticleListViewModel> {

        override val route = ARTICLES_ROUTE

        operator fun invoke(): String = route

        @Composable
        override fun viewModel(): ArticleListViewModel = daggerViewModel()

        @Composable
        override fun Content(viewModel: ArticleListViewModel) = ArticlesList(viewModel)
    }

    internal object ArticleDetails : NavigationRoute<ArticleDetailsViewModel> {

        override val route = "$ARTICLES_ROUTE{$ARTICLE_ID_KEY}"

        operator fun invoke(articleId: Int): String =
            route.replace("{$ARTICLE_ID_KEY}", "$articleId")

        override fun getArguments(): List<NamedNavArgument> =
            listOf(
                navArgument(ARTICLE_ID_KEY) { type = NavType.IntType }
            )

        fun getIndexFrom(savedStateHandle: SavedStateHandle): Int =
            savedStateHandle.getOrThrow(ARTICLE_ID_KEY)

        @Composable
        override fun viewModel(): ArticleDetailsViewModel = daggerViewModel()

        @Composable
        override fun Content(viewModel: ArticleDetailsViewModel) = ArticleDetails(viewModel)
    }
}
