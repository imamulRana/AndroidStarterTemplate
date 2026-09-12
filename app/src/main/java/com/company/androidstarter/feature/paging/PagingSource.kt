package com.company.androidstarter.feature.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

class AppPagingSource(
    private val httpClient: HttpClient
) : PagingSource<Int, Comment>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Comment> {
        val position = params.key ?: 0
        return try {
            val response = httpClient.get("comments") {
                parameter("skip", position)
                parameter("limit", params.loadSize)
            }.body<CommentResponse>()

            val nextKey = if (response.comments.isEmpty()) {
                null
            } else {
                position + params.loadSize
            }

            LoadResult.Page(
                data = response.comments,
                prevKey = if (position == 0) null else position - params.loadSize,
                nextKey = nextKey
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Comment>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(state.config.pageSize)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(state.config.pageSize)
        }
    }
}
