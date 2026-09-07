package com.example.llmchat.data.model

import com.google.gson.annotations.SerializedName

/**
 * Оба провайдера (DeepSeek API и Ollama в OpenAI-совместимом режиме) используют
 * одинаковую схему запроса/ответа chat/completions, поэтому DTO общие для обоих.
 */

data class ChatMessage(
    val role: String,
    val content: String
)

data class ChatCompletionRequest(
    val model: String,
    val messages: List<ChatMessage>
)

data class ChatCompletionResponse(
    val choices: List<ChatChoice>? = null,
    val usage: ChatUsage? = null
)

data class ChatChoice(
    val message: ChatMessage? = null
)

data class ChatUsage(
    @SerializedName("prompt_tokens") val promptTokens: Int? = null,
    @SerializedName("completion_tokens") val completionTokens: Int? = null,
    @SerializedName("total_tokens") val totalTokens: Int? = null
)
