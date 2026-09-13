package com.company.androidstarter.core.network

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.ResponseException
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.statement.HttpResponse
import io.ktor.http.isSuccess
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun provideHttpClient(): HttpClient {
        return HttpClient(OkHttp) {
            engine {

            }
            install(ContentNegotiation) {
                json(Json {
                    isLenient = true
                    prettyPrint = true
                    explicitNulls = false
                })
            }
        }
    }
}

suspend inline fun <reified T> safeApiCall(
    crossinline request: suspend () -> HttpResponse
): T {
    return runCatching {
        val response = request()
        if (response.status.isSuccess()) {
            response.body<T>()
        } else {
            throw ResponseException(
                response,
                response.status.description
            )
        }
    }.getOrElse { exception ->
        throw Exception(
            exception.message ?: "Something went wrong!",
            exception
        )
    }
}