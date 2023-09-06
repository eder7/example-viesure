package io.viesure.test.ui

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import io.viesure.test.TestApplication
import io.viesure.test.ui.theme.ViesureTheme
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainActivity : ComponentActivity() {

    @Inject
    lateinit var modelFactory: ViewModelProvider.Factory

    override fun onCreate(savedInstanceState: Bundle?) {

        (applicationContext as TestApplication).component.inject(this)

        super.onCreate(savedInstanceState)

        val viewModel = modelFactory.create(ArticleListViewModel::class.java);

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiActions.collect {
                    when (it) {
                        is ArticleListViewModel.UiAction.ShowError -> {
                            AlertDialog.Builder(this@MainActivity)
                                .setMessage(it.message)
                                .setPositiveButton("OK") { dialogInterface: DialogInterface, _ ->
                                    dialogInterface.dismiss()
                                }
                                .create()
                                .show()
                        }
                    }
                }
            }
        }

        setContent {
            App(viewModel)
        }
    }

    @Composable
    @OptIn(ExperimentalMaterial3Api::class)
    private fun App(viewModel: ArticleListViewModel) {
        ViesureTheme {
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
                Column(
                    modifier = Modifier.padding(innerPadding),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    val uiState =
                        viewModel.uiState.collectAsState(initial = ArticleListViewModel.UiState.INITIAL)
                    if (uiState.value.loading)
                        LinearProgressIndicator()
                    WrappingImagesList(uiState.value) // TODO rename
                }

            }
        }
    }
}

@Composable
fun WrappingImagesList(state: ArticleListViewModel.UiState) {
    LazyColumn {
        items(state.articles) { article ->
            ArticleListItem(article)
        }
    }
}

