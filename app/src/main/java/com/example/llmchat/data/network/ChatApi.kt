package com.example.llmchat.data.network

import com.example.llmchat.data.model.ChatCompletionRequest
import com.example.llmchat.data.model.ChatCompletionResponse
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * Общий контракт для DeepSeek API и локального Ollama (OpenAI-совместимый режим).
 * Различие между провайдерами — только в base URL и заголовках авторизации,
 * которые настраиваются на уровне [NetworkModule].
 */
interface ChatApi {

    @POST("v1/chat/completions")
    suspend fun createChatCompletion(@Body request: ChatCompletionRequest): ChatCompletionResponse
}
