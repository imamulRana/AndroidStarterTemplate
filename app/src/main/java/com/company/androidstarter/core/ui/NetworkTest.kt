package com.company.androidstarter.core.ui

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.company.androidstarter.core.network.safeApiCall
import io.ktor.client.HttpClient
import io.ktor.client.request.get

@Composable
fun JSONTest(
    modifier: Modifier = Modifier,
    client: HttpClient
) {
    val snackbarHostState = remember { SnackbarHostState() }
    var errorMessage by remember {
        mutableStateOf<String?>(null)
    }

    val posts by produceState(
        initialValue = emptyList()
    ) {
        runCatching {
            value =
                safeApiCall<List<String>> {
                    client.get(
                        "https://jsonplaceholder.typicode.com/posts"
                    )
                }
        }.onFailure {
            errorMessage = it.message
        }
    }

    LaunchedEffect(errorMessage) {
        errorMessage?.let {
            snackbarHostState.showSnackbar(it)
            errorMessage = null
        }
    }

    Scaffold(
        modifier = modifier,
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState
            )
        }) {
        LazyColumn(
            contentPadding = it
        ) {
            items(posts) { post ->
                ListItem(headlineContent = { Text(post) })
            }
        }
    }
}