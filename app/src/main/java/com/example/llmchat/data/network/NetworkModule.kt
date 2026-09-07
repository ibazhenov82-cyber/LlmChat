package com.example.llmchat.data.network

import com.example.llmchat.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Простая фабрика сетевых клиентов без DI-фреймворка (Hilt/Koin намеренно не используются,
 * чтобы держать проект максимально простым и стабильным).
 */
object NetworkModule {

    private const val DEEPSEEK_BASE_URL = "https://api.deepseek.com/"
    private const val CONNECT_TIMEOUT_SECONDS = 30L
    private const val READ_TIMEOUT_SECONDS = 60L
    private const val WRITE_TIMEOUT_SECONDS = 60L

    /** Клиент для DeepSeek API (deepseek-v4-flash / deepseek-v4-pro), с Bearer-авторизацией. */
    fun provideDeepSeekApi(): ChatApi {
        val authInterceptor = Interceptor { chain ->
            val authorizedRequest = chain.request().newBuilder()
                .addHeader("Authorization", "Bearer ${BuildConfig.DEEPSEEK_API_KEY}")
                .addHeader("Content-Type", "application/json")
                .build()
            chain.proceed(authorizedRequest)
        }

        return buildRetrofit(
            baseUrl = DEEPSEEK_BASE_URL,
            client = buildOkHttpClient(authInterceptor)
        )
    }

    /** Клиент для локальной модели qwen3:0.6b через Ollama. Адрес берётся из local.properties. */
    fun provideOllamaApi(): ChatApi {
        val baseUrl = BuildConfig.LOCAL_HOST_QWEN3
            .ifBlank { "http://10.0.2.2:11434/" }
            .let { if (it.endsWith("/")) it else "$it/" }

        return buildRetrofit(
            baseUrl = baseUrl,
            client = buildOkHttpClient(authInterceptor = null)
        )
    }

    private fun buildOkHttpClient(authInterceptor: Interceptor?): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        }

        return OkHttpClient.Builder()
            .connectTimeout(CONNECT_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .readTimeout(READ_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .writeTimeout(WRITE_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .apply { authInterceptor?.let { addInterceptor(it) } }
            .addInterceptor(loggingInterceptor)
            .build()
    }

    private fun buildRetrofit(baseUrl: String, client: OkHttpClient): ChatApi {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ChatApi::class.java)
    }
}
