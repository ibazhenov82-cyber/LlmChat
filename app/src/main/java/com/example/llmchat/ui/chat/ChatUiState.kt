package com.example.llmchat.ui.chat

import com.example.llmchat.data.model.LlmModel

data class ChatUiState(
    val selectedModel: LlmModel = LlmModel.QWEN3_LOCAL,
    val promptText: String = "",
    val isLoading: Boolean = false,
    val responseText: String = "",
    val elapsedMillis: Long? = null,
    val totalTokens: Int? = null,
    val errorMessage: String? = null
) {
    /** Кнопка "Отправить" активна только когда есть непустой текст задачи и нет запроса в процессе. */
    val isSendEnabled: Boolean
        get() = promptText.isNotBlank() && !isLoading
}
