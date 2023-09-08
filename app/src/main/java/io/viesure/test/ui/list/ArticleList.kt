package io.viesure.test.ui.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import io.viesure.test.utils.ViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArticlesList(articleSelected: (Int) -> Unit) {

    val viewModel = viewModel(
        ArticleListViewModel::class.java,
        factory = ViewModelFactory.INSTANCE
    )

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text("Hello Viesure 🙂")
                }
            )
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            val uiState = viewModel.uiState.collectAsState(ArticleListViewModel.UiState.INITIAL)

            LazyColumn {
                items(uiState.value.articles) { article ->
                    Box(
                        modifier = Modifier
                            .clickable { articleSelected(article.id) }
                    ) {
                        ArticleListItem(article)
                    }
                }
            }

            if (uiState.value.loading) {
                LinearProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                )
            }
        }
    }
}
