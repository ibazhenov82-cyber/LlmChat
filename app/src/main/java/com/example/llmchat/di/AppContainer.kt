package com.example.llmchat.di

import com.example.llmchat.data.network.ChatApi
import com.example.llmchat.data.network.NetworkModule
import com.example.llmchat.data.repository.LlmRepository
import com.example.llmchat.data.repository.LlmRepositoryImpl

/**
 * Небольшой сервис-локатор вместо Hilt/Koin — для проекта такого размера
 * ручной DI проще, стабильнее и не тянет дополнительных зависимостей.
 */
class AppContainer {

    private val deepSeekApi: ChatApi by lazy { NetworkModule.provideDeepSeekApi() }
    private val ollamaApi: ChatApi by lazy { NetworkModule.provideOllamaApi() }

    val llmRepository: LlmRepository by lazy {
        LlmRepositoryImpl(deepSeekApi = deepSeekApi, ollamaApi = ollamaApi)
    }
}
