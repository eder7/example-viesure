package io.viesure.test.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@Composable
@Preview(widthDp = 400)
internal fun ArticleListItem(
    article: ArticleListViewModel.Article = ArticleListViewModel.Article.createDummy()
) {
    Row(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .padding(8.dp)
    ) {
        Column(Modifier.padding(3.dp)) {
            AsyncImage(
                model = article.imageUri,
                contentDescription = "Article image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .background(Color.Gray)
                    .size(50.dp)
            )
        }
        Spacer(Modifier.width(6.dp))
        Column {
            Text(
                text = article.title,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = article.description,
                overflow = TextOverflow.Ellipsis,
                maxLines = 2
            )
        }
    }
}