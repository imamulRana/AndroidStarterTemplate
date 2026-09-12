package com.company.androidstarter.feature.paging

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.asItemSnapshotListFlow
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.patch
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import javax.inject.Inject

@Serializable
data class UpdateCommentRequest(
    val body: String
)

@Serializable
data class User(
    val id: Long,
    val username: String,
    val fullName: String
)

@Serializable
data class Comment(
    val id: Long,
    val body: String,
    val postId: Long,
    val likes: Long,
    val user: User
)

@Serializable
data class CommentResponse(
    val comments: List<Comment>,
    val total: Int,
    val skip: Int,
    val limit: Int
)

@HiltViewModel
class PagingTestViewModel @Inject constructor(
    private val httpClient: HttpClient
) : ViewModel() {

    val apiComments = Pager(
        config = PagingConfig(pageSize = 20, enablePlaceholders = true),
        pagingSourceFactory = { AppPagingSource(httpClient) }
    ).flow.cachedIn(viewModelScope)

    // Local overrides, keyed by item id, applied on top of whatever
    // Paging loaded — this is what avoids refresh().
    private val _localEdits = MutableStateFlow<Map<Long, String>>(emptyMap())
    val localEdits: StateFlow<Map<Long, String>> = _localEdits

    fun patchComment(commentId: Long, newBody: String) {
        viewModelScope.launch {
            httpClient.patch("comments/$commentId") {
                contentType(ContentType.Application.Json)
                setBody(mapOf("body" to newBody))
            }.body<Comment>()

            _localEdits.update { it + (commentId to newBody) }
        }
    }
}