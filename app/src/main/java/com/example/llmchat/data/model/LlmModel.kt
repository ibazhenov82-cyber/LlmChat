package com.example.llmchat.data.model

/**
 * Провайдер, через который обслуживается модель.
 */
enum class LlmProvider {
    /** Локальный inference-сервер Ollama (OpenAI-совместимый endpoint /v1/chat/completions). */
    OLLAMA_LOCAL,

    /** Официальный DeepSeek API (https://api.deepseek.com). */
    DEEPSEEK
}

/**
 * Модели LLM, доступные для выбора в интерфейсе.
 *
 * @param id идентификатор модели, который отправляется в поле "model" запроса.
 * @param displayName человекочитаемое название для UI.
 * @param provider провайдер, определяющий какой Retrofit-клиент использовать.
 */
enum class LlmModel(
    val id: String,
    val displayName: String,
    val provider: LlmProvider
) {
    QWEN3_LOCAL(
        id = "qwen3:0.6b",
        displayName = "Qwen3 0.6B",
        provider = LlmProvider.OLLAMA_LOCAL
    ),
    DEEPSEEK_FLASH(
        id = "deepseek-v4-flash",
        displayName = "DeepSeek V4 Flash",
        provider = LlmProvider.DEEPSEEK
    ),
    DEEPSEEK_PRO(
        id = "deepseek-v4-pro",
        displayName = "DeepSeek V4 Pro",
        provider = LlmProvider.DEEPSEEK
    )
}
