package com.company.androidstarter.feature.paging

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey

@Composable
fun PagingTest(
    modifier: Modifier = Modifier, viewModel: PagingTestViewModel
) {
    val lazyPagingItems = viewModel.apiComments.collectAsLazyPagingItems()
    val edits by viewModel.localEdits.collectAsStateWithLifecycle()

    LazyColumn {
        items(
            count = lazyPagingItems.itemCount,
            key = lazyPagingItems.itemKey { it.id }
        ) { index ->
            val original = lazyPagingItems[index]
            val displayed = original?.let { comment ->
                edits[comment.id]?.let { comment.copy(body = it) } ?: comment
            }
            ListItem(
                leadingContent = {
                    Text(text = displayed?.id.toString())
                },
                headlineContent = {
                    Text(displayed?.body.orEmpty())
                },
                supportingContent = {
                    Text(displayed?.user?.fullName.orEmpty())
                })
        }
    }
}