package com.example.llmchat.data.model

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
    val choices: List<ChatChoice>? = null
)

data class ChatChoice(
    val message: ChatMessage? = null
)
