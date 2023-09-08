package io.viesure.test.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController

@Composable
fun NavigationComponent() {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.ArticleList()
    ) {
        Routes.ArticleList.composable(this, navController)
        Routes.ArticleDetails.composable(this, navController)
    }
}
