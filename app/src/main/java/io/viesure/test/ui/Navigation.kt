package io.viesure.test.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import io.viesure.test.ui.detail.ArticleDetails
import io.viesure.test.ui.list.ArticlesList

private const val ARTICLES_ROUTE = "articles"
private const val ARTICLE_DETAIL_ROUTE = "article"
private const val ARTICLE_DETAIL_ID_KEY = "articleId"

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val actions = AppActions(navController)

    NavHost(
        navController = navController,
        startDestination = ARTICLES_ROUTE
    ) {
        composable(
            route = ARTICLES_ROUTE
        ) {
            ArticlesList(articleSelected = actions.articleSelected)
        }
        composable(
            route = "${ARTICLE_DETAIL_ROUTE}/{${ARTICLE_DETAIL_ID_KEY}}",
            arguments = listOf(
                navArgument(ARTICLE_DETAIL_ID_KEY) {
                    type = NavType.IntType
                }
            )
        ) { backStackEntry ->
            val arguments = requireNotNull(backStackEntry.arguments)
            ArticleDetails(
                articleId = arguments.getInt(ARTICLE_DETAIL_ID_KEY),
                actions.navigateUp
            )
        }
    }
}

class AppActions(navController: NavController) {

    val articleSelected: (Int) -> Unit = { catId: Int ->
        navController.navigate("${ARTICLE_DETAIL_ROUTE}/$catId")
    }

    val navigateUp: () -> Unit = {
        navController.navigateUp()
    }
}
