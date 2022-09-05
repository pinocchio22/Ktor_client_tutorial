package com.example.ktor_client_tutorial

import android.util.Log
import io.ktor.client.*
import io.ktor.client.features.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.features.logging.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.serialization.Serializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer


object KtorClient {

    // json setting
    private val json = Json {
        encodeDefaults = true
        ignoreUnknownKeys = true
    }

    // http client setting
    var httpClient = HttpClient {
        // setting json
        install(JsonFeature) {
            serializer = KotlinxSerializer(json = json)
        }

        // setting logging
        install(Logging) {
            logger = object : Logger {
                override fun log(message: String) {
                    Log.d("TAG", "api log : $message")
                }
            }
            level = LogLevel.ALL
        }
        install(HttpTimeout) {
            requestTimeoutMillis = 15_000
            connectTimeoutMillis = 15_000
            socketTimeoutMillis = 15_000
        }

        // 기본적인 api 호출시하는 setting
        defaultRequest {
            contentType(ContentType.Application.Json)
            accept(ContentType.Application.Json)
        }
    }
}